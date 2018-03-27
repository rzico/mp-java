package net.wit.controller.model;

import net.sf.json.JSONObject;
import net.wit.entity.Category;
import net.wit.entity.Coupon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponActivityModel extends BaseModel implements Serializable {
    /** 0 无门槛 1 消费送 2 领卡送 */
    private Long atveType;
    /** 使用条件 */
    private BigDecimal atveMinPrice;
    /** 赠送数量  */
    private Long atveAmount;

    public Long getAtveType() {
        return atveType;
    }

    public void setAtveType(Long atveType) {
        this.atveType = atveType;
    }

    public BigDecimal getAtveMinPrice() {
        return atveMinPrice;
    }

    public void setAtveMinPrice(BigDecimal atveMinPrice) {
        this.atveMinPrice = atveMinPrice;
    }

    public Long getAtveAmount() {
        return atveAmount;
    }

    public void setAtveAmount(Long atveAmount) {
        this.atveAmount = atveAmount;
    }

    public void bind(String activity) {
        JSONObject jb = JSONObject.fromObject(activity);
        this.atveType = jb.getLong("type");
        this.atveMinPrice = new BigDecimal(jb.getDouble("min"));
        this.atveAmount = jb.getLong("amount");
    }

}