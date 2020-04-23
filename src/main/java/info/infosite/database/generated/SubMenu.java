package info.infosite.database.generated;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subMenu")
@Getter
@Setter
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

    public void AddNewTable(Tab table) {
        this.tables.add(table);
    }

}