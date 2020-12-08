package info.infosite.entities.computer;

import lombok.Data;

import java.util.List;

@Data
public class Computer {
    String name;
    String motherboard;
    List<String> cpu;
    OperationSystem os;
    List<Memory> memory;
    List<Disk> disks;
    List<Network> networks;
}
