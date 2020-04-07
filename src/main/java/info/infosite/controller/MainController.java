package info.infosite.controller;

import info.infosite.TableView;
import info.infosite.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    public ColRepository colRepository;
    public LineRepository lineRepository;
    public MenuRepository menuRepository;
    public SubMenuRepository subMenuRepository;
    public TableRepository tableRepository;
    private List<TableView> tableViews;

    @Autowired
    public void setColRepository(ColRepository colRepository) {
        this.colRepository = colRepository;
    }
    @Autowired
    public void setLineRepository(LineRepository lineRepository) {
        this.lineRepository= lineRepository;
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
    public String IndexPage(Model model){
        model.addAttribute("menus",menuRepository.findAll());
     return "index";
 }

    @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam(name = "id") int id) {
        this.tableViews = new ArrayList<TableView>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model.addAttribute("menus", menuRepository.findAll());
        model.addAttribute("tables", tableViews);
        return "index";
    }

    @GetMapping(value = "/edit")
    public String ShowChange(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {

        TableView tableView = tableViews.get(idTab);
        List<Line> lines = tableView.getLines().get(idLine);
        model.addAttribute("editLine", lines);
        model.addAttribute("tableName", tableView.getName());
        model.addAttribute("cols", tableView.getCols());
        return "add";
    }

    @PostMapping(value = "/edit")
    public String Change(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {

        return "index";
    }
}
