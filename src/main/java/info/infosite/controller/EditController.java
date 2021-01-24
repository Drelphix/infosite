package info.infosite.controller;

import info.infosite.entities.auth.RoleRepository;
import info.infosite.entities.auth.User;
import info.infosite.entities.auth.UserRepository;
import info.infosite.entities.gentable.*;
import info.infosite.entities.guide.Guide;
import info.infosite.entities.guide.GuideMenu;
import info.infosite.entities.guide.GuideMenuRepository;
import info.infosite.entities.guide.GuideRepository;
import info.infosite.entities.views.ListLineView;
import info.infosite.entities.views.TableView;
import info.infosite.functions.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EditController {
    @Autowired
    public ColRepository colRepository;
    @Autowired
    public LineRepository lineRepository;
    @Autowired
    public MenuRepository menuRepository;
    @Autowired
    public SubMenuRepository subMenuRepository;
    @Autowired
    public TableRepository tableRepository;
    @Autowired
    public GuideRepository guideRepository;
    @Autowired
    public RoleRepository roleRepository;
    @Autowired
    public GuideMenuRepository guideMenuRepository;
    @Autowired
    MenuService menuService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String EditLines(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        menuService.CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
        lines.setIdSubMenu(tableView.getSubMenu().getIdSubMenu());
        lines.setIdTable(tableView.getId());
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        model.addAttribute("tableName", tableView.getName());
        model.addAttribute("cols", tableView.getCols());
        model.addAttribute("lines", lines);
        return "add";
    }

    @RequestMapping(value = "/line/edit", method = RequestMethod.POST)
    public String SaveLines(Model model, @ModelAttribute ListLineView table) {
        for (Line line : table.getLines()) {
            lineRepository.save(line);
        }
        return "redirect:/show?id=" + table.getIdSubMenu();
    }

    @RequestMapping(value = "/menu/edit", method = RequestMethod.GET)
    public String EditMenu(Model model, @RequestParam(name = "id") int idMenu) {
        menuService.CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        model.addAttribute("xmls", menuService.xmlMenus);
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/menu/edit", method = RequestMethod.POST)
    public String SaveMenu(Model model, @ModelAttribute Menu menu) {
        try {
            for (SubMenu subMenu : menu.getSubMenuSet()) {
                subMenuRepository.save(subMenu);
            }
            menuRepository.save(menu);
        } catch (NullPointerException ignored) {

        }
        menuService.menus = menuRepository.findAll();
        return "redirect:/";
    }


    @RequestMapping(value = "/table/edit", method = RequestMethod.GET)
    public String EditTable(Model model, @RequestParam(name = "tab") int idTab) {
        menuService.CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        model.addAttribute("table", tableView);
        model.addAttribute("xmls", menuService.xmlMenus);
        model.addAttribute("menus", menuService.menus);
        return "add";
    }


    @RequestMapping(value = "/table/edit", method = RequestMethod.POST)
    public String SaveTable(Model model, @ModelAttribute(name = "table") TableView table) {
        Tab tab = tableRepository.getOne(table.getId());
        int max = 0;
        tab.setName(table.getName());
        tab.setSubMenu(table.getSubMenu());
        try {
            for (Col col : table.getCols()) {
                if (!col.getName().equals("")) {
                    colRepository.save(col);
                    if (col.getLines().size() == 0) {
                        for (int i = 0; i < max; i++) {
                            lineRepository.save(new Line(col));
                        }
                    } else {
                        if (max < col.getLines().size())
                            max = col.getLines().size();
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
        tableRepository.save(tab);

        return "redirect:/show?id=" + table.getSubMenu().getIdSubMenu();
    }

    @RequestMapping(value = "/user/edit")
    public String EditUser(Model model, @RequestParam int id) {
        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        List<String> roles =new ArrayList<>();
        model.addAttribute("roles",menuService.getRolesByRole(roles));
        model.addAttribute("edit", true);
        return "registration";
    }

    @PostMapping(value = "/user/edit")
    public String EditUser(Model model, @ModelAttribute User user,@Valid String role) {
        User base = userRepository.findUserByUsername(user.getUsername());
        if (user.getPassword().equals("")) {
            user.setPassword(base.getPassword());

        } else user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(!role.equals("1,")){
            user.setRole(roleRepository.findRoleByRole(role.substring(2)));
            System.out.println(user.getRole());
        }
        user.setChats(base.getChats());
        userRepository.save(user);
        return "redirect:/management";
    }

    @RequestMapping(value = "/guide/edit")
    public String EditGuide(Model model, @RequestParam int id) {
        Guide guide = guideRepository.getOne(id);
        model.addAttribute("currentGuide",guide);
        model.addAttribute("guide", guide);
        model.addAttribute("edit", true);
        return "addguide";
    }

    @PostMapping(value = "/guide/edit")
    public String EditGuide(Model model, @ModelAttribute Guide guide) {
        guide.setLastEditDate(LocalDate.now().toString());
        guide.setLastEditUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        guideRepository.save(guide);
        return "redirect:/guides";
    }

    @RequestMapping(value = "/guideMenu/edit", method = RequestMethod.GET)
    public String EditGuideMenu(Model model, HttpSession httpSession, @RequestParam int id) {
        model.addAttribute("newGuideMenu", guideMenuRepository.getOne(id));
        menuService.CheckMenu();
        model = menuService.addMenu(model, httpSession);
        model.addAttribute("guideMenu", guideMenuRepository.findAll());
        model.addAttribute("editGuideMenu", "");
        return "guides";
    }

    @RequestMapping(value = "/guideMenu/rename", method = RequestMethod.POST)
    public String SaveNewGuideMenu(Model model, @ModelAttribute GuideMenu newGuideMenu) {
        try {
            GuideMenu guideMenu = guideMenuRepository.getOne(newGuideMenu.getId());
            guideMenu.setName(newGuideMenu.getName());
            guideMenuRepository.save(guideMenu);
            return "redirect:/guides";
        } catch (NullPointerException e) {
            model.addAttribute("error", "Произошла ошибка сохранения");
        }
        return "guides";
    }

}
