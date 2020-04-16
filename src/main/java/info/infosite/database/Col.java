package info.infosite.database;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "col")
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

    public int getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(int idColumn) {
        this.idColumn = idColumn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tab getTable() {
        return table;
    }

    public void setTable(Tab table) {
        this.table = table;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
