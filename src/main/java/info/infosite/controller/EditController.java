package info.infosite.controller;

import info.infosite.database.*;
import info.infosite.views.ListLineView;
import info.infosite.views.TableView;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class EditController {
    public ColRepository colRepository;
    public LineRepository lineRepository;
    public MenuRepository menuRepository;
    public SubMenuRepository subMenuRepository;
    public TableRepository tableRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private List<Menu> menus;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

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

    //Edit something

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String EditLines(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
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

    @RequestMapping(value = "/editMenu", method = RequestMethod.GET)
    public String EditMenu(Model model, @RequestParam(name = "id") int idMenu) {
        CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        model.addAttribute("menus", this.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/editMenu", method = RequestMethod.POST)
    public String SaveMenu(Model model, @ModelAttribute Menu menu) {
        for (SubMenu subMenu : menu.getSubMenuSet()) {
            subMenuRepository.save(subMenu);
        }
        menuRepository.save(menu);
        return "redirect:/";
    }


    @RequestMapping(value = "/editTab", method = RequestMethod.GET)
    public String EditTable(Model model, @RequestParam(name = "tab") int idTab) {
        CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
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

    // Add something new

    @RequestMapping(value = "/addSub", method = RequestMethod.GET)
    public String AddSubMenu(Model model, @RequestParam(name = "id") int idMenu) {
        CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        List<SubMenu> subMenuList = menu.getSubMenuSet();
        subMenuList.add(new SubMenu(menu));
        menu.setSubMenuSet(subMenuList);
        model.addAttribute("menus", this.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/addCol", method = RequestMethod.GET)
    public String AddNewCol(Model model, @RequestParam(name = "tab") int idTab) {
        CheckMenu();
        Tab table = tableRepository.getOne(idTab);
        TableView tableView = new TableView(table);
        List<Col> cols = tableView.getCols();
        cols.add(new Col(table));

        model.addAttribute("table", tableView);
        model.addAttribute("menus", this.menus);
        return "add";
    }

    @RequestMapping(value = "/addLine", method = RequestMethod.GET)
    public String AddNewLine(Model model, @RequestParam(name = "tab") int idTab) {
        CheckMenu();
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

    //Delete something

    @RequestMapping(value = "/delCol", method = RequestMethod.GET)
    public String DeleteColumn(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "col") int idCol) {
        CheckMenu();
        Col column = colRepository.getOne(idCol);
        List<Line> lines = column.getLines();
        for (Line line : lines) {
            lineRepository.delete(line);
        }
        colRepository.delete(column);
        return "redirect:/editTab?tab=" + idTab;
    }


    @RequestMapping(value = "/delLine", method = RequestMethod.GET)
    public String DeleteLine(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "line") int idLine) {
        CheckMenu();
        TableView tableView = new TableView(tableRepository.getOne(idTab));
        ListLineView lines = new ListLineView(tableView.getLines().get(idLine));
        for (Line line : lines.getLines()) {
            lineRepository.delete(line);
        }
        return "redirect:/show?id=" + tableView.getSubMenu().getIdSubMenu();
    }

    @RequestMapping(value = "/delSub", method = RequestMethod.GET)
    public String DeleteSubMenu(Model model, @RequestParam(name = "idSub") int idSubMenu) {
        CheckMenu();
        SubMenu subMenu = subMenuRepository.getOne(idSubMenu);
        int idMenu = subMenu.getMenu().getIdMenu();
        DeleteSubMenu(subMenu);
        Menu menu = menuRepository.getOne(idMenu);
        model.addAttribute("menus", this.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    private void CheckMenu() {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
    }

    public void DeleteSubMenu(SubMenu subMenu) {
        Session session = getSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.delete(subMenu);
        tx.commit();
    }

}
