package info.infosite.controller;

import info.infosite.entities.auth.RoleRepository;
import info.infosite.entities.auth.User;
import info.infosite.entities.auth.UserRepository;
import info.infosite.functions.PassGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping(value = "/registration")
    public String Registration(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("repeat", "");
        return "registration";
    }

    @PostMapping(value = "/registration")
    public String Registration(Model model, @ModelAttribute User user, @ModelAttribute String repeat) {
        try {
            User test = userRepository.findUserByUsername(user.getUsername());
            test.toString();
            model.addAttribute("error", "Имя пользователя занято");
            return "registration";
        } catch (NullPointerException e) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(false);
            userRepository.save(user);
        }
        return "redirect:/management";
    }
    @GetMapping(value = "/user/generate")
    public String generateUserKey(Model model, @RequestParam int id){
        User user = userRepository.getOne(id);
        user.setUserKey(new PassGenerator().generate());
        userRepository.save(user);
        return "redirect:/management";
    }
}
