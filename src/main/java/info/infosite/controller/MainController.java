package info.infosite.controller;

import info.infosite.database.MenuRepository;
import info.infosite.database.Tab;
import info.infosite.database.TableRepository;
import info.infosite.functions.MenuService;
import info.infosite.views.ExcelTableReportView;
import info.infosite.views.TableView;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    public MenuRepository menuRepository;
    @Autowired
    public TableRepository tableRepository;
    @Autowired
    MenuService menuService;
    boolean editMode = false;

    @GetMapping(value = "/")
    public String IndexPage(Model model) throws IOException {
        menuService.menus = menuRepository.findAll();
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("mode", this.editMode);
        return "main";
    }

    @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam(name = "id") int id) {

        menuService.CheckMenu();
        List<TableView> tableViews = new ArrayList<TableView>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model.addAttribute("menus", menuService.menus);
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
        menuService.menus = menuRepository.findAll();
        //Write the workbook in file system
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        response.setContentType("application/xls");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + simpleDateFormat.format(new Date()) + ".xlsx");
        OutputStream os = response.getOutputStream();
        XSSFWorkbook workbook = new ExcelTableReportView().CreateNew(menuService.menus);
        workbook.write(os);
        os.close();
        return "redirect:/";
    }
}