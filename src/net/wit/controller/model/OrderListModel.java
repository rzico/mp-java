package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.Order;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderListModel extends BaseModel implements Serializable {

    private Long id;


    /**  买方 id */
    private Long memberId;

    /**  头方头像 */
    private String logo;

    /**  头方昵称 */
    private String name;

    /**  卖方 id */
    private Long sellerId;

    /**  卖方头像 */
    private String sellerLogo;

    /**  卖方昵称 */
    private String sellerName;

    /**  订单号 */
    private String sn;

    /**  订单日期 */
    private Date createDate;

    /**  状态描述 */
    private String statusDescr;

    /**  状态描述 */
    private String status;

    /**  订单金额 */
    private Integer quantity;

    /**  订单金额 */
    private BigDecimal amount;

    /**  收货人 */
    private String consignee;

    /**  本单收益 */
    private BigDecimal rebate;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerLogo() {
        return sellerLogo;
    }

    public void setSellerLogo(String sellerLogo) {
        this.sellerLogo = sellerLogo;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public void bind(Order order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.sn = order.getSn();
        this.logo = order.getMember().getLogo();
        this.name = order.getMember().displayName();
        this.memberId = order.getMember().getId();

        this.sellerId = order.getSeller().getId();
        if (order.getSeller().getTopic()!=null) {
            this.sellerLogo = order.getSeller().getTopic().getLogo();
            this.sellerName = order.getSeller().getTopic().getName();
        } else {
            this.sellerLogo = order.getSeller().getLogo();
            this.sellerName = order.getSeller().displayName();
        }

        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();

        this.orderItems = OrderItemModel.bindList(order.getOrderItems());

        this.amount = order.getAmount();
        this.quantity = order.getQuantity();
        this.consignee = order.getConsignee();
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

    public static List<OrderListModel> bindAndRebate(List<Order> orders) {
        List<OrderListModel> ms = new ArrayList<OrderListModel>();
        for (Order order:orders) {
            OrderListModel m = new OrderListModel();
            m.bind(order);
            m.setRebate(order.getRebate());
            ms.add(m);
        }
        return ms;
    }


}