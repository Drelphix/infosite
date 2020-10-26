package info.infosite.database;

import info.infosite.database.auth.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@Setter
public class Chat {
    @Id
    @Column
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    @NotNull
    private Long chatId;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "chats_id_login", nullable = false)
    private User user;

    public Chat() {
    }

    public Chat(Long chatId, User user) {
        this.user = user;
        this.chatId = chatId;
    }
}
