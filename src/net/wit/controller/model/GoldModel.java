package net.wit.controller.model;

import net.wit.entity.Deposit;
import net.wit.entity.Gold;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章编辑模板

public class GoldModel extends BaseModel implements Serializable {

    /** 类型 */
    private Gold.Type type;
    /** 交易方头像 */
    private String logo;
    /** 交易方昵称 */
    private String nickName;
    /** 摘要 */
    private String memo;
    /** 变动数量 */
    private Long amount;
    /** 余额 */
    private Long balance;
    /** 时间 */
    private Date createDate;

    public Gold.Type getType() {
        return type;
    }

    public void setType(Gold.Type type) {
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public void bind(Gold gold) {
        this.type = gold.getType();
        this.amount = gold.getCredit()-gold.getDebit();
        this.logo = gold.getMember().getLogo();
        this.nickName = gold.getMember().getNickName();
        this.createDate = gold.getCreateDate();
        this.balance = gold.getBalance();
        this.memo = gold.getMemo();
    }

    public static List<GoldModel> bindList(List<Gold> deposits) {
        List<GoldModel> ms = new ArrayList<GoldModel>();
        for (Gold deposit:deposits) {
            GoldModel m = new GoldModel();
            m.bind(deposit);
            ms.add(m);
        }
        return ms;
    }

}