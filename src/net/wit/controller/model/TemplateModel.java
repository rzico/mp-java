package net.wit.controller.model;

import net.wit.entity.Template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章编辑模板

public class TemplateModel extends BaseModel implements Serializable {

    /** id */
    private Long id;

    /** 缩例图 */
    private String sn;
    /** 名称 */
    private String name;
    /** 缩例图 */
    private String thumbnial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnial() {
        return thumbnial;
    }

    public void setThumbnial(String thumbnial) {
        this.thumbnial = thumbnial;
    }

    public void bind(Template template) {
        this.id = template.getId();
        this.sn = template.getSn();
        this.name = template.getName();
        this.thumbnial = template.getThumbnial();
    }

    public static List<TemplateModel> bindList(List<Template> templates,Template.Type type) {
        List<TemplateModel> ms = new ArrayList<TemplateModel>();
        for (Template template:templates) {
            if (template.getType().equals(type)) {
                TemplateModel m = new TemplateModel();
                m.bind(template);
                ms.add(m);
            }
        }
        return ms;
    }


}