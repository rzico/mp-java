package net.wit.controller.model;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RebateModel extends BaseModel implements Serializable {

    /**
     * 奖励金
     */
    private BigDecimal rebate;
    /**
     * 我的人脉
     */
    private int contacts;
    /**
     * 待维护人脉
     */
    private int invalid;

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public int getContacts() {
        return contacts;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }

    public int getInvalid() {
        return invalid;
    }

    public void setInvalid(int invalid) {
        this.invalid = invalid;
    }
}