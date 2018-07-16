package net.wit.controller.makey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.controller.model.BaseModel;
import net.wit.entity.Course;
import net.wit.entity.Organization;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class CourseModel extends BaseModel implements Serializable {
    
    private Long id;
    private String name;
    /** 缩例图 */
    private String thumbnail;

    /** 报名费 */
    private BigDecimal price;

    /** 报名数 */
    private Long signup;

    /** 阅读数 */
    private Long hits;

    /** 介绍 */
    private String content;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSignup() {
        return signup;
    }

    public void setSignup(Long signup) {
        this.signup = signup;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void bind(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.thumbnail = course.getThumbnail();
        this.hits = course.getHits();
        this.price = course.getPrice();
        this.signup = course.getSignup();
    }

    public static List<CourseModel> bindList(List<Course> courses) {
        List<CourseModel> ms = new ArrayList<CourseModel>();
        for (Course course:courses) {
            CourseModel m = new CourseModel();
            m.bind(course);
            ms.add(m);
        }
        return ms;
    }

}