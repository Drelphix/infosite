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

    public boolean like(OperationSystem os){
        return this.architecture.equals(os.architecture) &
                this.caption.equals(os.caption) &
                this.version.equals(os.version) &
                this.lastBootTime.equals(os.lastBootTime);
    }

}
