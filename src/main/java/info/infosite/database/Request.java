package info.infosite.database;

import info.infosite.database.auth.User;

import javax.persistence.*;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    private int id;

    @ManyToOne(targetEntity = User.class)
    private User userKey;

    @Column
    private String requestMessage;


}
