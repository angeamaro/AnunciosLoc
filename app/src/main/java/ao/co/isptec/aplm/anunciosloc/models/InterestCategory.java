package ao.co.isptec.aplm.anunciosloc.models;

import java.util.ArrayList;
import java.util.List;

public class InterestCategory {
    private String id;
    private String name;
    private int iconRes;
    private List<String> values;

    public InterestCategory(String id, String name, int iconRes, List<String> values) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public List<String> getValues() {
        return values != null ? values : new ArrayList<>();
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public int getValuesCount() {
        return values != null ? values.size() : 0;
    }
}
