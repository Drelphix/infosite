package info.infosite.controller;

import info.infosite.database.generated.MenuRepository;
import info.infosite.database.generated.Tab;
import info.infosite.database.generated.TableRepository;
import info.infosite.functions.MenuService;
import info.infosite.functions.XMLReader;
import info.infosite.views.ExcelTableReportView;
import info.infosite.views.TableView;
import info.infosite.views.XmlMenuView;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes("mode")
public class MainController {
    @Autowired
    public MenuRepository menuRepository;
    @Autowired
    public TableRepository tableRepository;
    @Autowired
    MenuService menuService;

    @GetMapping(value = "/")
    public String IndexPage(Model model, HttpSession httpSession) {
        menuService.CheckMenu();
        try {
            return "redirect:/show?id=" + menuService.menus.get(0).getSubMenuSet().get(0).getIdSubMenu();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            menuService.CheckMenu();
            model.addAttribute("xmls", menuService.xmlMenus);
            model.addAttribute("menus", menuService.menus);
            CheckMode(httpSession);
            model.addAttribute("mode", httpSession.getAttribute("mode"));
            return "main";
        }
    }


    @GetMapping(value = "/login")
    public String LoginPage(Model model) {
        return "login";
    }


    @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam(name = "id") int id, HttpSession httpSession) {
        menuService.CheckMenu();
        List<TableView> tableViews = new ArrayList<>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("submenu", id);
        model.addAttribute("tables", tableViews);
        model.addAttribute("xmls", menuService.xmlMenus);
        CheckMode(httpSession);
        model.addAttribute("mode", httpSession.getAttribute("mode"));
        return "main";
    }

    @GetMapping(value = "/mode")
    public String EditingMode(Model model, @RequestParam(name = "mode") boolean mode, HttpServletRequest request, HttpSession httpSession) {
        httpSession.setAttribute("mode", mode);
        model.addAttribute("mode", httpSession.getAttribute("mode"));
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

    @GetMapping(value = "/{name}")
    public String ShowDiskInfo(Model model, @PathVariable(name = "name") String name, @RequestParam(name = "id") int id, HttpSession httpSession) {
        menuService.CheckMenu();
        for (XmlMenuView xmlMenuView : menuService.xmlMenus) {
            if (xmlMenuView.getName().equals(name)) {
                String path = xmlMenuView.getPaths().get(id);
                XMLReader xmlReader = null;
                try {
                    xmlReader = new XMLReader(path);
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
                CheckMode(httpSession);
                model.addAttribute("mode", httpSession.getAttribute("mode"));
                model.addAttribute("menus", menuService.menus);
                model.addAttribute("xmls", menuService.xmlMenus);
                model.addAttribute("computer", xmlReader.getComputer());
            }
        }
        return "computer";
    }

    private void CheckMode(HttpSession httpSession) {
        try {
            if (!httpSession.getAttribute("mode").equals(true)) {
                httpSession.setAttribute("mode", false);
            }
        } catch (NullPointerException e) {
            httpSession.setAttribute("mode", false);
        }
    }
}