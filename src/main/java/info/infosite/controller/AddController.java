package info.infosite.controller;

import info.infosite.entities.auth.Role;
import info.infosite.entities.auth.RoleRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("mode")
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
    @Autowired
    private GuideMenuRepository guideMenuRepository;

    @RequestMapping(value = "/submenu/new", method = RequestMethod.GET)
    public String AddSubMenu(Model model, @RequestParam(name = "id") int idMenu,HttpSession httpSession) {
        Menu menu = menuRepository.getOne(idMenu);
        menu.AddSubMenu(new SubMenu(menu));
        menuService.menus = menuRepository.findAll();
        menuService.addMenu(model,httpSession);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/column/new", method = RequestMethod.GET)
    public String AddNewCol(Model model, @RequestParam(name = "tab") int idTab,HttpSession httpSession) {
        Tab table = tableRepository.getOne(idTab);
        TableView tableView = new TableView(table);
        List<Col> cols = tableView.getCols();
        cols.add(new Col(table));
        model.addAttribute("table", tableView);
        menuService.addMenu(model,httpSession);
        return "add";
    }

    @RequestMapping(value = "/line/new", method = RequestMethod.GET)
    public String AddNewLine(Model model, @RequestParam(name = "tab") int idTab,HttpSession httpSession) {
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
        menuService.addMenu(model,httpSession);
        return "add";
    }

    @RequestMapping(value = "/table/new", method = RequestMethod.GET)
    public String AddNewTable(Model model, @RequestParam(name = "id") int idSub, HttpSession httpSession) {
        SubMenu subMenu = subMenuRepository.getOne(idSub);
        subMenu.AddNewTable(new Tab(subMenu));
        model.addAttribute("submenu", subMenu);
        menuService.addMenu(model,httpSession);
        return "add";
    }

    @RequestMapping(value = "/menu/new", method = RequestMethod.GET)
    public String AddNewMenu(Model model,HttpSession httpSession) {
        model.addAttribute("newmenu", new Menu());
        menuService.addMenu(model,httpSession);
        return "add";
    }

    @RequestMapping(value = "/menu/save", method = RequestMethod.POST)
    public String AddNewMenu(Model model, @ModelAttribute Menu menu,HttpSession httpSession) {
        menuRepository.save(menu);
        menuService.menus = menuRepository.findAll();
        menuService.addMenu(model,httpSession);
        return "redirect:/";
    }

    @RequestMapping(value = "/table/new", method = RequestMethod.POST)
    public String SaveNewTable(Model model,HttpSession httpSession, @ModelAttribute SubMenu subMenu) {
        for (Tab tab : subMenu.getTables()) {
            if (tab.getName() != "") {
                tableRepository.save(tab);
            }
        }
        subMenuRepository.save(subMenu);
        menuService.addMenu(model,httpSession);
        return "redirect:/show?id=" + subMenu.getIdSubMenu();
    }

    @RequestMapping(value = "/guide/new", method = RequestMethod.GET)
    public String AddNewGuide(Model model, HttpSession httpSession,@RequestParam int mid) {
        Guide guide = new Guide();
        guide.setMenu(guideMenuRepository.getOne(mid));
        System.out.println(guide.getUsername());
        menuService.addMenu(model,httpSession);
        model.addAttribute("currentGuide",guide);
        model.addAttribute("mode", httpSession.getAttribute("mode"));
        model.addAttribute("guideMenus",guideMenuRepository.findAll());
        model.addAttribute("guide", guide);
        return "addguide";
    }

    @PostMapping(value = "/guide/show")
    public String showTempGuide(Model model,@ModelAttribute Guide guide){
        if(!guide.getDate().equals("")) {
            guide.setLastEditDate(LocalDate.now().toString());
            guide.setLastEditUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            guide.setDate(LocalDate.now().toString());
            guide.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        model.addAttribute("currentGuide", guide);
        model.addAttribute("guide", guide);
        try {
            if (guide.getId() == 0) throw new NullPointerException();

            model.addAttribute("edit", true);
        } catch (NullPointerException skip) {
        }
        model.addAttribute("guideMenus",guideMenuRepository.findAll());
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
        return "redirect:/guide/?id=" + guide.getId();
    }

    @RequestMapping(value = "/role/new", method = RequestMethod.POST)
    public String AddNewRole(Model model, @RequestParam String role) {
        if (roleRepository.findRoleByRole(role) == null) {
            Role newRole = new Role(role);
            roleRepository.save(newRole);
        }
        return "redirect:/management";
    }

    @RequestMapping(value = "/guideMenu/new", method = RequestMethod.GET)
    public String AddNewGuideMenu(Model model, HttpSession httpSession) {
        model.addAttribute("newGuideMenu", new GuideMenu());
        menuService.CheckMenu();
        model = menuService.addMenu(model, httpSession);
        model.addAttribute("guideMenu", guideMenuRepository.findAll());
        return "guides";
    }

    @RequestMapping(value = "/guideMenu/save", method = RequestMethod.POST)
    public String SaveNewGuideMenu(Model model, @ModelAttribute GuideMenu newGuideMenu) {
        try {
            GuideMenu guideMenu = new GuideMenu(newGuideMenu.getName());
            guideMenuRepository.save(guideMenu);
            return "redirect:/guides";
        } catch (NullPointerException e) {
            model.addAttribute("error", "Такое имя уже существует");
        }
        return "guides";
    }
}
