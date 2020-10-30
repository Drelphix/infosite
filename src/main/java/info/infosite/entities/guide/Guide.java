package info.infosite.entities.guide;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "guides")
@Getter
@Setter
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;

    @Lob
    @Column
    private String text;

    @Column
    private String date;

    @Column
    private String username;

    @Column
    private String lastEditUsername;
    @Column
    private String lastEditDate;
}