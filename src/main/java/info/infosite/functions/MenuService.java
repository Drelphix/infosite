package info.infosite.functions;

import info.infosite.database.Menu;
import info.infosite.database.MenuRepository;
import info.infosite.database.Xml;
import info.infosite.database.XmlRepository;
import info.infosite.views.XmlMenuView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            for (File file : lst) {
                xmlSubPaths.add(file.toString());
                xmlSubMenus.add(file.getName().substring(0, file.getName().length() - 4));
            }
            xmlMenu.setSubs(xmlSubMenus);
            xmlMenu.setPaths(xmlSubPaths);
            this.xmlMenus.add(xmlMenu);
        }
    }
}
