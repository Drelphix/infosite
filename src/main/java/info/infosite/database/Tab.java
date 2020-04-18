package info.infosite.database;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "tab")
public class Tab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTable")
    private int idTable;

    @Column(name = "name")
    private String name;

    @ManyToOne(targetEntity = SubMenu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "SubMenu")
    private SubMenu subMenu;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Col.class, mappedBy = "table", cascade = CascadeType.REMOVE)
    private List<Col> cols;

    public Tab() {
    }

    public Tab(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    public Tab(int idTable, String name, SubMenu subMenu, List<Col> cols) {
        this.idTable = idTable;
        this.name = name;
        this.subMenu = subMenu;
        this.cols = cols;
        SortCols();
    }

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

    public List<Col> getCols() {
        return cols;
    }

    public void setCols(List<Col> cols) {
        this.cols = cols;
    }


    public void SortCols() {
        Col temp = null;
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < cols.size() - 1; i++) {
                if (cols.get(i).getIdColumn() > cols.get(i + 1).getIdColumn()) {
                    isSorted = false;
                    temp = cols.get(i);
                    cols.set(i, cols.get(i + 1));
                    cols.set(i + 1, temp);
                }
            }
        }
    }
}
