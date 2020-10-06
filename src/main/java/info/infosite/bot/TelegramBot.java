package info.infosite.bot;

import info.infosite.database.auth.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@EnableAsync
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    UserRepository userRepository;

    @SneakyThrows
    @Override
    @Async
    public void onUpdateReceived(Update update) {

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
