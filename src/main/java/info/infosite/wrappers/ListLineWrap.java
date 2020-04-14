package info.infosite.wrappers;

import info.infosite.database.Line;

import java.util.List;

public class ListLineWrap {
    private List<Line> lines;

    public ListLineWrap() {
    }

    public ListLineWrap(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
