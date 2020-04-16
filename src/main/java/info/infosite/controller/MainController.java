package info.infosite.controller;

import info.infosite.database.*;
import info.infosite.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    public ColRepository colRepository;
    public LineRepository lineRepository;
    public MenuRepository menuRepository;
    public SubMenuRepository subMenuRepository;
    public TableRepository tableRepository;
    private List<Menu> menus;
    boolean editMode = false;

    @Autowired
    public void setColRepository(ColRepository colRepository) {
        this.colRepository = colRepository;
    }

    @Autowired
    public void setLineRepository(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Autowired
    public void setSubMenuRepository(SubMenuRepository subMenuRepository) {
        this.subMenuRepository = subMenuRepository;
    }

    @Autowired
    public void setTableRepository(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @GetMapping(value = "/")
    public String IndexPage(Model model) {
        this.menus = menuRepository.findAll();
        model.addAttribute("menus", this.menus);
        model.addAttribute("mode", this.editMode);
        return "index";
    }

    @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam(name = "id") int id) {

        this.menus = menuRepository.findAll();
        List<TableView> tableViews = new ArrayList<TableView>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model.addAttribute("menus", this.menus);
        model.addAttribute("submenu", id);
        model.addAttribute("tables", tableViews);
        model.addAttribute("mode", this.editMode);
        return "index";
    }

    @GetMapping(value = "/mode")
    public String EditingMode(Model model, @RequestParam(name = "mode") boolean mode, HttpServletRequest request) {
        this.editMode = mode;
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}