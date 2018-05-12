package net.wit.controller.model;

import net.wit.entity.Dragon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DragonModel extends BaseModel implements Serializable {

    private Dragon.Type type;
    private Dragon.Status status;
    private String title;
    private Date createDate;
    private Integer orderCount;

    public Dragon.Type getType() {
        return type;
    }

    public void setType(Dragon.Type type) {
        this.type = type;
    }

    public Dragon.Status getStatus() {
        return status;
    }

    public void setStatus(Dragon.Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public void bind(Dragon dragon) {
        this.type = dragon.getType();
        this.title = dragon.getTitle();
        this.createDate = dragon.getCreateDate();
        this.status = dragon.getStatus();
        this.orderCount = dragon.getOrders().size();
    }

    public static List<DragonModel> bindList(List<Dragon> dragons) {
        List<DragonModel> ms = new ArrayList<DragonModel>();
        for (Dragon dragon:dragons) {
            DragonModel m = new DragonModel();
            m.bind(dragon);
            ms.add(m);
        }
        return ms;
    }

}