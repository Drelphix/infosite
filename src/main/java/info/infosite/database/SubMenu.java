package info.infosite.database;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "subMenu")
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
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Tab.class, mappedBy = "subMenu", cascade = CascadeType.ALL)
    private List<Tab> tables;

    public SubMenu() {
    }

    public SubMenu(Menu menu) {
        this.menu = menu;
    }

    public int getIdSubMenu() {
        return idSubMenu;
    }

    public void setIdSubMenu(int idSubMenu) {
        this.idSubMenu = idSubMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Tab> getTables() {
        return tables;
    }

    public void setTables(List<Tab> tables) {
        this.tables = tables;
    }
}