package net.wit.controller.weex.model;

import net.wit.entity.Article;
import net.wit.entity.Deposit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//文章编辑模板

public class DepositModel implements Serializable {

    /** 类型 */
    private Deposit.Type type;
    /** 摘要 */
    private String memo;
    /** 变动金额 */
    private BigDecimal credit;
    /** 余额 */
    private BigDecimal balance;
    /** 时间 */
    private Date createDate;

    public Deposit.Type getType() {
        return type;
    }

    public void setType(Deposit.Type type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}