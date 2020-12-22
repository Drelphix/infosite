package info.infosite.entities.computer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Disk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String model;
    @Column
    private String serialNumber;
    @Column
    private  String size;
    @Column
    private String status;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public boolean like(Disk disk){
        return this.model.equals(disk.model) &
                this.serialNumber.equals(disk.serialNumber) &
                this.size.equals(disk.size) &
                this.status.equals(disk.status);
    }
}
