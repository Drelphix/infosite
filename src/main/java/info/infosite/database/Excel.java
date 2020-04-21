package info.infosite.database;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "excel")
@Data
public class Excel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "path")
    private String path;

}
