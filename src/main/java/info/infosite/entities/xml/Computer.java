package info.infosite.entities.xml;

import lombok.Data;

import java.util.List;

@Data
public class Computer {
    private String name;
    private String time;
    private String date;
    private List<Disk> disks;
    private List<Backup> backups;

    public void AddDisk(Disk disk) {
        this.disks.add(disk);
    }

    public void AddBackup(Backup backup) {
        this.backups.add(backup);
    }

}
