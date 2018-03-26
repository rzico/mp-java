package net.wit.controller.model;

import net.wit.entity.GoldProduct;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章编辑模板

public class GoldProductModel extends BaseModel implements Serializable {

    private Long id;
    /**  标题 */
    private String title;
    /**  售价 */
    private BigDecimal price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void bind(GoldProduct goldProduct) {
        this.id = goldProduct.getId();
        this.title = goldProduct.getTitle();
        this.price = goldProduct.getPrice();
    }

    public static List<GoldProductModel> bindList(List<GoldProduct> deposits) {
        List<GoldProductModel> ms = new ArrayList<GoldProductModel>();
        for (GoldProduct deposit:deposits) {
            GoldProductModel m = new GoldProductModel();
            m.bind(deposit);
            ms.add(m);
        }
        return ms;
    }

}