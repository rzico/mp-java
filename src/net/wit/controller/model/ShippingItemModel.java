package net.wit.controller.model;

import net.wit.entity.OrderItem;
import net.wit.entity.ShippingItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShippingItemModel extends BaseModel implements Serializable {

    private Long id;

    /** 缩例图 */
    private String thumbnail;

    /** 规格 */
    private String spec;

    /** 名称 */
    private String name;
    /** 数量 */
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void bind(ShippingItem shippingItem) {
        this.id = shippingItem.getId();
        this.thumbnail = shippingItem.getThumbnail();
        this.spec = shippingItem.getSpec();
        this.name = shippingItem.getName();
        this.quantity = shippingItem.getQuantity();
    }


    public static List<ShippingItemModel> bindList(List<ShippingItem> shippingItems) {
        List<ShippingItemModel> ms = new ArrayList<ShippingItemModel>();
        for (ShippingItem shippingItem:shippingItems) {
            ShippingItemModel m = new ShippingItemModel();
            m.bind(shippingItem);
            ms.add(m);
        }
        return ms;
    }

}