package info.infosite.controller;

import info.infosite.database.*;
import info.infosite.views.ListLineView;
import info.infosite.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    public ColRepository colRepository;
    public LineRepository lineRepository;
    public MenuRepository menuRepository;
    public SubMenuRepository subMenuRepository;
    public TableRepository tableRepository;

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
        List<TableView> tableViews = new ArrayList<TableView>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model.addAttribute("menus", menuRepository.findAll());
        model.addAttribute("tables", tableViews);
        return "index";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String EditLines(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
        List<String> cols = new ArrayList<>();
        for (Col col : tableView.getCols()) {
            cols.add(col.getName());
        }
        model.addAttribute("menus", menuRepository.findAll());
        model.addAttribute("tableName", tableView.getName());
        model.addAttribute("cols", cols);
        model.addAttribute("lines", lines);
        return "add";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String SaveLines(Model model, @ModelAttribute ListLineView table) {
        for (Line line : table.getLines()) {
            lineRepository.save(line);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/editTab", method = RequestMethod.GET)
    public String EditTable(Model model, @RequestParam(name = "tab") int idTab) {
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        model.addAttribute("table", tableView);
        model.addAttribute("menus", menuRepository.findAll());
        return "add";
    }

    @RequestMapping(value = "/addCol", method = RequestMethod.GET)
    public String AddNewCol(Model model, @RequestParam(name = "tab") int idTab) {
        Tab table = tableRepository.getOne(idTab);
        TableView tableView = new TableView(table);
        List<Col> cols = tableView.getCols();
        cols.add(new Col(table));
        model.addAttribute("table", tableView);
        model.addAttribute("menus", menuRepository.findAll());
        return "add";
    }

    @RequestMapping(value = "/editTab", method = RequestMethod.POST)
    public String SaveTable(Model model, @ModelAttribute(name = "table") TableView table) {
        Tab tab = tableRepository.getOne(table.getId());
        Set<Col> temp = new HashSet<>();
        temp.addAll(table.getCols());
        tab.setCols(temp);
        tab.setName(table.getName());
        tab.setSubMenu(table.getSubMenu());
        for (Col col : temp) {
            colRepository.save(col);
        }
        tableRepository.save(tab);
        return "redirect:/";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String AddNewLine(Model model, @RequestParam(name = "tab") int idTab) {
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        List<Line> lineList = new ArrayList<>();
        List<String> cols = new ArrayList<>();
        for (Col col : tableView.getCols()) {
            cols.add(col.getName());
            lineList.add(new Line(col));
        }
        ListLineView lines = new ListLineView(lineList);
        model.addAttribute("tableName", tableView.getName());
        model.addAttribute("cols", cols);
        model.addAttribute("lines", lines);
        model.addAttribute("menus", menuRepository.findAll());
        return "add";
    }

    @RequestMapping(value = "/delCol", method = RequestMethod.GET)
    public String deleteColumn(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "col") int idCol) {
        try {
            colRepository.delete(colRepository.getOne(idCol));
        } catch (Exception e) {
            model.addAttribute("message", "При удалении столбца произошла ошибка");
            model.addAttribute("delError", e);
            return "error";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/delLine", method = RequestMethod.GET)
    public String deleteLine(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        try {
            TableView tableView = new TableView(tableRepository.getOne(idTab));
            ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
            for (Line line : lines.getLines()) {
                lineRepository.delete(line);
            }
        } catch (Exception e) {
            model.addAttribute("message", "При удалении строки произошла ошибка");
            model.addAttribute("delError", e);
            return "error";
        }
        return "redirect:/";
    }
}
