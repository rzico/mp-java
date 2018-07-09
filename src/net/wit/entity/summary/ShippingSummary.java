package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShippingSummary implements Serializable {

    private Long sellerId;
    private String sellerName;

     /** 销售金额 */
    private BigDecimal amount;
    /**  结算货款 */
    private BigDecimal cost;
    /**  配送费 */
    private BigDecimal shippingFreight;

    /**  送货工资 */
    private BigDecimal adminFreight;
    /**  楼层工资 */
    private BigDecimal levelFreight;

    /**  送货利润 */
    private BigDecimal profit;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }


    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}