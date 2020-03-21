package info.infosite.database;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "changes")
public class Change {
    @Id
    @Column (name = "id_change")
    private int idChanges;

    @Column (name = "changes")
    @NotNull
    private String change;

    @Column (name = "description")
    @NotNull
    private String description;

    @Column (name = "time")
    @NotNull
    private Date time;

    @Column (name ="device")
    @NotNull
    private String device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idAddress")
    private Address address;

    public Change(){

    }

    public int getIdChanges() {
        return idChanges;
    }

    public void setIdChanges(int idChanges) {
        this.idChanges = idChanges;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
