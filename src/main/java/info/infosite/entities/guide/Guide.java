package info.infosite.entities.guide;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "guides")
@Getter
@Setter
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(targetEntity = GuideMenu.class)
    private GuideMenu menu;
    @Column
    @NotNull
    private String title;

    @Lob
    @Column
    @NotNull
    private String text;

    @Column
    @NotNull
    private String date;

    @Column
    @NotNull
    private String username;
    @Column
    private String lastEditUsername;
    @Column
    private String lastEditDate;
    @Column
    @NotNull
    private String shortDescription;
}