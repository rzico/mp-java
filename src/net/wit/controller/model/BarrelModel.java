package net.wit.controller.model;
import net.wit.entity.Barrel;
import net.wit.entity.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BarrelModel extends BaseModel implements Serializable {
    private Long id;
    private String name;

    private Boolean show;
    private Integer quantity;
    private Integer returnQuantity;

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

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public void bind(Barrel barrel) {
        this.id = barrel.getId();
        this.name = barrel.getName();
        this.show = false;
        this.quantity = 0;
        this.returnQuantity = 0;
    }

    public static List<BarrelModel> bindList(List<Barrel> barrels) {
        List<BarrelModel> ms = new ArrayList<BarrelModel>();
        for (Barrel barrel:barrels) {
          BarrelModel m = new BarrelModel();
          m.bind(barrel);
          ms.add(m);
        }
        return ms;
    }

}