package info.infosite.database;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table (name = "regions")
public class Region {
    @Id
    @Column(name = "id_region")
    @NotNull
    private int idRegion;

    @Column(name = "region")
    @NotNull
    private String region;

    @OneToMany(targetEntity = Address.class,fetch = FetchType.LAZY,mappedBy = "region")
    private Set<Address> addresses;

    public Region(){

    }
    public Region(String region) {
        this.region = region;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

}
