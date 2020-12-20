package info.infosite.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import info.infosite.api.ComputerDeserializer;
import info.infosite.entities.computer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
public class ApiController {
    @Autowired
    ComputerRepository computerRepository;
    @Autowired
    OSRepository osRepository;

    @PostMapping(value = "/api", consumes = "application/json", produces = "application/json")
    public void getComputerInfo(@RequestBody HashMap<String, Object> jsonMap) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String json = new ObjectMapper().writeValueAsString(jsonMap);
        SimpleModule module =
                new SimpleModule("ComputerDeserializer", new Version(3, 1, 8, null, null, null));
        module.addDeserializer(Computer.class, new ComputerDeserializer(Computer.class));
        objectMapper.registerModule(module);
        Computer computer = objectMapper.readValue(json,Computer.class);
        try {
            Computer temp = computerRepository.findByName(computer.getName());
            if(!computer.like(temp)){
            computerRepository.deleteById(temp.getId());
            saveComputer(computer);}
        } catch (NullPointerException|IllegalArgumentException e){
            saveComputer(computer);
        }
    }

    private void saveComputer(Computer computer){
        computer.setDate(LocalDateTime.now());
        for(Cpu cpu:computer.getCpu()){
            cpu.setComputer(computer);
        }
        for(Disk disk:computer.getDisks()){
            disk.setComputer(computer);
        }
        for(Memory memory:computer.getMemory()){
            memory.setComputer(computer);
        }
        for(Network network:computer.getNetworks()){
            network.setComputer(computer);
        }
        computer.getOs().setComputer(computer);
        computerRepository.save(computer);
    }
}
