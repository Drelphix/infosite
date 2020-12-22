package info.infosite.entities.computer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Memory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String capacity;

    @Column
    private String speed;

    @Column
    private String serialNumber;

    @Column
    private String partNumber;

    @Column
    private String manufacturer;

    @Column
    private String location;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public boolean like(Memory memory) {
        return this.capacity.equals(memory.capacity) &
                this.speed.equals(memory.speed) &
                this.serialNumber.equals(memory.serialNumber) &
                this.location.equals(memory.location) &
                this.manufacturer.equals(memory.manufacturer) &
                this.partNumber.equals(memory.partNumber);
    }
}
