package info.infosite.controller;

import info.infosite.entities.auth.UserRepository;
import info.infosite.entities.gentable.MenuRepository;
import info.infosite.entities.gentable.Tab;
import info.infosite.entities.gentable.TableRepository;
import info.infosite.entities.guide.GuideRepository;
import info.infosite.entities.request.RequestRepository;
import info.infosite.entities.request.Status;
import info.infosite.entities.views.ExcelTableReportView;
import info.infosite.entities.views.TableView;
import info.infosite.entities.views.XmlMenuView;
import info.infosite.functions.MenuService;
import info.infosite.functions.XMLReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public UserRepository userRepository;
    @Autowired
    public GuideRepository guideRepository;
    @Autowired
    public RequestRepository requestRepository;
    @Autowired
    MenuService menuService;

    @GetMapping(value = "/")
    public String IndexPage(Model model, HttpSession httpSession) {
        menuService.CheckMenu();
        try {
            return "redirect:/show?id=" + menuService.menus.get(0).getSubMenuSet().get(0).getIdSubMenu();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            CheckMode(httpSession);
            model = addMenu(model, httpSession);
            return "main";
        }
    }

    @GetMapping(value = "/management")
    public String ManageUsers(Model model, HttpSession httpSession) {
        menuService.CheckMenu();
        CheckMode(httpSession);
        model = addMenu(model, httpSession);
        model.addAttribute("users", userRepository.findAll(Sort.by(Sort.Direction.ASC, "username")));
        return "management";
    }

    @GetMapping(value = "/show")
    public String ShowTables(Model model, @RequestParam(name = "id") int id, HttpSession httpSession) {
        menuService.CheckMenu();
        List<TableView> tableViews = new ArrayList<>();
        for (Tab tab : tableRepository.findTableBySubMenuId(id)) {
            tableViews.add(new TableView(tab));
        }
        model = addMenu(model, httpSession);
        model.addAttribute("submenu", id);
        model.addAttribute("tables", tableViews);
        return "main";
    }

    @GetMapping(value = "/orders")
    public String ShowUnmarkedOrders(Model model, HttpSession httpSession) {
        menuService.CheckMenu();
        model = addMenu(model, httpSession);
        model.addAttribute("orders", requestRepository.findAllByStatusNot(Status.COMPLETED));
        return "requests";
    }

    @GetMapping(value = "/order")
    public String ShowOrder(Model model, @RequestParam String show, HttpSession httpSession) {
        menuService.CheckMenu();
        model = addMenu(model, httpSession);
        try {
            if (show.equals("all")) {
                model.addAttribute("orders", requestRepository.findAll());
                model.addAttribute("selStatus", "");
                return "requests";
            } else {
                if (show.equals("active")) {
                    model.addAttribute("orders", requestRepository.findAllByStatus(Status.ACTIVE));
                    return "requests";
                } else {
                    model.addAttribute("orders", requestRepository.findAllByStatus(Status.IN_WORK));
                }
            }
        } catch (Exception e) {
            model.addAttribute("orders", requestRepository.findAll());
            model.addAttribute("selStatus", "");
            return "requests";
        }
        return "requests";
    }


    @GetMapping(value = "/mode")
    public String EditingMode(Model model, @RequestParam boolean mode, HttpServletRequest request) {
        model.addAttribute("mode", mode);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping(value = "/export")
    public String DownloadExcel(HttpServletResponse response) throws IOException {
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

    @GetMapping(value = "/guides")
    public String ShowGuides(Model model, HttpSession httpSession) {
        menuService.CheckMenu();
        model = addMenu(model, httpSession);
        model.addAttribute("guides", guideRepository.findAll());
        return "guides";
    }

    @GetMapping(value = "/guide")
    public String ShowGuide(Model model, HttpSession httpSession, @RequestParam(name = "id") int id) {
        menuService.CheckMenu();
        model.addAttribute("guides", guideRepository.findAll());
        model = addMenu(model, httpSession);
        model.addAttribute("currentGuide", guideRepository.getOne(id));
        return "guides";
    }


    @GetMapping(value = "/xml/{name}")
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
                model = addMenu(model, httpSession);
                model.addAttribute("computer", xmlReader.getComputer());
            }
        }
        return "computer";
    }

    @GetMapping(value = "/requests")
    public String showRequests(Model model) {

        return "request";
    }

    //Новая заявка
    @GetMapping(value = "/request/new")
    public String addNewRequest(Model model) {

        return "request";
    }

    //Подтверждение заявки
    @GetMapping(value = "/request/confirm")
    public String confirmRequest(Model model) {

        return "request";
    }

    //Отметка о выполнении заявки
    @GetMapping(value = "/request/complete")
    public String completeRequest(Model model) {

        return "request";
    }


    private Model addMenu(Model model, HttpSession httpSession) {
        model.addAttribute("menus", menuService.menus);
        model.addAttribute("xmls", menuService.xmlMenus);
        CheckMode(httpSession);
        model.addAttribute("mode", httpSession.getAttribute("mode"));
        return model;
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