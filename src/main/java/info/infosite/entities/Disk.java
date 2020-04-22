package info.infosite.entities;

import lombok.Data;

@Data
public class Disk {
    private String letter;
    private String name;
    private String totalSize;
    private String freeSpace;
}
