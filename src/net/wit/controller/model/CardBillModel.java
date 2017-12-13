package net.wit.controller.model;

import net.wit.entity.CardBill;
import net.wit.entity.Deposit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章编辑模板

public class CardBillModel extends BaseModel implements Serializable {

    /** 类型 */
    private CardBill.Type type;
    /** 交易方头像 */
    private String logo;
    /** 摘要 */
    private String memo;
    /** 变动金额 */
    private BigDecimal amount;
    /** 余额 */
    private BigDecimal balance;
    /** 时间 */
    private Date createDate;

    public CardBill.Type getType() {
        return type;
    }

    public void setType(CardBill.Type type) {
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


    public void bind(CardBill cardBill) {
        this.type =  cardBill.getType();
        this.amount = cardBill.getCredit().subtract(cardBill.getDebit());
        this.logo = cardBill.getMember().getLogo();
        this.createDate = cardBill.getCreateDate();
        this.balance = cardBill.getBalance();
        this.memo = cardBill.getMemo();
    }

    public static List<CardBillModel> bindList(List<CardBill> cardBills) {
        List<CardBillModel> ms = new ArrayList<CardBillModel>();
        for (CardBill cardBill:cardBills) {
            CardBillModel m = new CardBillModel();
            m.bind(cardBill);
            ms.add(m);
        }
        return ms;
    }

}