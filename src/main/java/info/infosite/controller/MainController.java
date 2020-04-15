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
    private List<Menu> menus;

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
        return "index";
    }

    @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam(name = "id") int id) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        List<TableView> tableViews = new ArrayList<TableView>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model.addAttribute("menus", this.menus);
        model.addAttribute("tables", tableViews);
        return "index";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String EditLines(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        Tab tab = tableRepository.getOne(idTab);
        TableView tableView = new TableView(tab);
        ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
        lines.setIdSubMenu(tableView.getSubMenu().getIdSubMenu());
        lines.setIdTable(tableView.getId());
        List<String> cols = new ArrayList<>();
        for (Col col : tableView.getCols()) {
            cols.add(col.getName());
        }
        model.addAttribute("menus", this.menus);
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
        return "redirect:/show?id=" + table.getIdSubMenu();
    }

    @RequestMapping(value = "/editTab", method = RequestMethod.GET)
    public String EditTable(Model model, @RequestParam(name = "tab") int idTab) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        model.addAttribute("table", tableView);
        model.addAttribute("menus", this.menus);
        return "add";
    }

    @RequestMapping(value = "/addCol", method = RequestMethod.GET)
    public String AddNewCol(Model model, @RequestParam(name = "tab") int idTab) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        Tab table = tableRepository.getOne(idTab);
        TableView tableView = new TableView(table);
        List<Col> cols = tableView.getCols();
        cols.add(new Col(table));

        model.addAttribute("table", tableView);
        model.addAttribute("menus", this.menus);
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

        return "redirect:/show?id=" + table.getSubMenu().getIdSubMenu();
    }

    @RequestMapping(value = "/addLine", method = RequestMethod.GET)
    public String AddNewLine(Model model, @RequestParam(name = "tab") int idTab) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        List<Line> lineList = new ArrayList<>();
        List<String> cols = new ArrayList<>();
        for (Col col : tableView.getCols()) {
            cols.add(col.getName());
            lineList.add(new Line(col));
        }
        ListLineView lines = new ListLineView(lineList);
        lines.setIdTable(tableView.getId());
        lines.setIdSubMenu(tableView.getSubMenu().getIdSubMenu());
        model.addAttribute("tableName", tableView.getName());
        model.addAttribute("cols", cols);
        model.addAttribute("lines", lines);
        model.addAttribute("menus", this.menus);
        return "add";
    }

    @RequestMapping(value = "/delCol", method = RequestMethod.GET)
    public String deleteColumn(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "col") int idCol) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        Col column = colRepository.getOne(idCol);
        List<Line> lines = column.getLines();
        for (Line line : lines) {
            lineRepository.delete(line);
        }
        colRepository.delete(column);
        return "redirect:/editTab?id=" + idTab;
    }

    @RequestMapping(value = "/delLine", method = RequestMethod.GET)
    public String deleteLine(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
        for (Line line : lines.getLines()) {
            lineRepository.delete(line);
        }
        return "redirect:/show?id=" + tableView.getSubMenu().getIdSubMenu();
    }
}
