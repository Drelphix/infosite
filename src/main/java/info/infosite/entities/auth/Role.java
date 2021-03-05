package info.infosite.entities.auth;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String role;

    public Role(String role) {
        this.role = role;
    }
    public Role() {
    }
}
