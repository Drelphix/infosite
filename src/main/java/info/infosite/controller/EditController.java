package info.infosite.controller;

import info.infosite.database.auth.User;
import info.infosite.database.auth.UserRepository;
import info.infosite.database.generated.*;
import info.infosite.functions.MenuService;
import info.infosite.views.ListLineView;
import info.infosite.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String SaveLines(Model model, @ModelAttribute ListLineView table) {
        for (Line line : table.getLines()) {
            lineRepository.save(line);
        }
        return "redirect:/show?id=" + table.getIdSubMenu();
    }

    @RequestMapping(value = "/editMenu", method = RequestMethod.GET)
    public String EditMenu(Model model, @RequestParam(name = "id") int idMenu) {
        menuService.CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        model.addAttribute("xmls", menuService.xmlMenus);
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/editMenu", method = RequestMethod.POST)
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


    @RequestMapping(value = "/editTab", method = RequestMethod.GET)
    public String EditTable(Model model, @RequestParam(name = "tab") int idTab) {
        menuService.CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        model.addAttribute("table", tableView);
        model.addAttribute("xmls", menuService.xmlMenus);
        model.addAttribute("menus", menuService.menus);
        return "add";
    }


    @RequestMapping(value = "/editTab", method = RequestMethod.POST)
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

    @RequestMapping(value = "/edituser")
    public String EditUser(Model model, @RequestParam int id) {
        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        return "registration";
    }

    @PostMapping(value = "/edituser")
    public String EditUser(Model model, @ModelAttribute User user) {
        User base = userRepository.findUserByUsername(user.getUsername());
        user.setRole("admin");
        if(user.getPassword().equals("")) {
            user.setPassword(base.getPassword());

        } else user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/manage";
    }
}
