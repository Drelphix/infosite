package info.infosite.bot;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {
    public String userKey;
    public Long chatId;
    public List<String> chat;

    public UserRequest(Long chatId, String message) {
        this.chatId = chatId;
        this.chat = new ArrayList<>();
        this.addMessage(message);
    }

    public UserRequest() {
    }

    public void addMessage(String message) {
        this.getChat().add(message);
    }
}
