package info.infosite.database;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @Column(name = "id_address")
    private int idAddress;

    @Column(name = "address")
    @NotNull
    private String address;

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name="idRegion")
    private Region region;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "address",targetEntity = Password.class)
    private Set<Password> passwords;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "address",targetEntity = Change.class)
    private Set<Change> changes;

    public Address() {
    }

    public Address(String address, Region region) {
        this.address = address;
        this.region = region;
    }

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Set<Password> getPasswords() {
        return passwords;
    }

    public void setPasswords(Set<Password> passwords) {
        this.passwords = passwords;
    }

    public Set<Change> getChanges() {
        return changes;
    }

    public void setChanges(Set<Change> changes) {
        this.changes = changes;
    }
}