package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BarrelSummary implements Serializable {

    private Long sellerId;

    private String sellerName;

    private String barrelName;

     /** 配送-送出 */
    private Integer quantity;
     /** 配送-回收 */
    private Integer returnQuantity;

    /** 派单-送出 */
    private Integer sQuantity;
    /** 派单-回收 */
    private Integer sReturnQuantity;

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getBarrelName() {
        return barrelName;
    }

    public void setBarrelName(String barrelName) {
        this.barrelName = barrelName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getsQuantity() {
        return sQuantity;
    }

    public void setsQuantity(Integer sQuantity) {
        this.sQuantity = sQuantity;
    }

    public Integer getsReturnQuantity() {
        return sReturnQuantity;
    }

    public void setsReturnQuantity(Integer sReturnQuantity) {
        this.sReturnQuantity = sReturnQuantity;
    }

}