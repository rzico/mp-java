package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Course;
import net.wit.entity.CourseOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章列表图

public class CourseOrderModel extends BaseModel implements Serializable {
    
    private Long id;

    private Date createDate;
    private String name;
    private String subTitle;
    /** 缩例图 */
    private String thumbnail;

    /** 报名费 */
    private BigDecimal price;

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void bind(CourseOrder courseOrder) {
        this.id = courseOrder.getId();
        this.name = courseOrder.getName();
        this.subTitle = courseOrder.getCourse().getSubTitle();
        this.createDate = courseOrder.getCreateDate();
        this.thumbnail = courseOrder.getThumbnail();
        this.price = courseOrder.getPrice();


    }

    public static List<CourseOrderModel> bindList(List<CourseOrder> courseOrders) {
        List<CourseOrderModel> ms = new ArrayList<CourseOrderModel>();
        for (CourseOrder course:courseOrders) {
            CourseOrderModel m = new CourseOrderModel();
            m.bind(course);
            ms.add(m);
        }
        return ms;
    }

}