package net.wit.controller.makey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Counselor;
import net.wit.entity.Course;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class CounselorModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 头像 */
    private String logo;

    /** 名称 */
    private String name;

    /** 电话 */
    private String phone;

    /** 签名 */
    private String autograph;

    /** 头街 */
    private String [] speciality ;

    /** 介绍 */
    private String content;

    /** 标签名 */
    private List<TagModel> tags = new ArrayList<TagModel>();

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String[] getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String[] speciality) {
        this.speciality = speciality;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public void bind(Counselor counselor) {
        this.id = counselor.getId();
        this.name = counselor.getName();
        this.logo = counselor.getLogo();
        this.content = counselor.getContent();
        this.autograph = counselor.getAutograph();
        if (counselor.getSpeciality()!=null) {
            this.speciality = counselor.getSpeciality().split(",");
        } else {
            this.speciality = null;
        }
        this.phone = counselor.getPhone();

        this.tags = TagModel.bindList(counselor.getTags());

    }

    public static List<CounselorModel> bindList(List<Counselor> counselors) {
        List<CounselorModel> ms = new ArrayList<CounselorModel>();
        for (Counselor counselor:counselors) {
            CounselorModel m = new CounselorModel();
            m.bind(counselor);
            ms.add(m);
        }
        return ms;
    }

}