package info.infosite.entities;

import lombok.Data;

import java.util.List;

@Data
public class Backup {
    private String path;
    private List<File> files;

    public void AddFile(File file) {
        this.files.add(file);
    }
}
