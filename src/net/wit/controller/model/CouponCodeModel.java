package net.wit.controller.model;

import net.wit.entity.*;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CouponCodeModel extends BaseModel implements Serializable {

    private Long id;
    /** 商户 */
    private String name;
    /** 头像 */
    private String logo;
    /** 背景色 */
    private Coupon.Color color;
    /** 名称 */
    private String couponName;
    /** 介绍 */
    private String introduction;
    /** 范围 */
    private String descr;
    /** 金额 */
    private String amount;

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

     public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Coupon.Color getColor() {
        return color;
    }

    public void setColor(Coupon.Color color) {
        this.color = color;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void bind(CouponCode couponCode) {
        this.id = couponCode.getId();
        Coupon coupon = couponCode.getCoupon();
        Member owner = coupon.getDistributor();
        if (owner.getTopic()!=null) {
            this.name = owner.getTopic().getName();
            this.logo = owner.getTopic().getLogo();
        } else {
            this.name = owner.getNickName();
            this.logo = owner.getLogo();
        }
        String sc = "全场";
        if (coupon.getType().equals(Coupon.Scope.mall)) {
            sc = "商城购买";
        } else
        if (coupon.getType().equals(Coupon.Scope.shop)) {
            sc = "到店消费";
        } else {
            sc = "全场";
        }
        this.couponName = sc+coupon.getName();
        this.introduction = coupon.getIntroduction();
        if (this.descr==null) {
            if (coupon.getEndDate()==null) {
                this.descr = "永久有效,解释权归商家所有";
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                this.descr = "有效期:"+formatter.format(coupon.getBeginDate())+"至"+formatter.format(coupon.getEndDate());

            }
        }
        this.color = coupon.getColor();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        if (coupon.getType().equals(Coupon.Type.discount)) {
            this.amount = nf.format(coupon.getAmount())+"折";
        } else {
            this.amount = nf.format(coupon.getAmount())+"元";
        }
    }


    public static List<CouponCodeModel> bindList(List<CouponCode> codes) {
        List<CouponCodeModel> ms = new ArrayList<CouponCodeModel>();
        for (CouponCode couponCode:codes) {
            CouponCodeModel m = new CouponCodeModel();
            m.bind(couponCode);
            ms.add(m);
        }
        return ms;
    }

}