package info.infosite.database;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMenu")
    private int idMenu;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER,targetEntity = SubMenu.class,mappedBy = "menu")
    private Set<SubMenu> subMenuSet;


    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SubMenu> getSubMenuSet() {
        return subMenuSet;
    }

    public void setSubMenuSet(Set<SubMenu> subMenuSet) {
        this.subMenuSet = subMenuSet;
    }
}
