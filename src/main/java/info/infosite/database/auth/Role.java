package info.infosite.database.auth;

import javax.persistence.*;

@Entity
@Table
public class Role {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String role;

}
