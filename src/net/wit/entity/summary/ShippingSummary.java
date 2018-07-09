package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShippingSummary implements Serializable {

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

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}