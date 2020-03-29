package info.infosite.database;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "line")
public class Line {
    @Id
    @Column (name = "idLine")
    private int idLine;

    @Column (name = "time")
    private Date time;

    @Column(name = "data")
    private String data;

    @ManyToOne()
    @JoinColumn(name = "idColumn")
    private Col col;

    public int getIdLine() {
        return idLine;
    }

    public void setIdLine(int idLine) {
        this.idLine = idLine;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
