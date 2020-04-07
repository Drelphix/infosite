package info.infosite.database;

import javax.persistence.*;

@Entity
@Table(name = "line")
public class Line {
    @Id
    @Column (name = "idLine")
    private int idLine;

    @Column(name = "data")
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idColumn")
    private Col col;

    public int getIdLine() {
        return idLine;
    }

    public void setIdLine(int idLine) {
        this.idLine = idLine;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Col getCol() {
        return col;
    }

    public void setCol(Col col) {
        this.col = col;
    }
}
