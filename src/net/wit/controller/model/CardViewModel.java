package net.wit.controller.model;

import net.wit.entity.Card;
import net.wit.entity.Topic;
import net.wit.entity.TopicCard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardViewModel extends BaseModel implements Serializable {

    private Long id;
    /** 姓名 */
    private String name;
    /** 手机号 */
    private String mobile;
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
    /** 积分 */
    private Long point;
    /** 店铺 */
    private String shopName;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void bind(Card card) {
        this.id = card.getId();
        Topic topic = card.getOwner().getTopic();
        this.name = card.getName();
        this.mobile = card.getMobile();
        this.code = card.getCode();
        this.logo = card.getMembers().get(0).getLogo();
        this.balance = card.getBalance();
        this.status = card.getStatus();
        this.vip = card.getVip();
        this.color = topic.getTopicCard().getColor();
        this.background = topic.getTopicCard().getBackground();
        this.point = card.getPoint();
        if (card.getShop()!=null) {
            this.shopName = card.getShop().getName();
        } else {
            this.shopName = "";
        }

    }


    public static List<CardViewModel> bindList(List<Card> cards) {
        List<CardViewModel> ms = new ArrayList<CardViewModel>();
        for (Card card:cards) {
            CardViewModel m = new CardViewModel();
            m.bind(card);
            ms.add(m);
        }
        return ms;
    }

}