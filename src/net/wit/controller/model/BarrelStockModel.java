package net.wit.controller.model;
import net.wit.entity.Barrel;
import net.wit.entity.BarrelStock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BarrelStockModel extends BaseModel implements Serializable {
    private Long id;
    private String name;

    private String logo;

    private Integer stock;

    private Integer mortgage ;

    private Integer borrow ;

    private BigDecimal pledge ;

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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getMortgage() {
        return mortgage;
    }

    public void setMortgage(Integer mortgage) {
        this.mortgage = mortgage;
    }

    public Integer getBorrow() {
        return borrow;
    }

    public void setBorrow(Integer borrow) {
        this.borrow = borrow;
    }

    public BigDecimal getPledge() {
        return pledge;
    }

    public void setPledge(BigDecimal pledge) {
        this.pledge = pledge;
    }

    public void bind(BarrelStock barrelStock) {
        this.id = barrelStock.getBarrel().getId();
        this.name = barrelStock.getBarrel().getName();
        this.logo = barrelStock.getBarrel().getLogo();
        this.stock = barrelStock.getStock();
        this.borrow = barrelStock.getBorrow();
        this.mortgage = barrelStock.getMortgage();
        this.pledge = barrelStock.getPledge();
    }

    public static List<BarrelStockModel> bindList(List<BarrelStock> barrels) {
        List<BarrelStockModel> ms = new ArrayList<BarrelStockModel>();
        for (BarrelStock barrel:barrels) {
          BarrelStockModel m = new BarrelStockModel();
          m.bind(barrel);
          ms.add(m);
        }
        return ms;
    }

}