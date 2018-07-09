package net.wit.controller.makey.model;
import net.wit.controller.model.BaseModel;
import net.wit.entity.AgentCategory;
import net.wit.entity.GaugeCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GaugeCategoryModel extends BaseModel implements Serializable {

    private Long id;
    /** 分类名 */

    private String name;
    /** 英文名 */
    private String english;

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

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void bind(GaugeCategory gaugeCategory) {
        this.id = gaugeCategory.getId();
        this.name = gaugeCategory.getName();
        this.count = gaugeCategory.getGauges().size();
        this.english = gaugeCategory.getEnglish();
    }
    public void bind(AgentCategory agentCategory) {
        this.id = agentCategory.getId();
        this.name = agentCategory.getName();
        this.count = agentCategory.getGauges().size();
        this.english = agentCategory.getEnglish();
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

    public static List<GaugeCategoryModel> bindAgent(List<AgentCategory> agentCategories) {
        List<GaugeCategoryModel> ms = new ArrayList<GaugeCategoryModel>();
        for (AgentCategory agentCategory:agentCategories) {
            GaugeCategoryModel m = new GaugeCategoryModel();
            m.bind(agentCategory);
            ms.add(m);
        }
        return ms;
    }

}