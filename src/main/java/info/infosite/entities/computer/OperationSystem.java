package info.infosite.entities.computer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Setter
@Getter
public class OperationSystem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @Column
    private String caption;
    @Column
    private String version;
    @Column
    private String architecture;
    @Column
    private String lastBootTime;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(nullable = false)
    private Computer computer;

    public String getShrinkCaption() {
        String[] caption = this.caption.split(" ");
        return this.caption.contains("Windows Server") ? caption[1] + " " + caption[2] + " " + caption[3] : caption[1] + " " + caption[2];
    }
}
