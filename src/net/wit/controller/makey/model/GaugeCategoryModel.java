package net.wit.controller.makey.model;
import net.wit.controller.model.BaseModel;
import net.wit.entity.GaugeCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GaugeCategoryModel extends BaseModel implements Serializable {

    private Long id;
    private String name;
    private int count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void bind(GaugeCategory gaugeCategory) {
        this.id = gaugeCategory.getId();
        this.name = gaugeCategory.getName();
        this.count = gaugeCategory.getGauges().size();
    }
    public static List<GaugeCategoryModel> bindList(List<GaugeCategory> gaugeCategories) {
        List<GaugeCategoryModel> ms = new ArrayList<GaugeCategoryModel>();
        for (GaugeCategory gaugeCategory:gaugeCategories) {
          GaugeCategoryModel m = new GaugeCategoryModel();
          m.bind(gaugeCategory);
          ms.add(m);
        }
        return ms;
    }

}