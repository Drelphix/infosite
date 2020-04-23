package info.infosite.tasks;

import info.infosite.database.Excel;
import info.infosite.database.ExcelRepository;
import info.infosite.database.generated.MenuRepository;
import info.infosite.views.ExcelTableReportView;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EnableScheduling
public class ScheduledTask {
    public MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public ExcelRepository excelRepository;

    @Autowired
    public void setExcelRepository(ExcelRepository excelRepository) {
        this.excelRepository = excelRepository;
    }

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
}
