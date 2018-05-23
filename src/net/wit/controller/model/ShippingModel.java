package net.wit.controller.model;

import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Shipping;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ShippingModel extends BaseModel implements Serializable {

    private Long id;

    /**  买方 id */
    private Long memberId;

    /**  买方头像 */
    private String logo;

    /**  买方昵称 */
    private String name;

    /**  卖方 id */
    private Long sellerId;

    /**  卖方头像 */
    private String sellerLogo;

    /**  卖方昵称 */
    private String sellerName;

    /**  订单号 */
    private String orderSn;

    /**  送货号 */
    private String sn;

    /**  日期 */
    private Date createDate;

    /**  状态描述 */
    private String statusDescr;

    /**  状态描述 */
    private String status;

    /**  支付方式 */
    private Order.PaymentMethod paymentMethod;

    /**  配送方式 */
    private Order.ShippingMethod shippingMethod;

    /**  支付方式 */
    private Order.PaymentStatus paymentStatus;

    /**  配送方式 */
    private Order.ShippingStatus shippingStatus;

    /** 地址 */
    private ReceiverModel receiver;

    /** 商品 */
    private List<ShippingItemModel> shippingItems;

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

    public ReceiverModel getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverModel receiver) {
        this.receiver = receiver;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Order.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Order.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Order.ShippingStatus getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Order.ShippingStatus shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public List<ShippingItemModel> getShippingItems() {
        return shippingItems;
    }

    public void setShippingItems(List<ShippingItemModel> shippingItems) {
        this.shippingItems = shippingItems;
    }

    public void bind(Shipping shipping) {
        this.id = shipping.getId();
        this.createDate = shipping.getCreateDate();
        this.sn = shipping.getSn();
        Order order = shipping.getOrder();
        this.orderSn = order.getSn();
        this.memberId = shipping.getMember().getId();
        this.logo = shipping.getMember().getLogo();
        this.name = shipping.getMember().displayName();
        this.sellerId = shipping.getSeller().getId();

        this.sellerLogo = shipping.getSeller().getLogo();
        this.sellerName = shipping.getSeller().topicName();

        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();

        this.shippingItems = ShippingItemModel.bindList(shipping.getShippingItems());

        this.paymentMethod = order.getPaymentMethod();
        this.shippingMethod = order.getShippingMethod();
        this.paymentStatus = order.getPaymentStatus();
        this.shippingStatus = order.getShippingStatus();

        this.receiver = new ReceiverModel();
        this.receiver.setAddress(order.getAddress());
        this.receiver.setAreaName(order.getAreaName());
        this.receiver.setConsignee(order.getConsignee());
        this.receiver.setPhone(order.getPhone());
        if (order.getLocation()!=null) {
            this.receiver.setLat(order.getLocation().getLat());
            this.receiver.setLng(order.getLocation().getLng());
        }

    }


    public void bindHeader(Order order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.sn = order.getSn();
        this.logo = order.getMember().getLogo();
        this.name = order.getMember().displayName();
        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();
        this.paymentMethod = order.getPaymentMethod();
        this.shippingMethod = order.getShippingMethod();

    }

}