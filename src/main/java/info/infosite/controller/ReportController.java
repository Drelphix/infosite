package info.infosite.controller;

import info.infosite.entities.auth.RoleRepository;
import info.infosite.entities.auth.UserRepository;
import info.infosite.entities.request.Request;
import info.infosite.entities.request.RequestRepository;
import info.infosite.entities.request.Status;
import info.infosite.functions.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReportController {
    @Autowired
    MenuService menuService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    @GetMapping(value = "/report")
    public String ShowReportPage(Model model) {
        menuService.CheckMenu();
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("startDate","");
        model.addAttribute("endDate","");
        return "report";
    }
    @PostMapping(value = "/report")
    public String LoadReportPage(Model model, @Valid String status,@Valid String user,@Valid String endDate, @Valid String startDate) {
        if(endDate.equals("")) endDate= LocalDate.now().toString();
        if(status.equals("")) status="all";
        List<Request> requests = new ArrayList<>();
            if (status.equals("all") && user.equals("") && !startDate.equals(""))
                requests = requestRepository.findAllBetweenDates(LocalDateTime.parse(startDate + "T00:00:00.0"), LocalDateTime.parse(endDate + "T00:00:00.0"));
            else if (!status.equals("all") & user.equals("") & startDate.equals(""))
                requests = requestRepository.findAllByStatus(Status.fromString(status));
            else if (status.equals("all") & !user.equals("") & startDate.equals(""))
                requests = requestRepository.findAllByUser(userRepository.findUserByUsername(user));
            else if (status.equals("all") & user.equals("") & startDate.equals(""))
                requests = requestRepository.findAll();
            else if (!status.equals("all") & !user.equals("")  & startDate.equals(""))
                requests = requestRepository.findAllByUserAndStatus(userRepository.findUserByUsername(user), Status.fromString(status));
            else if(!status.equals("all") & user.equals(""))
                requests = requestRepository.findAllByStatusBetweenDates
                        (Status.fromString(status),LocalDateTime.parse(startDate + "T00:00:00.0"), LocalDateTime.parse(endDate + "T00:00:00.0") );
            else requests = requestRepository.findAllByUserBetweenDates
                        (userRepository.findUserByUsername(user), LocalDateTime.parse(startDate + "T00:00:00.0"), LocalDateTime.parse(endDate + "T00:00:00.0") );
        menuService.CheckMenu();
        model.addAttribute("orders",requests);
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("startDate","");
        model.addAttribute("endDate","");
        return "report";
    }


}
