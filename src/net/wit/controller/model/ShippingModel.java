package net.wit.controller.model;

import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingBarrel;

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

    /**  运费 */
    private BigDecimal freight;

    /**  工资 */
    private BigDecimal adminFreight;

    /**  配送点 */
    private Long shopId;

    /**  配送名称 */
    private String shopName;

    /**  送货员 */
    private Long adminId;

    /**  送货员姓名 */
    private String adminName;


    /**  买家留言 */
    private String orderMemo;

    /**  备注 */
    private String memo;

    /**  预约时间 */
    private Date hopeDate;


    /**  支付方式 */
    private String paymentMethod;

    /**  配送方式 */
    private String shippingMethod;

    /**  支付方式 */
    private Order.PaymentStatus paymentStatus;

    /**  配送方式 */
    private Order.ShippingStatus shippingStatus;

    /** 地址 */
    private ReceiverModel receiver;

    /** 商品 */
    private List<ShippingItemModel> shippingItems;

    /** 包装 */
    private List<ShippingBarrelModel> shippingBarrels;

    private ShippingTrackModel track;

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
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

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<ShippingBarrelModel> getShippingBarrels() {
        return shippingBarrels;
    }

    public void setShippingBarrels(List<ShippingBarrelModel> shippingBarrels) {
        this.shippingBarrels = shippingBarrels;
    }

    public String getOrderMemo() {
        return orderMemo;
    }

    public void setOrderMemo(String orderMemo) {
        this.orderMemo = orderMemo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getHopeDate() {
        return hopeDate;
    }

    public void setHopeDate(Date hopeDate) {
        this.hopeDate = hopeDate;
    }

    public ShippingTrackModel getTrack() {
        return track;
    }

    public void setTrack(ShippingTrackModel track) {
        this.track = track;
    }

    public BigDecimal getAdminFreight() {
        return adminFreight;
    }

    public void setAdminFreight(BigDecimal adminFreight) {
        this.adminFreight = adminFreight;
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
        this.shippingBarrels = ShippingBarrelModel.bindList(shipping.getShippingBarrels());

        this.paymentMethod = order.getPaymentPluginName();

        if (Order.ShippingMethod.shipping.equals(order.getShippingMethod())) {
            this.shippingMethod = "普通快递";
        } else
        if (Order.ShippingMethod.shipping.equals(order.getShippingMethod())) {
            this.shippingMethod = "同城配送";
        } else {
            this.shippingMethod = "电子卡包";
        }

        this.paymentStatus = order.getPaymentStatus();
        this.shippingStatus = order.getShippingStatus();

        this.freight = shipping.getFreight();
        this.adminFreight = shipping.getAdminFreight();

        this.receiver = new ReceiverModel();
        this.receiver.setAddress(shipping.getAddress());
        this.receiver.setAreaName(shipping.getAreaName());
        this.receiver.setConsignee(shipping.getConsignee());
        this.receiver.setPhone(shipping.getPhone());
        this.receiver.setLevel(shipping.getLevel());

        if (order.getLocation()!=null) {
            this.receiver.setLat(order.getLocation().getLat());
            this.receiver.setLng(order.getLocation().getLng());
        }

        if (shipping.getShop()!=null) {
            this.shopId = shipping.getShop().getId();
            this.shopName = shipping.getShop().getName();
        }

        if (shipping.getAdmin()!=null) {
            this.adminId = shipping.getAdmin().getId();
            this.adminName = shipping.getAdmin().realName();
        }

        this.memo = shipping.getMemo();
        this.orderMemo = order.getMemo();
        this.hopeDate = order.getHopeDate();


        ShippingTrackModel track = new ShippingTrackModel();
        track.setLng(0);
        track.setLat(0);
        if (shipping.getShippingMethod()==null) {
            track.setMethod("同城快送");
        } else {
            track.setMethod(shipping.getShippingMethod());
        }

        if (shipping.getAdmin()!=null && shipping.getAdmin().getMember()!=null) {
            Member shippingMember = shipping.getAdmin().getMember();
            if (shippingMember.getLocation()!=null) {
                track.setLng(shippingMember.getLocation().getLng());
                track.setLat(shippingMember.getLocation().getLng());
            }
            track.setMethod("同城配送");
            track.setName(shippingMember.realName());
            track.setStatus(shipping.getStatusDescr());
            track.setMobile(shippingMember.getMobile());
            track.setMemberId(shippingMember.getId());
        }

        this.track = track;

    }


    public void bindHeader(Shipping shipping) {

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

        this.paymentMethod = order.getPaymentPluginName();

        if (Order.ShippingMethod.shipping.equals(order.getShippingMethod())) {
            this.shippingMethod = "普通快递";
        } else
        if (Order.ShippingMethod.shipping.equals(order.getShippingMethod())) {
            this.shippingMethod = "同城配送";
        } else {
            this.shippingMethod = "电子卡包";
        }
        this.paymentStatus = order.getPaymentStatus();
        this.shippingStatus = order.getShippingStatus();

        this.freight = shipping.getFreight();
        this.adminFreight = shipping.getAdminFreight();

        this.receiver = new ReceiverModel();
        this.receiver.setAddress(shipping.getAddress());
        this.receiver.setAreaName(shipping.getAreaName());
        this.receiver.setConsignee(shipping.getConsignee());
        this.receiver.setPhone(shipping.getPhone());
        this.receiver.setLevel(shipping.getLevel());

        if (order.getLocation()!=null) {
            this.receiver.setLat(order.getLocation().getLat());
            this.receiver.setLng(order.getLocation().getLng());
        }

        if (shipping.getShop()!=null) {
            this.shopId = shipping.getShop().getId();
            this.shopName = shipping.getShop().getName();
        }

        if (shipping.getAdmin()!=null) {
            this.adminId = shipping.getAdmin().getId();
            this.adminName = shipping.getAdmin().getName();
        }

        this.memo = shipping.getMemo();
        this.orderMemo = order.getMemo();
        this.hopeDate = order.getHopeDate();


        ShippingTrackModel track = new ShippingTrackModel();
        track.setLng(0);
        track.setLat(0);
        if (shipping.getShippingMethod().equals(net.wit.entity.Order.ShippingMethod.cardbkg)) {
            track.setMethod("存入卡包");
        } else {
            track.setMethod("普通快递");
        }

        if (shipping.getAdmin()!=null && shipping.getAdmin().getMember()!=null) {
            Member shippingMember = shipping.getAdmin().getMember();
            if (shippingMember.getLocation()!=null) {
                track.setLng(shippingMember.getLocation().getLng());
                track.setLat(shippingMember.getLocation().getLng());
            }
            track.setMethod("同城配送");
            track.setName(shippingMember.realName());
            track.setStatus(shipping.getStatusDescr());
            track.setMobile(shippingMember.getMobile());
            track.setMemberId(shippingMember.getId());
        }

        this.track = track;
    }

}