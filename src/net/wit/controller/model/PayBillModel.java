package net.wit.controller.model;
import net.wit.entity.Card;
import net.wit.entity.Enterprise;
import net.wit.entity.PayBill;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayBillModel extends BaseModel implements Serializable {

    private Long id;
    /** 商户 */
    private BigDecimal amount;
    /** 卡号 */
    private BigDecimal noDiscount;
    /** 优惠券 */
    private String couponName;
    /** 优惠金额 */
    private BigDecimal couponDiscount;
    /** 会员卡抵扣 */
    private BigDecimal cardDiscount;
    /** 实付金额 */
    private BigDecimal effectiveAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNoDiscount() {
        return noDiscount;
    }

    public void setNoDiscount(BigDecimal noDiscount) {
        this.noDiscount = noDiscount;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public BigDecimal getCardDiscount() {
        return cardDiscount;
    }

    public void setCardDiscount(BigDecimal cardDiscount) {
        this.cardDiscount = cardDiscount;
    }

    public BigDecimal getEffectiveAmount() {
        return effectiveAmount;
    }

    public void setEffectiveAmount(BigDecimal effectiveAmount) {
        this.effectiveAmount = effectiveAmount;
    }

    public void bind(PayBill payBill) {
        this.id = payBill.getId();
        this.amount = payBill.getAmount();
        this.noDiscount = payBill.getNoDiscount();
        this.cardDiscount = payBill.getCardDiscount();
        this.couponDiscount = payBill.getCouponDiscount();
        this.effectiveAmount = payBill.getPayBillAmount();
        if (payBill.getCouponCode()!=null) {
            this.couponName = payBill.getCouponCode().getCoupon().getName();
        }
    }
}