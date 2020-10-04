package info.infosite.bot;

import info.infosite.database.auth.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@EnableAsync
public class TelegramBot extends TelegramLongPollingBot {
    private final static String UNAUTHORIZED_TEXT = "Извините, но я вас не знаю. Авторизуйтесь, пожалуйста. Используйте для этого команды: /login, /password";
    @Autowired
    UserRepository userRepository;

    @SneakyThrows
    @Override
    @Async
    public void onUpdateReceived(Update update) {
        List<String> chat = new ArrayList<>();
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText();
            chat.add(inputText);
            SendMessage message = new SendMessage();
            try {
                userRepository.findUserByChatId(update.getMessage().getChatId().toString());
                if (!chat.get(0).equals(UNAUTHORIZED_TEXT)) {
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.setChatId(update.getMessage().getChatId())
                        .setText(UNAUTHORIZED_TEXT);
                execute(message);
                if (update.getMessage().getText().equals("/login")) {
                    message.setChatId(update.getMessage().getChatId())
                            .setText("Пожалуйста, Введите логин");
                    execute(message);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "AutospaceAsistBot";
    }

    @Override
    public String getBotToken() {
        return "1308591163:AAFxENlXd6urBpEY4fj-VpoHfyJhVculU74";
    }
}
