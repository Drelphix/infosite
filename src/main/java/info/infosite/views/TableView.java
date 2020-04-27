package info.infosite.views;

import info.infosite.database.generated.Col;
import info.infosite.database.generated.Line;
import info.infosite.database.generated.SubMenu;
import info.infosite.database.generated.Tab;

import java.util.ArrayList;
import java.util.List;

public class TableView {
    private int id;
    private String name;
    private List<Col> cols;
    private int numberCols;
    private List<List<Line>> lines;
    private SubMenu subMenu;

    public TableView() {
    }

    public TableView(Tab table) {
        this.id = table.getIdTable();
        this.name = table.getName();
        this.cols = table.getCols();
        this.numberCols = cols.size();
        this.subMenu = table.getSubMenu();
        lines = new ArrayList<>();
        int i = 0;
        //generating line to display
        if (cols.size() != 0) {
            int end = cols.get(0).getLines().size();
            while (i < end) {
                ArrayList<Line> lineArrayList = new ArrayList<>();
                for (Col col : this.cols) {
                    if (end < col.getLines().size()) {
                        end = col.getLines().size();
                    }
                    try {
                        if (col.isHidden()) {
                            for (Line line : col.getLines()) {
                                line.setHidden(true);
                            }
                        } else {
                            for (Line line : col.getLines()) {
                                line.setHidden(false);
                            }
                        }
                        lineArrayList.add(col.getLines().get(i));
                    } catch (Exception e) {
                        lineArrayList.add(new Line(col));
                    }

                }
                i++;
                lines.add(lineArrayList);
            }
        }
    }


    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Col> getCols() {
        return cols;
    }

    public void setCols(List<Col> cols) {
        this.cols = cols;
    }

    public int getNumberCols() {
        return numberCols;
    }

    public void setNumberCols(int numberCols) {
        this.numberCols = numberCols;
    }

    public List<List<Line>> getLines() {
        return lines;
    }

    public void setLines(List<List<Line>> lines) {
        this.lines = lines;
    }
}
