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

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Col.class, mappedBy = "table", cascade = CascadeType.ALL)
    private List<Col> cols;

    public Tab() {
    }

    public Tab(SubMenu subMenu) {
        this.subMenu = subMenu;
    }
}
