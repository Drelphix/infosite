package info.infosite.functions;

import info.infosite.database.Menu;
import info.infosite.database.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    public List<Menu> menus;
    @Autowired
    MenuRepository menuRepository;

    public void CheckMenu() {
        try {
            menus.isEmpty();
        } catch (NullPointerException e) {
            menus = menuRepository.findAll();
        }
    }
}
