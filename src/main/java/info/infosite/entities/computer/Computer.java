package info.infosite.entities.computer;

import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Computer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull
    @Unique
    private String name;

    @Column
    private String motherboard;

    @Column
    private String cpu;

    @OneToOne(targetEntity = OperationSystem.class, mappedBy = "computer", cascade = CascadeType.ALL)
    private OperationSystem os;

    @OneToMany(targetEntity = Memory.class, mappedBy = "computer", cascade = CascadeType.ALL)
    private List<Memory> memory;

    @OneToMany(targetEntity = Disk.class, mappedBy = "computer", cascade = CascadeType.ALL)
    private List<Disk> disks;

    @OneToMany(targetEntity = Network.class, mappedBy = "computer", cascade = CascadeType.ALL)
    private List<Network> networks;

    @Column
    private LocalDateTime date;

    public String getSummaryMemory() {
        long summaryMemory = 0;
        for (Memory memory : this.memory) {
            summaryMemory += Long.parseLong(memory.getCapacity());
        }
        return summaryMemory + " Gb";
    }
}
