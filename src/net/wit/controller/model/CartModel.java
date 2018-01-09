package net.wit.controller.model;

import net.wit.entity.Cart;
import net.wit.entity.CartItem;
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

    private Boolean isLowStock;

    List<CartItemModel> cartItems;

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

    public BigDecimal getEffectivePrice() {
        return effectivePrice;
    }

    public void setEffectivePrice(BigDecimal effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    public Boolean getLowStock() {
        return isLowStock;
    }

    public void setLowStock(Boolean lowStock) {
        isLowStock = lowStock;
    }

    public List<CartItemModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemModel> cartItems) {
        this.cartItems = cartItems;
    }

    public void bind(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.effectivePrice = cart.getEffectivePrice();
        this.isLowStock = cart.getIsLowStock();
        this.cartItems = new ArrayList<CartItemModel>();
        for (CartItem cartItem:cart.getCartItems()) {
            CartItemModel cartItemModel = new CartItemModel();
            cartItemModel.bind(cartItem);
            cartItems.add(cartItemModel);
        }
    }

    public void bindHeader(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.effectivePrice = cart.getEffectivePrice();
        this.isLowStock = cart.getIsLowStock();
    }

}