package info.infosite.database;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subMenu")
@Data
public class SubMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSubMenu")
    private int idSubMenu;

    @Column(name = "name")
    private String name;

    @ManyToOne(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "idMenu")
    private Menu menu;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Tab.class, mappedBy = "subMenu", cascade = CascadeType.REMOVE)
    private List<Tab> tables;

    public SubMenu() {
    }

    public SubMenu(Menu menu) {
        this.menu = menu;
    }

}