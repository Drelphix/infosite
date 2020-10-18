package info.infosite.bot;

import info.infosite.database.ChatId;
import info.infosite.database.ChatIdRepository;
import info.infosite.database.auth.User;
import info.infosite.database.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableAsync
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatIdRepository chatIdRepository;

    private static final String IF_COMMAND = "/";
    private static final String START_MESSAGE = "Добро пожаловать. Для продолжения пожалуйста введите уникальный идентификатор." +
            "В случае его отсутствия обратитесь к системным администраторам.";
    private static final String NEW_REQUEST_MESSAGE = "Пожалуйста опишите проблему как можно точнее";
    private static final String NO_KEY_MESSAGE = "Извините, но этот код неверный. Обратитесь к системным администраторам";
    List<UserRequest> userRequests = new ArrayList<>();

    @Override
    public String getBotUsername() {
        return "AutospaceAsistBot";
    }

    @Override
    public String getBotToken() {
        return "1308591163:AAFxENlXd6urBpEY4fj-VpoHfyJhVculU74";
    }

    @Override
    @Async
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        switch (text) {
            case "/start":
                try {
                    sendMessage(update, "Добро пожаловать, " + helloMessage(update.getMessage().getChatId()));
                    break;
                } catch (NullPointerException e) {
                    for (UserRequest userRequest : userRequests) {
                        if (userRequest.getChatId().equals(update.getMessage().getChatId())) {
                            CheckUserKey(update);
                            break;
                        }
                    }
                }

                sendMessage(update, START_MESSAGE);
                userRequests.add(new UserRequest(update.getMessage().getChatId(), "/start"));
                break;
            case "/new":
                sendMessage(update, NEW_REQUEST_MESSAGE);
                break;
            default:
                text = checkLastCommand(update);
                switch (text) {
                    case "/start":
                        CheckUserKey(update);
                }
                break;
        }
    }

    private void sendMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String checkLastCommand(Update update) {
        Long chatId = update.getMessage().getChatId();
        for (UserRequest userRequest : userRequests) {
            if (userRequest.getChatId().equals(chatId)) {
                for (int i = userRequest.chat.size() - 1; i >= 0; i--) {
                    if (userRequest.chat.get(i).startsWith(IF_COMMAND)) {
                        return userRequest.chat.get(i);
                    }
                }
            }
        }
        return null;
    }

    private void CheckUserKey(Update update) {
        String text = update.getMessage().getText();
        int idUser = -1;
        for (UserRequest userRequest : userRequests) {
            try {
                sendMessage(update, "Добро пожаловать, " + helloMessage(text));
                chatIdRepository.save(new ChatId(update.getMessage().getChatId()));
                return;
            } catch (NullPointerException e) {
                if (userRequest.getChatId().equals(update.getMessage().getChatId())) {
                    idUser = userRequests.indexOf(userRequest);
                }
            }
        }
        try {
            UserRequest userRequest = userRequests.get(idUser);
            userRequest.addMessage(text);
            userRequest.setUserKey(text);
            sendMessage(update, "Добро пожаловать " + helloMessage(update.getMessage().getChatId()));
            chatIdRepository.save(new ChatId(update.getMessage().getChatId()));
        } catch (Exception e) {
            sendMessage(update, NO_KEY_MESSAGE);
            e.printStackTrace();
        }
    }

    private String helloMessage(String key) {
        User user = userRepository.findUserByUserKey(key);
        return user.getInfo();
    }

    private String helloMessage(Long chatId) {
        User user = userRepository.findUserByChatId(chatId);
        return user.getInfo();
    }
}
