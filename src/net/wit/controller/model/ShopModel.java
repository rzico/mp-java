package net.wit.controller.model;
import net.wit.entity.Card;
import net.wit.entity.Enterprise;
import net.wit.entity.Shop;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShopModel implements Serializable {

    private Long id;
    /** 商户 */
    private String name;
    /** 地址 */
    private String addr;
    /** 头像 */
    private String logo;

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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void bind(Shop shop) {
        this.id = shop.getId();
        Enterprise enterprise = shop.getEnterprise();
        this.name = enterprise.getName();
        this.addr = shop.getAddress();
        this.logo = enterprise.getLogo();
        if (this.logo==null) {
            shop.getOwner().getLogo();
        }
    }
}