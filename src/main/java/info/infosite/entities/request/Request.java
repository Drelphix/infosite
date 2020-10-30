package info.infosite.entities.request;

import info.infosite.entities.auth.User;
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

    private Status status;

    @Column
    private String requestMessage;

    @Column
    private String date;

    public Request() {
    }

    public Request(String requestMessage, User user) {
        this.requestMessage = requestMessage;
        this.user = user;
    }


}
