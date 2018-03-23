package net.wit.controller.model;

import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponModel extends BaseModel implements Serializable {

    private Long id;
    /** 类型 */
    private Coupon.Type type;
    /** 使用范围 */
    private Coupon.Scope scope;
    /** logo */
    private String logo;
    /** 店名 */
    private String shopName;
    /** 名称 */
    private String name;
    /** 使用起始日期 */
    private Date beginDate;
    /** 使用结束日期 */
    private Date endDate;
    /** 颜色 */
    private Coupon.Color color;
    /** 优惠金额/折扣比例 */
    private BigDecimal amount;
    /** 发券数量 */
    private Long stock;
    /** 使用条件 0 代表无门槛 */
    private BigDecimal minimumPrice;

    /** 介绍 */
    private String introduction;
    /**  商品 */
    private String goodsName;
    /**  id  */
    private Long goodsId;
    /**  ownerId  */
    private Long ownerId;

    /**  type   */
    private CouponActivityModel activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Coupon.Type getType() {
        return type;
    }

    public void setType(Coupon.Type type) {
        this.type = type;
    }

    public Coupon.Scope getScope() {
        return scope;
    }

    public void setScope(Coupon.Scope scope) {
        this.scope = scope;
    }

    public Coupon.Color getColor() {
        return color;
    }

    public void setColor(Coupon.Color color) {
        this.color = color;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public CouponActivityModel getActivity() {
        return activity;
    }

    public void setActivity(CouponActivityModel activity) {
        this.activity = activity;
    }

    public void bind(Coupon coupon) {
        this.id = coupon.getId();
        this.color = coupon.getColor();
        this.scope = coupon.getScope();
        if (coupon.getType().equals(Coupon.Type.exchange)) {
            this.amount = coupon.getGoods().product().getPrice();
            if (coupon.getGoods()!=null) {
                this.goodsName = coupon.getGoods().product().getName();
                this.goodsId = coupon.getGoods().getId();
            }
        } else {
            this.amount = coupon.getAmount();
        }
        this.beginDate = coupon.getBeginDate();
        this.endDate = coupon.getEndDate();
        this.name = coupon.getName();
        this.minimumPrice = coupon.getMinimumPrice();
        this.introduction = coupon.getIntroduction();
        this.type = coupon.getType();
        this.stock = coupon.getStock();
        this.logo = coupon.getDistributor().getLogo();
        if (coupon.getDistributor().getTopic()!=null) {
            this.shopName = coupon.getDistributor().getTopic().getName();
        } else {
            this.shopName = "来自平台优惠券";
        }
        this.ownerId = coupon.getDistributor().getId();
        this.activity = new CouponActivityModel();
        if (coupon.getActivity()!=null) {
            this.activity.bind(coupon.getActivity());
        }
    }

    public static List<CouponModel> bindList(List<Coupon> coupons) {
        List<CouponModel> ms = new ArrayList<CouponModel>();
        for (Coupon coupon:coupons) {
            CouponModel m = new CouponModel();
            m.bind(coupon);
            ms.add(m);
        }
        return ms;
    }

}