package net.wit.controller.weex.model;

import net.wit.entity.Tag;
import net.wit.entity.Template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章编辑模板

public class TemplateModel implements Serializable {

    /** 缩例图 */
    private String sn;
    /** 名称 */
    private String name;
    /** 文件名 */
    private String vue;
    /** 缩例图 */
    private String thumbnial;

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

    public String getVue() {
        return vue;
    }

    public void setVue(String vue) {
        this.vue = vue;
    }

    public void bind(Template template) {
        this.sn = template.getSn();
        this.name = template.getName();
        this.thumbnial = template.getThumbnial();
        this.vue = template.getVue();
    }

    public static List<TemplateModel> bindList(List<Template> templates) {
        List<TemplateModel> ms = new ArrayList<TemplateModel>();
        for (Template template:templates) {
            TemplateModel m = new TemplateModel();
            m.bind(template);
            ms.add(m);
        }
        return ms;
    }


}