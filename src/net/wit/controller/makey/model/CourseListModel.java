package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Course;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class CourseListModel extends BaseModel implements Serializable {

    private Long id;
    private String name;
    private String subTitle;
    /** 缩例图 */
    private String thumbnail;

    /** 报名费 */
    private BigDecimal price;

    /** 报名数 */
    private Long signup;

    /** 阅读数 */
    private Long hits;

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

    public void bind(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.thumbnail = course.getThumbnail();
        this.hits = course.getHits();
        this.price = course.getPrice();
        this.signup = course.getSignup();

        this.tags = TagModel.bindList(course.getTags());

    }

    public static List<CourseListModel> bindList(List<Course> courses) {
        List<CourseListModel> ms = new ArrayList<CourseListModel>();
        for (Course course:courses) {
            CourseListModel m = new CourseListModel();
            m.bind(course);
            ms.add(m);
        }
        return ms;
    }

}