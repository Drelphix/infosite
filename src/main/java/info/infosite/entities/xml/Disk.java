package info.infosite.entities.xml;

import lombok.Data;

@Data
public class Disk {
    private String letter;
    private String name;
    private String totalSize;
    private String freeSpace;
    private double freeSpaceGb;
}
