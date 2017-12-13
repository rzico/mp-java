package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class CashierModel extends BaseModel implements Serializable {

    private Long shopId;
    /** 今日收银 */
    private BigDecimal today;
    /** 昨天收银 */
    private BigDecimal yesterday;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getToday() {
        return today;
    }

    public void setToday(BigDecimal today) {
        this.today = today;
    }

    public BigDecimal getYesterday() {
        return yesterday;
    }

    public void setYesterday(BigDecimal yesterday) {
        this.yesterday = yesterday;
    }
}