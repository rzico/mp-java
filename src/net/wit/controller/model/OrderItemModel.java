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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void bind(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.thumbnail = orderItem.getThumbnail();
        this.spec = orderItem.getSpec();
        this.name = orderItem.getName();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();
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

}