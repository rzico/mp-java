package net.wit.controller.model;

import net.wit.entity.Payment;
import net.wit.entity.Refunds;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//文章编辑模板

public class RefundsModel extends BaseModel implements Serializable {

    /** 收款方 */
    private String nickName;
    /** 收款方头像 */
    private String logo;
    /** 摘要 */
    private String memo;
    /** 金额 */
    private BigDecimal amount;

    /** 交易时间 */
    private Date createDate;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void bind(Refunds refunds) {
        this.amount = refunds.getAmount();
        this.createDate = refunds.getCreateDate();
        this.memo = refunds.getMemo();
        this.logo = refunds.getPayee().getLogo();
        this.nickName = refunds.getPayee().displayName();
    }
}