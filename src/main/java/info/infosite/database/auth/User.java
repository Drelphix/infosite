package info.infosite.database.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Column
    private String fio;

    @Column
    private String chatId;

}