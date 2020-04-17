package info.infosite.controller;

import info.infosite.database.*;
import info.infosite.views.ExcelTableReportView;
import info.infosite.views.ListLineView;
import info.infosite.views.TableView;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

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
    public String IndexPage(Model model) throws IOException {
        this.menus = menuRepository.findAll();
        new ExcelTableReportView().CreateNew(this.menus);
        model.addAttribute("menus", this.menus);
        model.addAttribute("mode", this.editMode);
        return "main";
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
        return "main";
    }

    @GetMapping(value = "/mode")
    public String EditingMode(Model model, @RequestParam(name = "mode") boolean mode, HttpServletRequest request) {
        this.editMode = mode;
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping(value = "/export")
    public String DownloadExcel(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.menus = menuRepository.findAll();
        //Write the workbook in file system
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        response.setContentType("application/xls");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + simpleDateFormat.format(new Date()) + ".xlsx");
        OutputStream os = response.getOutputStream();
        XSSFWorkbook workbook = new ExcelTableReportView().CreateNew(this.menus);
        workbook.write(os);
        os.close();
        return "redirect:/";
    }

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
        try {
            for (SubMenu subMenu : menu.getSubMenuSet()) {
                subMenuRepository.save(subMenu);
            }
            menuRepository.save(menu);
        } catch (NullPointerException e) {

        }
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
        try {
            temp.addAll(table.getCols());
            tab.setCols(temp);
            tab.setName(table.getName());
            tab.setSubMenu(table.getSubMenu());
            for (Col col : temp) {
                if (col.getName() != "") {
                    colRepository.save(col);
                }
            }
            tableRepository.save(tab);
        } catch (NullPointerException e) {

        }
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
        this.menus = menuRepository.findAll();
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

    @RequestMapping(value = "/addTab", method = RequestMethod.GET)
    public String AddNewTable(Model model, @RequestParam(name = "id") int idSub) {
        CheckMenu();
        SubMenu subMenu = subMenuRepository.getOne(idSub);
        List<Tab> tables = subMenu.getTables();
        tables.add(new Tab(subMenu));
        subMenu.setTables(tables);
        model.addAttribute("submenu", subMenu);
        model.addAttribute("menus", this.menus);
        return "add";
    }

    @RequestMapping(value = "/addMenu", method = RequestMethod.GET)
    public String AddNewMenu(Model model) {
        CheckMenu();
        model.addAttribute("newmenu", new Menu());
        model.addAttribute("menus", this.menus);
        return "add";
    }

    @RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
    public String AddNewMenu(Model model, @ModelAttribute Menu menu) {
        menuRepository.save(menu);
        this.menus = menuRepository.findAll();
        model.addAttribute("menus", this.menus);
        return "redirect:/";
    }

    @RequestMapping(value = "/addTab", method = RequestMethod.POST)
    public String SaveNewTable(Model model, @ModelAttribute SubMenu subMenu) {
        CheckMenu();
        for (Tab tab : subMenu.getTables()) {
            if (tab.getName() != "") {
                tableRepository.save(tab);
            }
        }
        subMenuRepository.save(subMenu);
        model.addAttribute("menus", this.menus);
        return "redirect:/show?id=" + subMenu.getIdSubMenu();
    }

    //Delete something

    @RequestMapping(value = "/delCol", method = RequestMethod.GET)
    public String DeleteColumn(Model model, @RequestParam(name = "tab") int idTab, @RequestParam(name = "col") int idCol) {
        CheckMenu();
        Col column = colRepository.getOne(idCol);
        DeleteColumn(column);
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
    public String DeleteSubMenu(Model model, @RequestParam(name = "id") int idSubMenu) {
        CheckMenu();
        SubMenu subMenu = subMenuRepository.getOne(idSubMenu);
        DeleteSubMenu(subMenu);
        Menu menu = menuRepository.getOne(subMenu.getMenu().getIdMenu());
        this.menus = menuRepository.findAll();
        model.addAttribute("menus", this.menus);
        model.addAttribute("menu", menu);
        return "add";
    }

    @RequestMapping(value = "/delTab", method = RequestMethod.GET)
    public String DeleteTable(Model model, @RequestParam(name = "id") int idTab) {
        CheckMenu();
        Tab table = tableRepository.getOne(idTab);
        DeleteTable(table);
        return "redirect:/show?id=" + table.getSubMenu().getIdSubMenu();
    }

    @RequestMapping(value = "/delMenu", method = RequestMethod.GET)
    public String DeleteMenu(Model model, @RequestParam(name = "id") int idMenu) {
        CheckMenu();
        Menu menu = menuRepository.getOne(idMenu);
        DeleteMenu(menu);
        return "redirect:/";
    }

    private void CheckMenu() {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
    }

    private void DeleteSubMenu(SubMenu subMenu) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(subMenu);
        tx.commit();
    }

    private void DeleteColumn(Col column) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(column);
        tx.commit();
    }

    private void DeleteTable(Tab table) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(table);
        tx.commit();
    }

    private void DeleteMenu(Menu menu) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(menu);
        tx.commit();
    }
}