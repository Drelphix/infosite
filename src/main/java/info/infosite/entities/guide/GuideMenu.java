package info.infosite.entities.guide;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "guideMenu")
@Getter
@Setter
public class GuideMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @OneToMany(targetEntity = Guide.class, mappedBy = "menu")
    private List<Guide> guides;

    public GuideMenu(String name) {
        this.name = name;
    }

    public GuideMenu() {
    }
}
