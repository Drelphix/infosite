package info.infosite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.infosite.entities.computer.Computer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiController {

    @PostMapping(value = "/api", consumes = "application/json", produces = "application/json")
    public String getComputerInfo(@RequestBody Map<String, Object> json) throws JsonProcessingException {
        Computer computer1 = new Computer();
        ObjectMapper objectMapper = new ObjectMapper();
        return null;
    }
}
