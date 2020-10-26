package info.infosite.database.auth;

import info.infosite.database.Chat;
import info.infosite.database.Request;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id_login")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private boolean active;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinColumn
    private Role role;

    @Column(name = "fio")
    private String info;

    @Column
    String region;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Request.class)
    @JoinColumn
    private List<Request> requests;

    @Column
    private String userKey;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Chat.class)
    @JoinColumn
    private List<Chat> chats;

}