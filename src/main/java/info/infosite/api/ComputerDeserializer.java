package info.infosite.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import info.infosite.entities.computer.*;

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
        List<Cpu> cpu = new ArrayList<>();
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
        switch (fieldName){
            case "capacity":{
                memoryList.add(new Memory());
                int last=memoryList.size()-1;
                memoryList.get(last).setCapacity(value);
                break;
            }
            case "speed":{
                memoryList.get(memoryList.size()-1).setSpeed(value);
                break;
            }
            case "bankLabel":{
                memoryList.get(memoryList.size()-1).setBankLabel(value);
                break;
            }
            case "location":{
                memoryList.get(memoryList.size()-1).setLocation(value);
                break;
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
        switch (fieldName){
            case "model":{
                disks.add(new Disk());
                int last=disks.size()-1;
                disks.get(last).setModel(value);
                break;
            }
            case "serialNumber":{
                disks.get(disks.size()-1).setSerialNumber(value);
                break;
            }
            case "size":{
                disks.get(disks.size()-1).setSize(value);
                break;
            }
            case "status":{
                disks.get(disks.size()-1).setStatus(value);
                break;
            }
        }
    }
    private void setCpu (String value,String fieldName,List<Cpu> cpu){
        if("name".equals(fieldName)){
            cpu.add(new Cpu());
            int last=cpu.size()-1;
            cpu.get(last).setName(value);
        }
    }
    private void setNetwork(String value,String fieldName,List<Network> networks){
        switch (fieldName){
            case "description":{
                networks.add(new Network());
                int last=networks.size()-1;
                networks.get(last).setDescription(value);
                break;
            }
            case "ipAddress":{
                networks.get(networks.size()-1).setIpAddress(value);
                break;
            }
        }
    }
}