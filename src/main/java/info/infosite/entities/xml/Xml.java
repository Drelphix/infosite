package info.infosite.entities.xml;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "xml")
@Getter
@Setter
public class Xml {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long idXml;

    @Column(name = "path")
    private String path;

    @Column(name = "menu")
    private String menuName;

}
