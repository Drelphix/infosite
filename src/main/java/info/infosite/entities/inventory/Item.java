package info.infosite.entities.inventory;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Setter
@Getter
public class Item {
    @Id
    @Column
    private long id;

    @Column
    private String name;

    @Column
    private String date;


}
