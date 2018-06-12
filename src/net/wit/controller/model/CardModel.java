package net.wit.controller.model;
import net.wit.entity.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardModel extends BaseModel implements Serializable {

    private Long id;

    private Long memberId;
    /** 商户 */
    private String name;
    /** 店铺 */
    private String shopName;
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
    /** 角色 */
    private Card.Type type;
    /** 余额 */
    private BigDecimal balance;
    /** 积分 */
    private Long point;

    private Card.PaymentMethod paymentMethod;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Card.Type getType() {
        return type;
    }

    public void setType(Card.Type type) {
        this.type = type;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Card.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Card.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void bind(Card card) {
        this.id = card.getId();
        Topic topic = card.getOwner().getTopic();
        this.name = topic.getName();
        this.code = card.getCode();
        this.logo = card.getOwner().getLogo();
        this.balance = card.getBalance().setScale(2,BigDecimal.ROUND_HALF_DOWN);
        this.status = card.getStatus();
        this.vip = card.getVip();
        this.type = card.getType();
        this.color = topic.getTopicCard().getColor();
        this.background = topic.getTopicCard().getBackground();
        if (card.getShop()!=null) {
            this.shopName = card.getShop().getName();
        } else {
            this.shopName = "";
        }
        this.point = card.getPoint();
        if (card.getMember()!=null) {
            this.memberId = card.getMember().getId();
        } else {
            if (card.getMembers().size() > 0) {
                this.memberId = card.getMembers().get(0).getId();
            } else {
                this.memberId = 0L;
            }
        }
        this.paymentMethod = card.getPaymentMethod();
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