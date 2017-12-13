package net.wit.controller.model;
import net.wit.entity.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardModel extends BaseModel implements Serializable {

    private Long id;
    /** 商户 */
    private String name;
    /** 卡号 */
    private String code;
    /** 头像 */
    private String logo;
    /** 背景色 */
    private TopicCard.Color color;
    /** 背景 */
    private String background;
    /** 状态 */
    private Card.Status status;
    /** 等级 */
    private Card.VIP vip;
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

    public TopicCard.Color getColor() {
        return color;
    }

    public void setColor(TopicCard.Color color) {
        this.color = color;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Card.Status getStatus() {
        return status;
    }

    public void setStatus(Card.Status status) {
        this.status = status;
    }

    public Card.VIP getVip() {
        return vip;
    }

    public void setVip(Card.VIP vip) {
        this.vip = vip;
    }

    public void bind(Card card) {
        this.id = card.getId();
        Topic topic = card.getOwner().getTopic();
        this.name = topic.getName();
        this.code = card.getCode();
        this.logo = card.getOwner().getLogo();
        this.balance = card.getBalance();
        this.status = card.getStatus();
        this.vip = card.getVip();
        this.color = topic.getTopicCard().getColor();
        this.background = topic.getTopicCard().getBackground();
    }


    public static List<CardModel> bindList(List<Card> cards) {
        List<CardModel> ms = new ArrayList<CardModel>();
        for (Card card:cards) {
            CardModel m = new CardModel();
            m.bind(card);
            ms.add(m);
        }
        return ms;
    }

}