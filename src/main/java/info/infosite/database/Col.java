package info.infosite.database;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "col")
@Getter
@Setter
public class Col {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idColumn")
    private int idColumn;

    @Column(name = "name")
    private String name;

    @ManyToOne(targetEntity = Tab.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "idTable")
    private Tab table;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Line.class, mappedBy = "col", cascade = CascadeType.REMOVE)
    private List<Line> lines;

    public Col() {
    }

    public Col(Tab table) {
        this.table = table;
    }

}
