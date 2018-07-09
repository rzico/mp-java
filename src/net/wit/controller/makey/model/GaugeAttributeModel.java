package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Gauge;
import net.wit.entity.MemberAttribute;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class GaugeAttributeModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void bind(MemberAttribute memberAttribute) {
        this.id = memberAttribute.getId();
        this.title = memberAttribute.getName();
        this.value = "";
    }

    public static List<GaugeAttributeModel> bindList(List<MemberAttribute> memberAttributes) {
        List<GaugeAttributeModel> ms = new ArrayList<GaugeAttributeModel>();
        for (MemberAttribute memberAttribute:memberAttributes) {
            GaugeAttributeModel m = new GaugeAttributeModel();
            m.bind(memberAttribute);
            ms.add(m);
        }
        return ms;
    }

}