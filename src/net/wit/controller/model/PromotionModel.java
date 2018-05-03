package net.wit.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.entity.Goods;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Promotion;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PromotionModel extends BaseModel implements Serializable {

    private Long id;

    /** 类型 */
    private Promotion.Type type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** 购买数量 */
    private Integer quantity;

    /** 赠送数量 */
    private Integer giftQuantity;

    /** 赠品 */
    private Long giftId;

    /** 赠品 */
    private String giftName;

    /*  使用条件*/
    private BigDecimal minimumPrice;

    /**  活动商品 */
    private Long goodsId;

    /**  活动商品 */
    private String goodsName;

    public Promotion.Type getType() {
        return type;
    }

    public void setType(Promotion.Type type) {
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

    public Long getGiftId() {
        return giftId;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void bind(Promotion promotion) {

        this.id = promotion.getId();
        this.type = promotion.getType();
        if (promotion.getGift()!=null) {
            this.giftId = promotion.getGift().getId();
            this.giftName = promotion.getGift().getName();
        }
        if (promotion.getGoods()!=null) {
            this.goodsId = promotion.getGoods().getId();
            this.goodsName = promotion.getGoods().product().getName();
        }
        this.quantity = promotion.getQuantity();
        this.giftQuantity = promotion.getGiftQuantity();
        this.minimumPrice = promotion.getMinimumPrice();

    }


    public static List<PromotionModel> bindList(List<Promotion> promotions) {
        List<PromotionModel> ms = new ArrayList<PromotionModel>();
        for (Promotion promotion:promotions) {
            PromotionModel m = new PromotionModel();
            m.bind(promotion);
            ms.add(m);
        }
        return ms;
    }


}