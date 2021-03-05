package info.infosite.entities.computer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Entity
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

    @Column
    private String ipAddress;

    @Column
    private String macAddress;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public boolean like(Network network) {
        return this.description.equals(network.description) &
                this.ipAddress.equals(network.ipAddress);
    }

    public List<String> getIpAddresses() {
    List<String> nothing = new ArrayList<>();
    nothing.add("Ip-адреса отсутствуют");
        try {
            return Arrays.asList(ipAddress.split(","));
        } catch (NullPointerException e){ return nothing;

        }
    }

}
