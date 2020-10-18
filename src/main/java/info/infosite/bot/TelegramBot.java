package info.infosite.bot;

import info.infosite.database.auth.User;
import info.infosite.database.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    UserRepository userRepository;

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
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        switch (text) {
            case "/start":
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

    private boolean CheckUserKey(Update update) {
        String text = update.getMessage().getText();
        int idUser = -1;
        for (UserRequest userRequest : userRequests) {
            try {
                userRequest.getUserKey().equals(text);
                return true;
            } catch (NullPointerException e) {
                if (userRequest.getChatId().equals(update.getMessage().getChatId())) {
                    idUser = userRequests.indexOf(userRequest);
                }
            }
        }
        try {
            User user = userRepository.findUserByUserKey(text);
            UserRequest userRequest = userRequests.get(idUser);
            userRequest.addMessage(text);
            userRequest.setUserKey(text);
            sendMessage(update, "Добро пожаловать " + user.getFio());
            return true;
        } catch (Exception e) {
            sendMessage(update, NO_KEY_MESSAGE);
        }
        return false;
    }
}
