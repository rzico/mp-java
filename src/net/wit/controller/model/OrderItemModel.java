package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderItemModel extends BaseModel implements Serializable {

    private Long id;

    /** 缩例图 */
    private String thumbnail;

    /** 规格 */
    private String spec;

    /** 名称 */
    private String name;

    /** 销售价 */
    private BigDecimal price;

    /** 数量 */
    private Integer quantity;

    /** 是否赠品 */
    private Boolean isGift;

    private PromotionModel promotion;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsGift() {
        return isGift;
    }

    public void setIsGift(Boolean gift) {
        isGift = gift;
    }

    public PromotionModel getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionModel promotion) {
        this.promotion = promotion;
    }

    public void bind(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.thumbnail = orderItem.getThumbnail();
        this.spec = orderItem.getSpec();
        this.name = orderItem.getName();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();

        this.isGift = orderItem.getIsGift();
        this.promotion = new PromotionModel();
        if (orderItem.getPromotion()!=null) {
            this.promotion.bind(orderItem.getPromotion());
        }

    }


    public static List<OrderItemModel> bindList(List<OrderItem> orderItems) {
        List<OrderItemModel> ms = new ArrayList<OrderItemModel>();
        for (OrderItem orderItem:orderItems) {
            OrderItemModel m = new OrderItemModel();
            m.bind(orderItem);
            ms.add(m);
        }
        return ms;
    }

    public static List<OrderItemModel> giftList(List<OrderItem> orderItems) {
        List<OrderItemModel> ms = new ArrayList<OrderItemModel>();
        for (OrderItem orderItem:orderItems) {
            if (orderItem.getPrice().compareTo(BigDecimal.ZERO)==0) {
                OrderItemModel m = new OrderItemModel();
                m.bind(orderItem);
                ms.add(m);
            }
        }
        return ms;
    }

}