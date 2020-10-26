package info.infosite.database;

import info.infosite.database.auth.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column
    private String Status;
    @Column
    private String requestMessage;

    public Request() {
    }

    public Request(String requestMessage, User user) {
        this.requestMessage = requestMessage;
        this.user = user;
    }


}
