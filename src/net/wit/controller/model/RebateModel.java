package net.wit.controller.model;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.summary.RebateSummary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RebateModel extends BaseModel implements Serializable {
    /** 昵称 */
    private String nickName;

    /** 头像 */
    private String logo;

    /**
     * 奖励金
     */
    private BigDecimal rebate;

    /**
     * 我的人脉
     */
    private long contacts;

    /**
     * 待维护人脉
     */
    private long invalid;

    /**
     * 当前号数
     */
    private long ranking;


    /** 是否代理商 none  operate agent */
    private String agentType;

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public long getContacts() {
        return contacts;
    }

    public void setContacts(long contacts) {
        this.contacts = contacts;
    }

    public long getInvalid() {
        return invalid;
    }

    public void setInvalid(long invalid) {
        this.invalid = invalid;
    }

    public long getRanking() {
        return ranking;
    }

    public void setRanking(long ranking) {
        this.ranking = ranking;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
}