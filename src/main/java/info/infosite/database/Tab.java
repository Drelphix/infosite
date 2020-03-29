package info.infosite.database;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table (name = "tab")
public class Tab {

    @Id
    @Column(name = "idTable")
    private int idTable;

    @Column(name="name")
    private String name;

    @ManyToOne(targetEntity = SubMenu.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "SubMenu")
    private SubMenu subMenu;

    @OneToMany(fetch = FetchType.EAGER,targetEntity = Col.class,mappedBy = "table")
    private Set<Col> cols;

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    public Set<Col> getCols() {
        return cols;
    }

    public void setCols(Set<Col> cols) {
        this.cols = cols;
    }
}
