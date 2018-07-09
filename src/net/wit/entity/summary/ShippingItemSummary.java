package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShippingItemSummary implements Serializable {

    private Long product;
    private String name;

    /**  销量 */
    private Integer quantity;
    /**  货款 */
    private BigDecimal cost;

    /**  合计货款 */
    private BigDecimal subTotal;
    /**  配送费 */
    private BigDecimal shippingFreight;
    /**  送货工资 */
    private BigDecimal adminFreight;
    /**  楼层工资 */
    private BigDecimal levelFreight;
    /**  送货利润 */
    private BigDecimal profit;


    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}