package net.wit.controller.model;

import net.wit.entity.Order;
import net.wit.entity.Shipping;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShippingListModel extends BaseModel implements Serializable {

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
    private String orderSn;

    /**  订单号 */
    private String sn;

    /**  订单日期 */
    private Date createDate;

    /**  预约时间 */
    private Date hopeDate;

    /**  状态描述 */
    private String statusDescr;

    /**  状态描述 */
    private Shipping.ShippingStatus status;

    /**  订单金额 */
    private Integer quantity;

    /**  收货人 */
    private String consignee;

    /**  分组名称 */
    private String groupName;

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

    public Shipping.ShippingStatus getStatus() {
        return status;
    }

    public void setStatus(Shipping.ShippingStatus status) {
        this.status = status;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getHopeDate() {
        return hopeDate;
    }

    public void setHopeDate(Date hopeDate) {
        this.hopeDate = hopeDate;
    }

    public void bind(Shipping shipping) {
        Order order = shipping.getOrder();
        this.id = shipping.getId();
        this.createDate = shipping.getCreateDate();
        this.sn = shipping.getSn();
        this.orderSn = order.getSn();
        this.logo = shipping.getMember().getLogo();
        this.name = shipping.getMember().displayName();
        this.memberId = shipping.getMember().getId();

        this.sellerLogo = shipping.getSeller().getLogo();
        this.sellerName = shipping.getSeller().topicName();

        this.status = shipping.getShippingStatus();
        this.statusDescr = shipping.getStatusDescr();

        this.shippingItems = ShippingItemModel.bindList(shipping.getShippingItems());

        this.quantity = shipping.getQuantity();
        this.consignee = shipping.getConsignee();

        this.groupName = shipping.getGroupName();

        this.hopeDate = shipping.getHopeDate();
    }

    public static List<ShippingListModel> bindList(List<Shipping> shippings) {
        List<ShippingListModel> ms = new ArrayList<ShippingListModel>();
        for (Shipping shipping:shippings) {
            ShippingListModel m = new ShippingListModel();
            m.bind(shipping);
            ms.add(m);
        }
        return ms;
    }

}