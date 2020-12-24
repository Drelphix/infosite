package info.infosite.tasks;

import info.infosite.entities.excel.Excel;
import info.infosite.entities.excel.ExcelRepository;
import info.infosite.entities.gentable.Menu;
import info.infosite.entities.gentable.MenuRepository;
import info.infosite.entities.views.ExcelTableReportView;
import info.infosite.entities.views.XmlMenuView;
import info.infosite.entities.xml.Backup;
import info.infosite.entities.xml.Disk;
import info.infosite.functions.MenuService;
import info.infosite.functions.XMLReader;
import info.infosite.mail.MailService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
public class ScheduledTask {
    @Autowired
    public ExcelRepository excelRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuService menuService;
    @Autowired
    MailService mailService;
    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    //@Scheduled(cron = "0 0 23 * * ?")
    @Transactional
    public void ExcelCopy() throws IOException {
        String path;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        List<Excel> excels = excelRepository.findAll();
        List<Menu> menus = menuRepository.findAll();
        XSSFWorkbook workbook = new ExcelTableReportView().CreateNew(menus);
        for (Excel excel : excels) {
            path = excel.getPath();
            FileOutputStream out = new FileOutputStream(new File(path + simpleDateFormat.format(new Date()) + ".xlsx"));
            workbook.write(out);
            out.close();
        }
    }

    //@Scheduled(cron = "0 0/30 0 * * ?")
    public void CheckFreeSpace() {
        menuService.CheckMenu();
        for (XmlMenuView xmlMenuView : menuService.xmlMenus) {
            for (String path : xmlMenuView.getPaths()) {
                try {
                    XMLReader reader = new XMLReader(path);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(reader.getComputer().getDate() + " " + reader.getComputer().getTime(), formatter);
                    //Time of data aging
                    if (dateTime.minus(6, ChronoUnit.HOURS).isBefore(LocalDateTime.now())) {
                        mailService.SendEmail("Внимание, на " + reader.getComputer().getName() + " проблемы с обновлением", "Информация о " + reader.getComputer().getName() + " не обновлялась с " + dateTime.toLocalDate());
                    }
                    //Checking last backup

                    for (Backup backup : reader.getComputer().getBackups()) {
                        dateTime = LocalDateTime.parse(backup.getFiles().get(0).getLastDate() + " " + reader.getComputer().getTime(), formatter);
                        if (dateTime.minus(1, ChronoUnit.DAYS).isBefore(LocalDateTime.now())) {
                            mailService.SendEmail("Внимание, на " + reader.getComputer().getName() + " проблемы с бэкапом", "Бэкап на " + reader.getComputer().getName() + " не обновлялся более суток. Название: " + backup.getFiles().get(0).getName() + " Время: " + backup.getFiles().get(0).getLastDate());
                        }
                    }
                    //Checking free space
                    for (Disk disk : reader.getComputer().getDisks()) {
                        if (disk.getFreeSpaceGb() <= 5.0) {
                            mailService.SendEmail("Мало места на " + reader.getComputer().getName(), "Осталось " + disk.getFreeSpaceGb() + "Гб на диске " + disk.getLetter() + " на " + reader.getComputer().getName());
                        }
                    }

                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
