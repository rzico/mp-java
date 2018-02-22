package net.wit.controller.model;

import net.wit.entity.Cart;
import net.wit.entity.CartItem;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItemModel extends BaseModel implements Serializable {

    private Long id;

    private int quantity;

    /** 缩例图 */
    private String thumbnail;

    /** 规格 */
    private String spec;

    /** 名称 */
    private String name;

    /** 销售价 */
    private BigDecimal price;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void bind(CartItem cartItem) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
        Product product = cartItem.getProduct();
        this.name = product.getName();
        this.spec = product.getSpec();
        this.thumbnail = product.getThumbnail();
    }
}