package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.wit.entity.Member;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderSummary implements Serializable {

     /** 营业额 */
    private BigDecimal amount;

    /**  商品合计 */
    private BigDecimal price;

    /**  佣金 */
    private BigDecimal fee;

    /**  运费 */
    private BigDecimal freight;

    /**  积分抵扣 */
    private BigDecimal pointDiscount;

    /**  优惠折扣 */
    private BigDecimal couponDiscount;

    /**  电子券抵扣 */
    private BigDecimal exchangeDiscount;

    /**  调价 */
    private BigDecimal offsetAmount;

    /**  付款金额 */
    private BigDecimal AmountPayable;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
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

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public BigDecimal getExchangeDiscount() {
        return exchangeDiscount;
    }

    public void setExchangeDiscount(BigDecimal exchangeDiscount) {
        this.exchangeDiscount = exchangeDiscount;
    }

    public BigDecimal getOffsetAmount() {
        return offsetAmount;
    }

    public void setOffsetAmount(BigDecimal offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmountPayable() {
        return AmountPayable;
    }

    public void setAmountPayable(BigDecimal amountPayable) {
        AmountPayable = amountPayable;
    }
}