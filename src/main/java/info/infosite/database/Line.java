package info.infosite.database;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "line")
@Data
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLine")
    private int idLine;

    @Column(name = "data")
    private String data;

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
