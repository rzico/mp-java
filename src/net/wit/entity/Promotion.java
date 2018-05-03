package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity-促销
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_promotion")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_promotion_sequence")
public class Promotion extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /**
     * 活动类型
     */
    public enum Type {

        /** 买N送N */
        give,

        /** 满A赠B */
        gift

    }

    /** 活动类型 */
    @Column(columnDefinition="int(11) not null comment '活动类型 {give:买N送N,gift:满A赠B}'")
    private Type type;

    /** 购买数量 */
    @NotNull
    @Min(1)
    @Column(nullable = false, updatable = false,columnDefinition="int(11) not null default 0 comment '购买数量'")
    private Integer quantity;

    /** 赠送数量 */
    @NotNull
    @Min(1)
    @Column(nullable = false, updatable = false,columnDefinition="int(11) not null default 0 comment '赠送数量'")
    private Integer giftQuantity;

    /** 赠品 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product gift;

    /*  使用条件*/
    @Min(0)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '使用条件'")
    private BigDecimal minimumPrice;

    /**  活动商品 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Goods goods;

    /** 业主,入驻商家时不能为空 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member owner;

    /** 是否删除 */
    @NotNull
    @Column(nullable = false,columnDefinition="bit not null comment '是否删除'")
    private Boolean deleted;

    /*删除前处理*/
    @PreRemove
    public void preRemove(){

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getGiftQuantity() {
        return giftQuantity;
    }

    public void setGiftQuantity(Integer giftQuantity) {
        this.giftQuantity = giftQuantity;
    }

    public Product getGift() {
        return gift;
    }

    public void setGift(Product gift) {
        this.gift = gift;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer calc(BigDecimal price,Integer quantity) {
//       if (getMinimumPrice().compareTo(BigDecimal.ZERO)>0 && getMinimumPrice().compareTo(price)>0) {
//           return 0;
//       }
       if (getQuantity()>0 && getQuantity()>quantity) {
           return 0;
       }
       if (getType().equals(Type.give)) {
           Integer q = quantity / getQuantity();
           return getGiftQuantity()*q;
       } else {
           return getGiftQuantity();
       }
    }
}
