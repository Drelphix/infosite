package info.infosite.views;

import info.infosite.database.Line;
import info.infosite.database.Menu;
import info.infosite.database.SubMenu;
import info.infosite.database.Tab;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelTableReportView {
    Object[] objects;
    private String path;

    public XSSFWorkbook CreateNew(List<Menu> menus) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (Menu menu : menus) {
            for (SubMenu subMenu : menu.getSubMenuSet()) {
                XSSFSheet spreadsheet = workbook.createSheet(menu.getName() + "." + subMenu.getName() + "-" + subMenu.getIdSubMenu());
                XSSFRow row;
                int id = 0;
                int max = 0;
                Map<String, Object[]> empinfo =
                        new TreeMap<>();
                for (Tab tab : subMenu.getTables()) {
                    TableView tableView = new TableView(tab);
                    empinfo.put(Integer.toString(id), new Object[]{"Название таблицы : ", tableView.getName()});
                    id++;
                    this.objects = new Object[tableView.getCols().size()];
                    if (tableView.getCols().size() > max) {
                        max = tableView.getCols().size();
                    }
                    for (int i = 0; i < tableView.getCols().size(); i++) {
                        this.objects[i] = tableView.getCols().get(i).getName();
                    }
                    empinfo.put(Integer.toString(id), this.objects);
                    id++;
                    for (List<Line> lineList : tableView.getLines()) {
                        this.objects = new Object[lineList.size()];
                        for (int i = 0; i < lineList.size(); i++) {
                            this.objects[i] = lineList.get(i).getData();
                        }
                        empinfo.put(Integer.toString(id), this.objects);
                        id++;
                    }

                    Set<String> keyid = empinfo.keySet();
                    int rowid = 0;
                    for (String key : keyid) {
                        row = spreadsheet.createRow(rowid++);
                        Object[] objectArr = empinfo.get(key);
                        int cellid = 0;

                        for (Object obj : objectArr) {
                            Cell cell = row.createCell(cellid++);
                            cell.setCellValue((String) obj);
                        }
                    }
                }
                for (int i = 0; i <= max; i++) {
                    spreadsheet.autoSizeColumn(i);
                }
            }
        }

        return workbook;
    }
}
