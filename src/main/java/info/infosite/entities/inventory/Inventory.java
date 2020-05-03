package info.infosite.entities.inventory;

import javax.persistence.*;

@Entity
@Table
public class Inventory {
    @Id
    @Column
    private long id;

    @OneToOne
    private Item item;

    @Column
    private int count;

    @Column
    private String time;

    @Column
    private String user;

    @OneToOne
    private Place place;
}
