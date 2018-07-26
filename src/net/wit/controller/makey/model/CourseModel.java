package net.wit.controller.makey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
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
    private String subTitle;

    /** 头街 */
    private String [] tagNames ;
    /** 缩例图 */
    private String thumbnail;

    /** 报名费 */
    private BigDecimal price;

    /** 报名数 */
    private Long signup;

    /** 阅读数 */
    private Long hits;

    /** 课程目标 */
    private String content1;

    /** 课程内容 */
    private String content2;


    /** 授课形式 */
     private String content3;

    /** 讲师头像 */
    private String contentLogo;

    /** 讲师简介 */
    private String content4;

    /** 课程大纲 */
    private String content5;

    /** 适合谁听 */
    private String content6;

    /** 您将获得 */
    private String content7;

    /** 往期回顾 图片json */
    private String images;

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

    public String getContentLogo() {
        return contentLogo;
    }

    public void setContentLogo(String contentLogo) {
        this.contentLogo = contentLogo;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public String[] getTagNames() {
        return tagNames;
    }

    public void setTagNames(String[] tagNames) {
        this.tagNames = tagNames;
    }

    public void bind(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.subTitle = course.getSubTitle();
        this.thumbnail = course.getThumbnail();
        this.hits = course.getHits();
        this.price = course.getPrice();
        this.signup = course.getSignup();
        this.contentLogo = course.getContentLogo();
        this.content1 = course.getContent1();
        this.content2 = course.getContent2();
        this.content3 = course.getContent3();
        this.content4 = course.getContent4();
        this.content5 = course.getContent5();
        this.content6 = course.getContent6();
        this.content7 = course.getContent7();
        this.tags = TagModel.bindList(course.getTags());
        if (course.getTagNames()!=null) {
            this.tagNames = course.getTagNames().split(",");
        } else {
            this.tagNames = null;
        }
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