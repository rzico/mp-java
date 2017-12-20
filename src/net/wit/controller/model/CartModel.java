package net.wit.controller.model;

import net.wit.entity.Cart;
import net.wit.entity.Goods;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartModel extends BaseModel implements Serializable {

    private Long id;

    private int quantity;

    private BigDecimal effectivePrice;

    public void bind(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.effectivePrice = cart.getEffectivePrice();
    }
}