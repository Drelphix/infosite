package info.infosite.views;

import info.infosite.database.Line;

import java.util.List;

public class ListLineView {

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

}
