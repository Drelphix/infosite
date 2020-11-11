package info.infosite.functions;

import info.infosite.entities.gentable.Menu;
import info.infosite.entities.gentable.MenuRepository;
import info.infosite.entities.views.XmlMenuView;
import info.infosite.entities.xml.Xml;
import info.infosite.entities.xml.XmlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    public List<Menu> menus;
    public List<XmlMenuView> xmlMenus;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    XmlRepository xmlRepository;

    public void CheckMenu() {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
        GetXmlMenu();
    }

    public void GetXmlMenu() {
        xmlMenus = new ArrayList<>();
        for (Xml xml : xmlRepository.findAll()) {
            XmlMenuView xmlMenu = new XmlMenuView();
            xmlMenu.setName(xml.getMenuName());
            List<String> xmlSubMenus = new ArrayList<>();
            List<String> xmlSubPaths = new ArrayList<>();
            File dir = new File(xml.getPath());
            File[] arrFiles = dir.listFiles();
            File[] lst = arrFiles;
            try {
                for (File file : lst) {
                    xmlSubPaths.add(file.toString());
                    xmlSubMenus.add(file.getName().substring(0, file.getName().length() - 4));
                }
                xmlMenu.setSubs(xmlSubMenus);
                xmlMenu.setPaths(xmlSubPaths);
            } catch (NullPointerException ignored) {

            }
            this.xmlMenus.add(xmlMenu);
        }
    }
    public Model addMenu(Model model, HttpSession httpSession) {
        model.addAttribute("menus", this.menus);
        model.addAttribute("xmls", this.xmlMenus);
        CheckMode(httpSession);
        model.addAttribute("mode", httpSession.getAttribute("mode"));
        return model;
    }

    public void CheckMode(HttpSession httpSession) {
        try {
            if (!httpSession.getAttribute("mode").equals(true)) {
                httpSession.setAttribute("mode", false);
            }
        } catch (NullPointerException e) {
            httpSession.setAttribute("mode", false);
        }
    }
}
