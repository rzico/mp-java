package net.wit.controller.model;

import net.wit.entity.Cart;
import net.wit.entity.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItemModel extends BaseModel implements Serializable {

    private Long id;

    private int quantity;

    private BigDecimal price;

    public void bind(CartItem cartItem) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }
}