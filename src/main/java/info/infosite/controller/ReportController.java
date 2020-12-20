package info.infosite.controller;

import info.infosite.entities.auth.RoleRepository;
import info.infosite.entities.auth.UserRepository;
import info.infosite.functions.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportController {
    @Autowired
    MenuService menuService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/report")
    public String ShowReportPage(Model model) {
        menuService.CheckMenu();
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("groups",roleRepository.findAll());
        return "report";
    }

    @GetMapping(value = "/show/report/byuser")
    public String GetReportForUsername(Model model, @RequestParam String type, @RequestParam String from, @RequestParam String to, @RequestParam String username){
        return "report";
    }

    @GetMapping(value = "/show/report/bygroup")
    public String GetReportForGroup(Model model, @RequestParam String type, @RequestParam String from, @RequestParam String to, @RequestParam String group){
        return "report";
    }

}
