package info.infosite.database;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMenu")
    private int idMenu;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = SubMenu.class, mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<SubMenu> subMenuSet;

    public void AddSubMenu(SubMenu subMenu) {
        this.subMenuSet.add(subMenu);
    }

    public void setSubMenuSet(List<SubMenu> subMenuSet) {
        this.subMenuSet = subMenuSet;
        SortSubMenu();
    }

    public void SortSubMenu() {
        SubMenu temp = null;
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < subMenuSet.size() - 1; i++) {
                if (subMenuSet.get(i).getIdSubMenu() > subMenuSet.get(i + 1).getIdSubMenu()) {
                    isSorted = false;
                    temp = subMenuSet.get(i);
                    subMenuSet.set(i, subMenuSet.get(i + 1));
                    subMenuSet.set(i + 1, temp);
                }
            }
        }

    }
}
