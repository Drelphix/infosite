package info.infosite.tasks;

import info.infosite.database.MenuRepository;
import info.infosite.views.ExcelTableReportView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@EnableScheduling
public class ScheduledTask {
    public MenuRepository menuRepository;


    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void ExcelCopy() throws IOException {
        new ExcelTableReportView().CreateNew(menuRepository.findAll());
    }
}
