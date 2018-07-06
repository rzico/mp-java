package net.wit.controller.model;

import net.wit.entity.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderModel extends BaseModel implements Serializable {

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
    private String sn;

    /**  订单日期 */
    private Date createDate;

    /**  状态描述 */
    private String statusDescr;

    /**  状态描述 */
    private String status;

    /**  优惠券 */
    private String couponName;

    /**  推广人 */
    private String promoter;

    /**  支付方式 */
    private String paymentMethod;

    /**  配送方式 */
    private String shippingMethod;

    /**  支付方式 */
    private Order.PaymentStatus paymentStatus;

    /**  配送方式 */
    private Order.ShippingStatus shippingStatus;

    /**  买家留言 */
    private String memo;

    /**  预约时间 */
    private Date hopeDate;

    /**  商品合计 */
    private BigDecimal price;

    /**   运费 */
    private BigDecimal freight;

    /**  配送费用 */
    private BigDecimal shippingFreight;

    /**  配送工资 */
    private BigDecimal adminFreight;

    /**  楼层费 */
    private BigDecimal levelFreight;

    /**  商品数量 */
    private Integer quantity;

    /**  订单金额 */
    private BigDecimal amount;

    /**  结算货款 */
    private BigDecimal cost;

    /**  优惠券折扣 */
    private BigDecimal couponDiscount;

    /**  电子券抵扣 */
    private BigDecimal exchangeDiscount;

    /**  电子券张数 */
    private Integer exchangeQuantity;

    /**  电子券余票 */
    private Integer exchangeBalance;

    /**  应付金额 */
    private BigDecimal amountPayable;

    /**  实付金额 */
    private BigDecimal amountPaid;

    /**  积分支付 */
    private BigDecimal pointDiscount;

    /** 地址 */
    private ReceiverModel receiver;

    private ShippingTrackModel track;

    /** 商品 */
    private List<OrderItemModel> orderItems;

    /** 日志 */
    private List<OrderLogModel> orderLogs;

    /** 赠品 */
    private List<OrderItemModel> giftItems;

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

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
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

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getPointDiscount() {
        return pointDiscount;
    }

    public void setPointDiscount(BigDecimal pointDiscount) {
        this.pointDiscount = pointDiscount;
    }

    public BigDecimal getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(BigDecimal amountPayable) {
        this.amountPayable = amountPayable;
    }

    public ShippingTrackModel getTrack() {
        return track;
    }

    public void setTrack(ShippingTrackModel track) {
        this.track = track;
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

    public BigDecimal getExchangeDiscount() {
        return exchangeDiscount;
    }

    public void setExchangeDiscount(BigDecimal exchangeDiscount) {
        this.exchangeDiscount = exchangeDiscount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getExchangeQuantity() {
        return exchangeQuantity;
    }

    public void setExchangeQuantity(Integer exchangeQuantity) {
        this.exchangeQuantity = exchangeQuantity;
    }

    public Integer getExchangeBalance() {
        return exchangeBalance;
    }

    public void setExchangeBalance(Integer exchangeBalance) {
        this.exchangeBalance = exchangeBalance;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getShippingFreight() {
        return shippingFreight;
    }

    public void setShippingFreight(BigDecimal shippingFreight) {
        this.shippingFreight = shippingFreight;
    }

    public BigDecimal getAdminFreight() {
        return adminFreight;
    }

    public void setAdminFreight(BigDecimal adminFreight) {
        this.adminFreight = adminFreight;
    }

    public BigDecimal getLevelFreight() {
        return levelFreight;
    }

    public void setLevelFreight(BigDecimal levelFreight) {
        this.levelFreight = levelFreight;
    }

    public List<OrderItemModel> getGiftItems() {
        return giftItems;
    }

    public void setGiftItems(List<OrderItemModel> giftItems) {
        this.giftItems = giftItems;
    }

    public void bind(Order order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.sn = order.getSn();
        this.memberId = order.getMember().getId();
        this.logo = order.getMember().getLogo();
        this.name = order.getMember().displayName();
        this.sellerId = order.getSeller().getId();

        this.sellerLogo = order.getSeller().getLogo();
        this.sellerName = order.getSeller().topicName();

        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();

        this.orderItems = OrderItemModel.bindList(order.getOrderItems());

        this.orderLogs = OrderLogModel.bindList(order.getOrderLogs());

        this.amount = order.getAmount();
        this.amountPayable = order.getAmountPayable();

        this.amountPaid = order.getAmountPaid();
        this.price = order.getPrice();
        if (order.getCouponCode()!=null) {
            this.couponName = order.getCouponCode().getCoupon().getName();
        }

        this.couponDiscount = order.getCouponDiscount();
        this.pointDiscount = order.getPointDiscount();
        this.paymentMethod = order.getPaymentPluginName();

        if (Order.ShippingMethod.shipping.equals(order.getShippingMethod())) {
            this.shippingMethod = "普通快递";
        } else
        if (Order.ShippingMethod.pickup.equals(order.getShippingMethod())) {
            this.shippingMethod = "到店自提";
        } else
        if (Order.ShippingMethod.warehouse.equals(order.getShippingMethod())) {
            this.shippingMethod = "同城配送";
        } else {
            this.shippingMethod = "电子卡包";
        }

         this.paymentStatus = order.getPaymentStatus();
        this.shippingStatus = order.getShippingStatus();

        this.exchangeDiscount = order.getExchangeDiscount();
        this.exchangeQuantity = order.getExchangeQuantity();
        this.exchangeBalance = order.getExchangeBalance();

        this.quantity = order.getQuantity();

        this.receiver = new ReceiverModel();
        this.receiver.setAddress(order.getAddress());
        this.receiver.setAreaName(order.getAreaName());
        this.receiver.setConsignee(order.getConsignee());
        this.receiver.setPhone(order.getPhone());
        if (order.getLocation()!=null) {
            this.receiver.setLat(order.getLocation().getLat());
            this.receiver.setLng(order.getLocation().getLng());
        }
        this.promoter = "";

        if (order.getPromoter()!=null) {
           Member promoter = order.getPromoter();
           if (promoter.getName()!=null) {
               this.promoter = promoter.getName();
           } else {
               this.promoter = promoter.displayName();
           }
        }

        this.cost = order.getCost();
        this.freight = order.getFreight();
        this.shippingFreight = order.getShippingFreight();
        this.adminFreight = order.getAdminFreight();
        this.levelFreight = order.getLevelFreight();

        this.memo = order.getMemo();
        this.hopeDate = order.getHopeDate();

        this.giftItems = OrderItemModel.giftList(order.getOrderItems());

    }


    public void bindHeader(Order order) {
        this.id = order.getId();
        this.createDate = order.getCreateDate();
        this.sn = order.getSn();
        this.logo = order.getMember().getLogo();
        this.name = order.getMember().displayName();
        this.status = order.getStatus();
        this.statusDescr = order.getStatusDescr();

        this.amount = order.getAmount();
        this.amountPayable = order.getAmountPayable();

        this.amountPaid = order.getAmountPaid();

        this.price = order.getPrice();
        if (order.getCouponCode()!=null) {
            this.couponName = order.getCouponCode().getCoupon().getName();
        }
        this.couponDiscount = order.getCouponDiscount();
        this.pointDiscount = order.getPointDiscount();
        this.paymentMethod = order.getPaymentPluginName();


            if (Order.ShippingMethod.shipping.equals(order.getShippingMethod())) {
                this.shippingMethod = "普通快递";
            } else
            if (Order.ShippingMethod.pickup.equals(order.getShippingMethod())) {
                this.shippingMethod = "到店自提";
            } else
            if (Order.ShippingMethod.warehouse.equals(order.getShippingMethod())) {
                this.shippingMethod = "同城配送";
            } else {
                this.shippingMethod = "电子卡包";
            }

        this.cost = order.getCost();
        this.freight = order.getFreight();
        this.shippingFreight = order.getShippingFreight();
        this.adminFreight = order.getAdminFreight();

        this.quantity = order.getQuantity();

        this.exchangeDiscount = order.getExchangeDiscount();
        this.exchangeQuantity = order.getExchangeQuantity();
        this.exchangeBalance = order.getExchangeBalance();

        this.memo = order.getMemo();
        this.hopeDate = order.getHopeDate();


        this.giftItems = OrderItemModel.giftList(order.getOrderItems());


    }

}