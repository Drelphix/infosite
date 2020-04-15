package info.infosite.views;

import info.infosite.database.Line;

import java.util.List;

public class ListLineView {
    private int idSubMenu;
    private int idTable;
    private List<Line> lines;

    public ListLineView() {
    }

    public ListLineView(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public int getIdSubMenu() {
        return idSubMenu;
    }

    public void setIdSubMenu(int idSubMenu) {
        this.idSubMenu = idSubMenu;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }
}
