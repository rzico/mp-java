package net.wit.controller.model;

import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.entity.TopicCard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardViewModel extends BaseModel implements Serializable {

    private Long id;

    private Long memberId;

    /** 姓名 */
    private String name;
    /** 手机号 */
    private String mobile;
    /** 是否实名 */
    private Boolean bindName;
    /** 是否绑定 */
    private Boolean bindMobile;

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
    /** 角色 */
    private Card.Type type;
    /** 等级 */
    private Card.VIP vip;
    /** 余额 */
    private BigDecimal balance;
    /** 积分 */
    private Long point;
    /** 店铺 */
    private String shopName;
    /** 推荐人 */
    private String promoter;
    /** 股东比例 */
    private BigDecimal bonus;

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


    public Boolean getBindName() {
        return bindName;
    }

    public void setBindName(Boolean bindName) {
        this.bindName = bindName;
    }

    public Boolean getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(Boolean bindMobile) {
        this.bindMobile = bindMobile;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public Card.Type getType() {
        return type;
    }

    public void setType(Card.Type type) {
        this.type = type;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void bind(Card card) {
        this.id = card.getId();
        Topic topic = card.getOwner().getTopic();
        this.name = card.getName();
        this.mobile = card.getMobile();
        this.code = card.getCode();

        Member member = card.getMembers().get(0);
        this.logo = member.getLogo();

        this.type = card.getType();
        this.bindMobile = false;
        this.bindName = false;
        this.balance = card.getBalance().setScale(2,BigDecimal.ROUND_HALF_DOWN);
        this.bonus = card.getBonus();
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
        if (card.getPromoter()!=null) {
            this.promoter = card.getPromoter().displayName();
        } else {
            this.promoter = "";
        }
        if (card.getMembers().size()>0) {
            this.memberId = card.getMembers().get(0).getId();
        } else {
            this.memberId = 0L;
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