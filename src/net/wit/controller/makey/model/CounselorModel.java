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
    private String speciality ;

    /** 领域 */
    private String[] fields ;

    /** 简介 */
    private String content1;

    /** 教育经历 */
    private String content2;

    /** 工作经历 */
    private String content3;

    /** 行业执照 */
    private String content4;

    /** 治疗取向 */
    private String content5;

    /** 咨询经验 */
    private String content6;

    /** 擅长领域 */
    private String content7;

    /** 咨询方式与费用 */
    private String content8;

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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getContent4() {
        return content4;
    }

    public void setContent4(String content4) {
        this.content4 = content4;
    }

    public String getContent5() {
        return content5;
    }

    public void setContent5(String content5) {
        this.content5 = content5;
    }

    public String getContent6() {
        return content6;
    }

    public void setContent6(String content6) {
        this.content6 = content6;
    }

    public String getContent7() {
        return content7;
    }

    public void setContent7(String content7) {
        this.content7 = content7;
    }

    public String getContent8() {
        return content8;
    }

    public void setContent8(String content8) {
        this.content8 = content8;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public void bind(Counselor counselor) {
        this.id = counselor.getId();
        this.name = counselor.getName();
        this.logo = counselor.getLogo();
        this.content1 = counselor.getContent1();
        this.content2 = counselor.getContent2();
        this.content3 = counselor.getContent3();
        this.content4 = counselor.getContent4();
        this.content5 = counselor.getContent5();
        this.content6 = counselor.getContent6();
        this.content7 = counselor.getContent7();
        this.content8 = counselor.getContent8();
        this.autograph = counselor.getAutograph();
        if (counselor.getSpeciality()!=null) {
            this.fields = counselor.getContent7().split(",");
        } else {
            this.fields = null;
        }
        this.phone = counselor.getPhone();
        this.speciality = counselor.getSpeciality();

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