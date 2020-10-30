package info.infosite.entities.views;

import lombok.Data;

import java.util.List;

@Data
public class XmlMenuView {
    private String name;
    private List<String> paths;
    private List<String> subs;
}
