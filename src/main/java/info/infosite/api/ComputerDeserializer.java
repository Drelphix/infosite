package info.infosite.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import info.infosite.entities.computer.*;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComputerDeserializer extends StdDeserializer<Computer> {


    public ComputerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Computer deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        Computer computer = new Computer();
        Cpu cpu = new Cpu();
        List<Memory> memory = new ArrayList<>();
        List<Disk> disks = new ArrayList<>();
        List<Network> networks = new ArrayList<>();
        OperationSystem os = new OperationSystem();
        String objectFieldName = "";
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();
            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                parser.nextToken();
                String value = parser.getValueAsString();
                if("memory".equals(fieldName)
                        ||"os".equals(fieldName)
                        ||"disks".equals(fieldName)
                        ||"cpu".equals(fieldName)
                        ||"networks".equals(fieldName)){
                    objectFieldName = fieldName;
                } else {
                    if(fieldName.equals("computer")){
                        computer.setName(value);
                    } else if(fieldName.equals("motherboard")){
                        computer.setMotherboard(value);
                    }
                   switch (objectFieldName){
                       case "memory":{
                            setMemory(value,fieldName,memory);break;
                       }
                       case "os":{
                           setOS(value,fieldName,os);break;
                       }
                       case "disks":{
                           setDisk(value,fieldName,disks);break;
                       }
                       case "cpu":{
                            setCpu(value,fieldName,cpu);break;
                       }
                       case "networks":{
                           setNetwork(value,fieldName,networks);break;
                       }
                       default: System.out.println("Unknown Element: "+ fieldName+" with value - "+value);
                       }
                   }
                }
            }
        computer.setCpu(cpu);
        computer.setMemory(memory);
        computer.setOs(os);
        computer.setDisks(disks);
        computer.setNetworks(networks);
        return computer;
    }
    private void setMemory(String value,String fieldName,List<Memory> memoryList) {
        int last = memoryList.size() - 1;
        switch (fieldName){
            case "capacity": {
                memoryList.add(new Memory());
                last = memoryList.size() - 1;
                memoryList.get(last).setCapacity(value);
                break;
            }
            case "speed": {
                memoryList.get(last).setSpeed(value);
                break;
            }
            case "serialNumber": {
                memoryList.get(last).setSerialNumber(value);
                break;
            }
            case "location": {
                memoryList.get(last).setLocation(value);
                break;
            }
            case "manufacturer": {
                memoryList.get(last).setManufacturer(value);
                break;
            }
            case "partNumber": {
                memoryList.get(last).setPartNumber(value);
            }
        }
    }
    private void setOS(String value,String fieldName,OperationSystem os){
        switch (fieldName){
            case "version":{
                os.setVersion(value);
                break;
            }
            case "architecture":{
                os.setArchitecture(value);
                break;
            }
            case "caption":{
                os.setCaption(value);
                break;
            }
            case "lastBootTime":{
                os.setLastBootTime(value);
                break;
            }
        }
    }
    private void setDisk(String value,String fieldName, List<Disk> disks){
        int last = disks.size() - 1;
        switch (fieldName){
            case "model":{
                disks.add(new Disk());
                last = disks.size() - 1;
                disks.get(last).setModel(value);
                break;
            }
            case "serialNumber":{
                disks.get(last).setSerialNumber(value);
                break;
            }
            case "size":{
                disks.get(last).setSize(value);
                break;
            }
            case "status":{
                disks.get(last).setStatus(value);
                break;
            }
        }
    }
    private void setCpu (String value,String fieldName,Cpu cpu){
        if("name".equals(fieldName)){
            cpu.setName(value);
        }
    }
    private void setNetwork(String value,String fieldName,List<Network> networks){
        int last = networks.size() - 1;
        switch (fieldName){
            case "description":{
                networks.add(new Network());
                last = networks.size() - 1;
                networks.get(last).setDescription(value);
                break;
            }
            case "ipAddress":{
                networks.get(last).setIpAddress(value);
                break;
            }
        }
    }
}