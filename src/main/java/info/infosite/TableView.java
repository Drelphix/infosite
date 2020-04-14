package info.infosite;

import info.infosite.database.Col;
import info.infosite.database.Line;
import info.infosite.database.Tab;

import java.util.ArrayList;
import java.util.List;

public class TableView {
    private int id;
    private String name;
    private List<Col> cols;
    private int numberCols;
    private List<List<Line>> lines;

    public TableView(Tab table) {
        this.id = table.getIdTable();
        this.name = table.getName();
        this.cols = new ArrayList<>();
        this.cols.addAll(table.getCols());
        this.numberCols = cols.size();

        lines = new ArrayList<>();
        int i = 0;
        int end = cols.get(0).getLines().size();
        SortCols();
        while (i < end) {
            ArrayList<Line> lineArrayList = new ArrayList<>();
            for (Col col : this.cols) {
                if (end < col.getLines().size()) {
                    end = col.getLines().size();
                }
                try {
                    lineArrayList.add(col.getLines().get(i));
                } catch (Exception e) {
                    lineArrayList.add(new Line(col));
                }

            }
            i++;
            lines.add(lineArrayList);
        }

    }

    private void SortCols() {
        Col temp = null;
        Boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < cols.size() - 1; i++) {
                if (cols.get(i).getIdColumn() > cols.get(i + 1).getIdColumn()) {
                    isSorted = false;
                    temp = cols.get(i);
                    cols.set(i, cols.get(i + 1));
                    cols.set(i + 1, temp);
                }
            }
        }
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
