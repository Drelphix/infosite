package info.infosite.entities.computer;

import info.infosite.entities.gentable.Tab;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Cpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public boolean like(Cpu cpu){
        if(this.name.equals(cpu.getName())) return true; else return false;
    }


}
