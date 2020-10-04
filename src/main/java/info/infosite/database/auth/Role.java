package info.infosite.database.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Role {
    @Id
    @Column
    private int id;

    @Column
    private String role;
}
