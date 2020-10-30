package info.infosite.entities.views;

import info.infosite.entities.xml.Backup;
import info.infosite.entities.xml.Computer;
import info.infosite.entities.xml.Disk;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ComputerView {
    private List<Disk> disks;
    private List<Backup> freshBackups;
    private List<Backup> anotherBackups;

    public ComputerView(Computer computer) {
        disks.addAll(disks);
    }
}
