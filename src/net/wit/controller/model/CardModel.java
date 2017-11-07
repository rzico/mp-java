package net.wit.controller.model;
import net.wit.entity.Card;
import net.wit.entity.Enterprise;

import java.io.Serializable;
import java.math.BigDecimal;

public class CardModel implements Serializable {

    private Long id;
    /** 商户 */
    private String name;
    /** 卡号 */
    private String code;
    /** 头像 */
    private String logo;
    /** 余额 */
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void bind(Card card) {
        this.id = card.getId();
        Enterprise enterprise = card.getShop().getEnterprise();
        this.name = enterprise.getName();
        this.code = card.getCode();
        this.logo = enterprise.getLogo();
        this.balance = card.getBalance();
    }
}