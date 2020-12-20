package info.infosite.entities.computer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
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
    private String bankLabel;

    @Column
    private String location;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public boolean like(Memory memory){
        if(this.capacity.equals(memory.capacity)&
        this.speed.equals(memory.speed)&
                this.bankLabel.equals(memory.bankLabel)&
                this.location.equals(memory.location)) return true; else return false;
    }
}
