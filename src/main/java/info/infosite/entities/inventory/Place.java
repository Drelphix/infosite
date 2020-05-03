package info.infosite.entities.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Place {
    @Id
    @Column
    private long id;

    @Column
    private String name;
}
