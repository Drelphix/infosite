package info.infosite.entities.computer;

import lombok.Data;

@Data
public class OperationSystem {
    String caption;
    String version;
    String architecture;
    String lastBootTime;

    public OperationSystem() {
    }

    public OperationSystem(String caption, String version, String architecture, String lastBootTime) {
        this.caption = caption;
        this.version = version;
        this.architecture = architecture;
        this.lastBootTime = lastBootTime;
    }
}
