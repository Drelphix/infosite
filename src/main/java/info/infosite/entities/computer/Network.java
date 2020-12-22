package info.infosite.entities.computer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Network {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    @Column
    private String description;

    @Column
    private String ipAddress;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public boolean like(Network network){
        return this.description.equals(network.description) &
                this.ipAddress.equals(network.ipAddress);
    }
}
