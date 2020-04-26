package info.infosite.tasks;

import info.infosite.database.Excel;
import info.infosite.database.ExcelRepository;
import info.infosite.database.generated.MenuRepository;
import info.infosite.entities.Disk;
import info.infosite.functions.MenuService;
import info.infosite.functions.XMLReader;
import info.infosite.mail.MailService;
import info.infosite.views.ExcelTableReportView;
import info.infosite.views.XmlMenuView;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@EnableScheduling
public class ScheduledTask {
    @Autowired
    public ExcelRepository excelRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuService menuService;
    @Autowired
    MailService mailService;

    @Scheduled(cron = "0 0 12 * * ?")
    public void ExcelCopy() throws IOException {
        String path;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        List<Excel> excels = excelRepository.findAll();
        XSSFWorkbook workbook = new ExcelTableReportView().CreateNew(menuRepository.findAll());
        for (Excel excel : excels) {
            path = excel.getPath();
            FileOutputStream out = new FileOutputStream(new File(path + simpleDateFormat.format(new Date()) + ".xlsx"));
            workbook.write(out);
            out.close();
        }
    }

    @Scheduled(cron = "0 0/30 * * *")
    public void CheckFreeSpace() {
        for (XmlMenuView xmlMenuView : menuService.xmlMenus) {
            for (String path : xmlMenuView.getPaths()) {
                try {
                    XMLReader reader = new XMLReader(path);
                    LocalDateTime dateTime = LocalDateTime.parse(reader.getComputer().getDate() + "T" + reader.getComputer().getTime());
                    if (dateTime.minus(5, ChronoUnit.HOURS).isBefore(LocalDateTime.now())) {
                        mailService.SendEmail("Info about " + reader.getComputer().getName(), "Information about " + reader.getComputer().getName() + " didn't update since " + dateTime);
                    }
                    for (Disk disk : reader.getComputer().getDisks()) {
                        if (disk.getFreeSpaceGb() <= 5.0) {
                            mailService.SendEmail("ALARM DISK SIZE AT " + reader.getComputer().getName(), "There are " + disk.getFreeSpaceGb() + " at disk " + disk.getLetter() + " at " + reader.getComputer().getName());
                        }
                    }
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
