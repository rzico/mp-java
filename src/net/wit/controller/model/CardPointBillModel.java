package net.wit.controller.model;

import net.wit.entity.CardBill;
import net.wit.entity.CardPointBill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章编辑模板

public class CardPointBillModel extends BaseModel implements Serializable {

    /** 交易方头像 */
    private String logo;
    /** 摘要 */
    private String memo;
    /** 变动金额 */
    private Long amount;
    /** 余额 */
    private Long balance;
    /** 时间 */
    private Date createDate;

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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public void bind(CardPointBill cardBill) {
        this.amount = cardBill.getCredit()-cardBill.getDebit();
        this.logo = cardBill.getMember().getLogo();
        this.createDate = cardBill.getCreateDate();
        this.balance = cardBill.getBalance();
        this.memo = cardBill.getMemo();
    }

    public static List<CardPointBillModel> bindList(List<CardPointBill> cardBills) {
        List<CardPointBillModel> ms = new ArrayList<CardPointBillModel>();
        for (CardPointBill cardBill:cardBills) {
            CardPointBillModel m = new CardPointBillModel();
            m.bind(cardBill);
            ms.add(m);
        }
        return ms;
    }

}