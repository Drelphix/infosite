package info.infosite.controller;

import info.infosite.database.generated.*;
import info.infosite.functions.DeleteService;
import info.infosite.functions.MenuService;
import info.infosite.views.ListLineView;
import info.infosite.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeleteController {
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
    DeleteService deleteService;

    @RequestMapping(value = "/delCol", method = RequestMethod.GET)
    public String DeleteColumn(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "col") int idCol) {
        Col column = colRepository.getOne(idCol);
        deleteService.DeleteColumn(column);
        return "redirect:/editTab?tab=" + idTab;
    }


    @RequestMapping(value = "/delLine", method = RequestMethod.GET)
    public String DeleteLine(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        menuService.CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
        for (Line line : lines.getLines()) {
            lineRepository.delete(line);
        }
        return "redirect:/show?id=" + tableView.getSubMenu().getIdSubMenu();
    }

    @RequestMapping(value = "/delSub", method = RequestMethod.GET)
    public String DeleteSubMenu(Model model, @RequestParam(name = "id") int idSubMenu) {
        menuService.CheckMenu();
        SubMenu subMenu = subMenuRepository.getOne(idSubMenu);
        deleteService.DeleteSubMenu(subMenu);
        Menu menu = menuRepository.getOne(subMenu.getMenu().getIdMenu());
        menuService.menus = menuRepository.findAll();
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/delTab", method = RequestMethod.GET)
    public String DeleteTable(Model model, @RequestParam(name = "id") int idTab) {
        menuService.CheckMenu();
        Tab table = tableRepository.getOne(idTab);
        deleteService.DeleteTable(table);
        return "redirect:/show?id=" + table.getSubMenu().getIdSubMenu();
    }

    @RequestMapping(value = "/delMenu", method = RequestMethod.GET)
    public String DeleteMenu(Model model, @RequestParam(name = "id") int idMenu) {
        menuService.CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        deleteService.DeleteMenu(menu);
        return "redirect:/";
    }
}
