package info.infosite.controller;

import info.infosite.database.generated.*;
import info.infosite.functions.MenuService;
import info.infosite.views.ListLineView;
import info.infosite.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AddController {
    @Autowired
    public MenuRepository menuRepository;
    @Autowired
    public SubMenuRepository subMenuRepository;
    @Autowired
    public TableRepository tableRepository;
    @Autowired
    MenuService menuService;

    @RequestMapping(value = "/addSub", method = RequestMethod.GET)
    public String AddSubMenu(Model model, @RequestParam(name = "id") int idMenu) {
        menuService.CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        menu.AddSubMenu(new SubMenu(menu));
        menuService.menus = menuRepository.findAll();
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/addCol", method = RequestMethod.GET)
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

    @RequestMapping(value = "/addLine", method = RequestMethod.GET)
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

    @RequestMapping(value = "/addTab", method = RequestMethod.GET)
    public String AddNewTable(Model model, @RequestParam(name = "id") int idSub) {
        menuService.CheckMenu();
        SubMenu subMenu = subMenuRepository.getOne(idSub);
        subMenu.AddNewTable(new Tab(subMenu));
        model.addAttribute("submenu", subMenu);
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "add";
    }

    @RequestMapping(value = "/addMenu", method = RequestMethod.GET)
    public String AddNewMenu(Model model) {
        menuService.CheckMenu();
        model.addAttribute("newmenu", new Menu());
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "add";
    }

    @RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
    public String AddNewMenu(Model model, @ModelAttribute Menu menu) {
        menuRepository.save(menu);
        menuService.menus = menuRepository.findAll();
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        return "redirect:/";
    }

    @RequestMapping(value = "/addTab", method = RequestMethod.POST)
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
}
