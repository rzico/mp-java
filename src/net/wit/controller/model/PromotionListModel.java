package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.Promotion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PromotionListModel extends BaseModel implements Serializable {

    private Long id;

    /** 类型 */
    private Promotion.Type type;
    /** 名称 */
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void bind(Promotion promotion) {

        this.id = promotion.getId();
        this.type = promotion.getType();
        if (this.type.equals(Promotion.Type.give)) {
            this.title = "买"+promotion.getQuantity()+"送"+promotion.getGiftQuantity();
        } else {
            this.title = "满"+promotion.getQuantity()+"赠"+promotion.getGift().getName();
        }

    }


    public static List<PromotionListModel> bindList(List<Promotion> promotions) {
        List<PromotionListModel> ms = new ArrayList<PromotionListModel>();
        for (Promotion promotion:promotions) {
            PromotionListModel m = new PromotionListModel();
            m.bind(promotion);
            ms.add(m);
        }
        return ms;
    }


}