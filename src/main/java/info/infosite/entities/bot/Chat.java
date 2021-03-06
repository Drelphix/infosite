package info.infosite.entities.bot;

import info.infosite.entities.auth.User;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

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
    @Unique
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
