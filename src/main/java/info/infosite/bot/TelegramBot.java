package info.infosite.bot;

import info.infosite.entities.auth.User;
import info.infosite.entities.auth.UserRepository;
import info.infosite.entities.bot.Chat;
import info.infosite.entities.bot.ChatIdRepository;
import info.infosite.entities.request.Request;
import info.infosite.entities.request.RequestRepository;
import info.infosite.entities.request.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.TransactionalException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Component
@EnableAsync
public class TelegramBot extends TelegramLongPollingBot {
    private static final String IF_COMMAND = "/";
    private static final String START_MESSAGE = "Добро пожаловать. Для продолжения пожалуйста введите уникальный идентификатор." +
            "В случае его отсутствия обратитесь к системным администраторам.";
    private static final String NEW_REQUEST_MESSAGE = "Пожалуйста опишите вашу проблему как можно точнее, в случае наличия тимвьюера оставте его в сообщении";
    private static final String NO_KEY_MESSAGE = "Извините, но этот код неверный. Обратитесь к системным администраторам";
    private static final String ERROR_MESSAGE = "Извините, но во время выполнения последней команды произошла ошибка.";
    private static final String REQUEST_MESSAGE = "Создана новая заявка ";
    private static final String OK_REQUEST_MESSAGE = "Ваша заявка успешно оформлена";
    private static final String UNKNOWN_USER_MESSAGE = "Извините, мы вас не знаем";
    private static final String HELP_MESSAGE = "Этот бот - часть хелпдеск системы, призванный к упрощению помощи пользователям и повышению реагирования" +
            "на проблемы в работе. Создан @Drelphix";
    private static final String CANCEL_MESSAGE = "Создание заявки отменено.";
    private static final String WAITING_MESSAGE = "Ожидаю команду...";

    static {
        ApiContextInitializer.init();
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatIdRepository chatIdRepository;
    @Autowired
    RequestRepository requestRepository;
    List<UserRequest> userRequests = new ArrayList<>();

    @Override
    public String getBotUsername() {
        return "AutospaceAsistBot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    @Async
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        switch (text) {
            case "/start":
                try {
                    sendMessage(chatId, "Добро пожаловать, " + helloMessage(update.getMessage().getChatId()));
                    break;
                } catch (NullPointerException e) {
                    for (UserRequest userRequest : userRequests) {
                        if (userRequest.getChatId().equals(update.getMessage().getChatId())) {
                            CheckUserKey(update);
                            break;
                        }
                    }
                    sendMessage(chatId, START_MESSAGE);
                    userRequests.add(new UserRequest(update.getMessage().getChatId(), text));
                    break;
                }
            case "/order":
                try {
                    helloMessage(chatId);
                    sendMessage(chatId, NEW_REQUEST_MESSAGE);
                    userRequests.add(new UserRequest(update.getMessage().getChatId(), text));
                } catch (NullPointerException e) {
                    sendMessage(chatId, UNKNOWN_USER_MESSAGE);
                } finally {
                    break;
                }
            case "/help":
                sendMessage(chatId, HELP_MESSAGE);
                break;
            case "/cancel":
                sendMessage(chatId, CANCEL_MESSAGE);
                break;
            default:
                text = checkLastCommand(update);
                switch (text) {
                    case "/start":
                        CheckUserKey(update);
                        break;
                    case "/order": {
                        try {
                            Request request = createNewRequest(update);
                            User user = request.getUser();
                            sendMessageToGroup(REQUEST_MESSAGE + "\n" +
                                    "От: " + user.getInfo() + " \n" +
                                    "Расположение: " + user.getRegion() + " \n" +
                                    "Описание проблемы: \n" +
                                    request.getRequestMessage(), "admin");
                            sendMessage(chatId, OK_REQUEST_MESSAGE);
                        } catch (NullPointerException e) {
                            sendMessage(chatId, ERROR_MESSAGE);
                        }
                        break;
                    }
                    case "/cancel":
                        sendMessage(chatId, WAITING_MESSAGE);
                        break;
                    default:
                        sendMessage(chatId, ERROR_MESSAGE);
                        break;
                }

        }
    }

    private void sendMessageToGroup(String message, String group) {
        List<User> userList = userRepository.findUsersByGroup(group);
        try {
            for (User user : userList) {
                for (Chat chat : user.getChats()) {
                    sendMessage(chat.getChatId(), message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
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
        Long chatId = update.getMessage().getChatId();
        int idUser = -1;
        for (UserRequest userRequest : userRequests) {
            try {
                sendMessage(chatId, "Добро пожаловать, " + helloMessage(text));
                chatIdRepository.save(new Chat(update.getMessage().getChatId(), getUserByCode(text)));
                return;
            } catch (NullPointerException e) {
                if (userRequest.getChatId().equals(chatId)) {
                    idUser = userRequests.indexOf(userRequest);
                }
            }
        }
        try {
            UserRequest userRequest = userRequests.get(idUser);
            userRequest.addMessage(text);
            userRequest.setUserKey(text);
            sendMessage(chatId, "Добро пожаловать " + helloMessage(update.getMessage().getChatId()));
            chatIdRepository.save(new Chat(update.getMessage().getChatId(), getUserByCode(text)));
        } catch (NullPointerException e) {
            sendMessage(chatId, NO_KEY_MESSAGE);
            e.printStackTrace();
        } catch (TransactionalException e) {
            sendMessage(chatId, ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private Request createNewRequest(Update update) {
        if (update.getMessage().getText().startsWith(IF_COMMAND)) return null;
        else {
            Request request = new Request(update.getMessage().getText(), getUserByChat(update.getMessage().getChatId()));
            request.setDate(new Date().toString());
            request.setStatus(Status.active);
            requestRepository.save(request);
            return request;
        }
    }


    private String helloMessage(String key) {
        User user = userRepository.findUserByUserKey(key);
        user.setUserKey("");
        userRepository.save(user);
        return user.getInfo();
    }

    private String helloMessage(Long chatId) {
        User user = userRepository.findUserByChat(chatId);
        user.setUserKey("");
        userRepository.save(user);
        return user.getInfo();
    }

    private User getUserByCode(String code) {
        User user = userRepository.findUserByUserKey(code);
        user.setUserKey("");
        userRepository.save(user);
        return user;
    }

    private User getUserByChat(Long chatId) {
        User user = userRepository.findUserByChat(chatId);
        return user;
    }
}
