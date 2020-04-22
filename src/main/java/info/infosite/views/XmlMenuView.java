package info.infosite.views;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class XmlMenuView {
    private String name;
    private List<String> paths;
    private List<String> subs;
}
