package net.wit.controller.weex.model;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WalletModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 余额 */
    private BigDecimal balance;
    /** 银行卡 */
    private String bankinfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBankinfo() {
        return bankinfo;
    }

    public void setBankinfo(String bankinfo) {
        this.bankinfo = bankinfo;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.balance = member.getBalance();
    }
}