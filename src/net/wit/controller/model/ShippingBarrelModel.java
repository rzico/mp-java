package net.wit.controller.model;

import net.wit.entity.Order;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingBarrel;
import net.wit.entity.ShippingItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShippingBarrelModel extends BaseModel implements Serializable {

    private Long id;
    private Long barrelId;

    private String name;

    private String logo;

    /**  送出数量 */
    private Integer quantity;

    /**  回收数量 */
    private Integer returnQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBarrelId() {
        return barrelId;
    }

    public void setBarrelId(Long barrelId) {
        this.barrelId = barrelId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void bind(ShippingBarrel shippingBarrel) {
        this.id = shippingBarrel.getId();
        this.barrelId = shippingBarrel.getBarrel().getId();
        this.name = shippingBarrel.getName();
        this.logo = shippingBarrel.getBarrel().getLogo();
        this.quantity = shippingBarrel.getQuantity();
        this.returnQuantity = shippingBarrel.getReturnQuantity();
    }


    public static List<ShippingBarrelModel> bindList(List<ShippingBarrel> shippingBarrels) {
        List<ShippingBarrelModel> ms = new ArrayList<ShippingBarrelModel>();
        for (ShippingBarrel shippingBarrel:shippingBarrels) {
            ShippingBarrelModel m = new ShippingBarrelModel();
            m.bind(shippingBarrel);
            ms.add(m);
        }
        return ms;
    }

}