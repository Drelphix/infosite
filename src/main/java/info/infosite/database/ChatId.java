package info.infosite.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class ChatId {
    @Id
    @Column
    private int id;

    @Column
    private Long chatId;

    public ChatId() {
    }

    public ChatId(Long chatId) {
        this.chatId = chatId;
    }
}
