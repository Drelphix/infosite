package info.infosite.controller;

import info.infosite.entities.request.Request;
import info.infosite.entities.request.RequestRepository;
import info.infosite.entities.request.Status;
import info.infosite.functions.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@SessionAttributes("mode")
public class OrderController {
    @Autowired
    MenuService menuService;
    @Autowired
    public RequestRepository requestRepository;


    @GetMapping(value = "/order")
    public String ShowOrder(Model model, @RequestParam String show, HttpSession httpSession) {
        menuService.CheckMenu();
        model = menuService.addMenu(model, httpSession);
        try {

            if (show.equals("all")) {
                model.addAttribute("orders", requestRepository.findAll());
                model.addAttribute("selStatus", "");
                model.addAttribute("status", "");
                return "requests";
            } else {
                if (show.equals("active")) {
                    model.addAttribute("orders", requestRepository.findAllByStatus(Status.active));
                    model.addAttribute("selStatus", "");
                    model.addAttribute("status", "");
                    return "requests";
                } else {
                    if (show.equals("inwork")) {
                        model.addAttribute("orders", requestRepository.findAllByStatus(Status.inwork));
                        model.addAttribute("selStatus", "");
                        model.addAttribute("status", "");
                    } else {
                        model.addAttribute("orders", requestRepository.findAllByStatus(Status.completed));
                        model.addAttribute("selStatus", "");
                        model.addAttribute("status", "");
                    }
                }
            }
        } catch (Exception e) {
            model.addAttribute("orders", requestRepository.findAll());
            model.addAttribute("status", "");
            model.addAttribute("selStatus", "");
            return "requests";
        }
        return "requests";
    }
    @PostMapping("/order/{id}")
    private String EditRequestStatus(Model model, @PathVariable int id,@Valid String status){
        Request request = requestRepository.getOne(id);
        if(status.equals("active")){
            request.setStatus(Status.active);
        } else  if(status.equals("inwork")){
            request.setStatus(Status.inwork);
        } else if(status.equals("completed")){
            request.setStatus(Status.completed);
            requestRepository.save(request);
            return "redirect:/order?show=all";
        } else if(status.equals("")){
            status="all";
        }
        requestRepository.save(request);
        return "redirect:/order?show="+status;
    }


}
