package info.infosite.database.generated;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "line")
@Getter
@Setter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLine")
    private int idLine;

    @Column(name = "data")
    private String data;

    private boolean hidden = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idColumn")
    private Col col;

    public Line() {
    }

    public Line(Col col) {
        this.setData("");
        this.col = col;
    }
}
