package info.infosite.controller;

import info.infosite.entities.auth.Role;
import info.infosite.entities.auth.RoleRepository;
import info.infosite.entities.gentable.*;
import info.infosite.entities.guide.Guide;
import info.infosite.entities.guide.GuideRepository;
import info.infosite.entities.views.ListLineView;
import info.infosite.entities.views.TableView;
import info.infosite.functions.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AddController {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private SubMenuRepository subMenuRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    MenuService menuService;
    @Autowired
    private GuideRepository guideRepository;
    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/submenu/new", method = RequestMethod.GET)
    public String AddSubMenu(Model model, @RequestParam(name = "id") int idMenu) {
        menuService.CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        menu.AddSubMenu(new SubMenu(menu));
        menuService.menus = menuRepository.findAll();
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/column/new", method = RequestMethod.GET)
    public String AddNewCol(Model model, @RequestParam(name = "tab") int idTab) {
        menuService.CheckMenu();
        Tab table = tableRepository.getOne(idTab);
        TableView tableView = new TableView(table);
        List<Col> cols = tableView.getCols();
        cols.add(new Col(table));
        model.addAttribute("table", tableView);
        model.addAttribute("menus", menuService.menus);
        return "add";
    }

    @RequestMapping(value = "/line/new", method = RequestMethod.GET)
    public String AddNewLine(Model model, @RequestParam(name = "tab") int idTab) {
        menuService.CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        List<Line> lineList = new ArrayList<>();
        for (Col col : tableView.getCols()) {
            lineList.add(new Line(col));
        }
        ListLineView lines = new ListLineView(lineList);
        lines.setIdTable(tableView.getId());
        lines.setIdSubMenu(tableView.getSubMenu().getIdSubMenu());
        model.addAttribute("tableName", tableView.getName());
        model.addAttribute("cols", tableView.getCols());
        model.addAttribute("lines", lines);
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "add";
    }

    @RequestMapping(value = "/table/new", method = RequestMethod.GET)
    public String AddNewTable(Model model, @RequestParam(name = "id") int idSub) {
        menuService.CheckMenu();
        SubMenu subMenu = subMenuRepository.getOne(idSub);
        subMenu.AddNewTable(new Tab(subMenu));
        model.addAttribute("submenu", subMenu);
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "add";
    }

    @RequestMapping(value = "/menu/new", method = RequestMethod.GET)
    public String AddNewMenu(Model model) {
        menuService.CheckMenu();
        model.addAttribute("newmenu", new Menu());
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "add";
    }

    @RequestMapping(value = "/menu/save", method = RequestMethod.POST)
    public String AddNewMenu(Model model, @ModelAttribute Menu menu) {
        menuRepository.save(menu);
        menuService.menus = menuRepository.findAll();
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "redirect:/";
    }

    @RequestMapping(value = "/table/new", method = RequestMethod.POST)
    public String SaveNewTable(Model model, @ModelAttribute SubMenu subMenu) {
        menuService.CheckMenu();
        for (Tab tab : subMenu.getTables()) {
            if (tab.getName() != "") {
                tableRepository.save(tab);
            }
        }
        subMenuRepository.save(subMenu);
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "redirect:/show?id=" + subMenu.getIdSubMenu();
    }

    @RequestMapping(value = "/guide/new", method = RequestMethod.GET)
    public String AddNewGuide(Model model, HttpSession httpSession) {
        Guide guide = new Guide();
        System.out.println(guide.getUsername());
        menuService.CheckMenu();
        model.addAttribute("currentGuide",guide);
        model.addAttribute("mode", httpSession.getAttribute("mode"));
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        model.addAttribute("guide", guide);
        return "addguide";
    }

    @PostMapping(value = "/guide/show")
    public String showTempGuide(Model model,@ModelAttribute Guide guide){
        guide.setLastEditDate(LocalDate.now().toString());
        guide.setLastEditUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("currentGuide", guide);
        try {
            if (guide.getId() == 0) throw new NullPointerException();
            model.addAttribute("edit", true);
        } catch (NullPointerException skip) {
        }
        return "addguide";
    }

    @RequestMapping(value = "/guide/save", method = RequestMethod.POST)
    public String SaveNewGuide(Model model, @ModelAttribute Guide guide) {
        try {
            if (guide.getDate().equals("") | guide.getUsername().equals("")) {
                throw new NullPointerException();
            }
            guide.setLastEditDate(LocalDate.now().toString());
            guide.setLastEditUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (NullPointerException e) {
            guide.setDate(LocalDate.now().toString());
            guide.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        guideRepository.saveAndFlush(guide);
        return "redirect:/guides";
    }
    @RequestMapping(value = "/role/new",method = RequestMethod.POST)
    public String AddNewRole(Model model,@RequestParam String role){
        if (roleRepository.findRoleByRole(role) == null) {
            Role newRole = new Role();
            newRole.setRole(role);
            roleRepository.save(newRole);
        }
        return "redirect:/management";
    }
}
