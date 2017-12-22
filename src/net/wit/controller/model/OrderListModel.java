package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.Order;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderListModel extends BaseModel implements Serializable {

    private Long id;

    /**  头方头像 */
    private String logo;

    /**  头方昵称 */
    private String name;

    /**  订单号 */
    private String sn;

    /**  订单日期 */
    private Date createDate;

    /**  状态描述 */
    private String statusDescr;

    /**  状态描述 */
    private String status;

    /** 商品 */
    private List<OrderItemModel> orderItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatusDescr() {
        return statusDescr;
    }

    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public void bind(Order order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.sn = order.getSn();
        this.logo = order.getMember().getLogo();
        this.name = order.getMember().getNickName();
        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();

        this.orderItems = OrderItemModel.bindList(order.getOrderItems());


    }


    public static List<OrderListModel> bindList(List<Order> orders) {
        List<OrderListModel> ms = new ArrayList<OrderListModel>();
        for (Order order:orders) {
            OrderListModel m = new OrderListModel();
            m.bind(order);
            ms.add(m);
        }
        return ms;
    }


}