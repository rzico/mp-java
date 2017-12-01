package net.wit.controller.model;

import net.wit.entity.Card;
import net.wit.entity.Topic;
import net.wit.entity.TopicCard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardActivityModel implements Serializable {
    private Long id;
    /**  充 */
    private BigDecimal amount;
    /**  送 */
    private BigDecimal present;
    /** 等级 */
    private Card.VIP vip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPresent() {
        return present;
    }

    public void setPresent(BigDecimal present) {
        this.present = present;
    }

    public Card.VIP getVip() {
        return vip;
    }

    public void setVip(Card.VIP vip) {
        this.vip = vip;
    }
}