package info.infosite.entities.computer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
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

    @OneToMany(targetEntity = Cpu.class,mappedBy = "computer",cascade = CascadeType.ALL)
    private List<Cpu> cpu;

    @OneToOne(targetEntity = OperationSystem.class,mappedBy = "computer",cascade = CascadeType.ALL)
    private OperationSystem os;

    @OneToMany(targetEntity = Memory.class,mappedBy = "computer",cascade = CascadeType.ALL)
    private List<Memory> memory;

    @OneToMany(targetEntity = Disk.class,mappedBy = "computer",cascade = CascadeType.ALL)
    private List<Disk> disks;

    @OneToMany(targetEntity = Network.class,mappedBy = "computer",cascade = CascadeType.ALL)
    private List<Network> networks;

    @Column
    private LocalDateTime date;

    public boolean like(Computer computer){
        if(!this.name.equals(computer.getName())||!this.motherboard.equals(computer.getMotherboard())){
           return false;
        }
            if (!this.os.like(computer.getOs())) return false;
            for(int i=0;i<cpu.size();i++){
                if(!this.cpu.get(i).like(computer.getCpu().get(i))) return true;
            }
            for (int i = 0; i < memory.size(); i++) {
                if(!this.memory.get(i).like(computer.getMemory().get(i))) return false;
            }
            for (int i = 0; i < disks.size(); i++) {
                if(!this.disks.get(i).like(computer.disks.get(i)))  return false;
            }
            for (int i = 0; i < networks.size(); i++) {
                if(!this.networks.get(i).like(computer.networks.get(i))) return false;
            }
        return true;
    }

    public String getSummaryMemory() {
        int summaryMemory=0;
        for (Memory memory:this.memory){
            summaryMemory+=Integer.parseInt(memory.getCapacity());
        }
        return summaryMemory+" Gb";
    }
    public String getCpuInfo(){
        String cpuInfo="";
        for (Cpu cpu:this.cpu){
            cpuInfo+=cpu.getName()+"; ";
        }
        return cpuInfo;
    }
}
