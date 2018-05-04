package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.wit.entity.PayBill;
import net.wit.entity.Shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PayBillShopSummary implements Serializable {
    private Shop shop;
    //消费类型
    private PayBill.Type type;
    //结算方式
    private String paymentPluginId;
    //消费金额
    private BigDecimal amount;
    //优惠券折扣
    private BigDecimal couponDiscount;
    //会员卡消费
    private BigDecimal cardDiscount;
    //会员卡到账
    private BigDecimal cardAmount;
    //手续费
    private BigDecimal fee;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getPaymentPluginId() {
        return paymentPluginId;
    }

    public void setPaymentPluginId(String paymentPluginId) {
        this.paymentPluginId = paymentPluginId;
    }

    public PayBill.Type getType() {
        return type;
    }

    public void setType(PayBill.Type type) {
        this.type = type;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }
}