package net.wit.controller.model;

import net.wit.entity.Order;
import net.wit.entity.Payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderModel extends BaseModel implements Serializable {

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

    /**  优惠券 */
    private String couponName;

    /**  支付方式 */
    private Order.PaymentMethod paymentMethod;

    /**  配送方式 */
    private Order.ShippingMethod shippingMethod;

    /**  商品合计 */
    private BigDecimal price;

    /**  订单金额 */
    private BigDecimal amount;

    /**  优惠券折扣 */
    private BigDecimal couponDiscount;

    /** 商品 */
    private List<OrderItemModel> orderItems;

    /** 日志 */
    private List<OrderLogModel> orderLogs;

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

    public List<OrderLogModel> getOrderLogs() {
        return orderLogs;
    }

    public void setOrderLogs(List<OrderLogModel> orderLogs) {
        this.orderLogs = orderLogs;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount;
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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Order.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Order.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order.ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(Order.ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

        this.orderLogs = OrderLogModel.bindList(order.getOrderLogs());

        this.amount = order.getAmount();
        this.price = order.getPrice();
        if (order.getCouponCode()!=null) {
            this.couponName = order.getCouponCode().getCoupon().getName();
        }
        this.couponDiscount = order.getCouponDiscount();
        this.paymentMethod = order.getPaymentMethod();
        this.shippingMethod = order.getShippingMethod();

    }


    public void bindHeader(Order order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.sn = order.getSn();
        this.logo = order.getMember().getLogo();
        this.name = order.getMember().getNickName();
        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();

        this.amount = order.getAmount();
        this.price = order.getPrice();
        if (order.getCouponCode()!=null) {
            this.couponName = order.getCouponCode().getCoupon().getName();
        }
        this.couponDiscount = order.getCouponDiscount();
        this.couponDiscount = order.getCouponDiscount();
        this.paymentMethod = order.getPaymentMethod();
        this.shippingMethod = order.getShippingMethod();

    }

}