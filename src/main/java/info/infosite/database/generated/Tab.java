package info.infosite.database.generated;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tab")
@Getter
@Setter
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
