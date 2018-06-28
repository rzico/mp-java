package net.wit.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.wit.entity.ArticleReward;
import net.wit.entity.Deposit;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章编辑模板

public class DepositModel extends BaseModel implements Serializable {

    /** 类型 */
    private Deposit.Type type;
    /** 交易方头像 */
    private String logo;
    /** 交易方昵称 */
    private String nickName;
    /** 付款方式 */
    private String method;
    /** 摘要 */
    private String memo;
    /** 变动金额 */
    private BigDecimal amount;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public void bind(Deposit deposit) {
        this.type = deposit.getType();
        this.amount = deposit.getCredit().subtract(deposit.getDebit());
        this.createDate = deposit.getCreateDate();
        this.balance = deposit.getBalance();
        this.memo = deposit.getMemo();
        if (deposit.getPayment()!=null) {
            this.method = deposit.getPayment().getPaymentMethod();
        } else
        if (deposit.getRefunds()!=null) {
            this.method = deposit.getRefunds().getPaymentMethod();
        } else {
            this.method = "往来结算";
        }
        Member trade = deposit.getTrade();
        if (trade!=null) {
            this.logo = trade.getLogo();
            this.nickName = trade.displayName();
        } else {
            this.logo = deposit.getMember().getLogo();
            this.nickName = deposit.getMember().displayName();
        }
    }

    public static List<DepositModel> bindList(List<Deposit> deposits) {
        List<DepositModel> ms = new ArrayList<DepositModel>();
        for (Deposit deposit:deposits) {
            DepositModel m = new DepositModel();
            m.bind(deposit);
            ms.add(m);
        }
        return ms;
    }

}