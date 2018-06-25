package net.wit.controller.model;
import net.wit.entity.Barrel;
import net.wit.entity.BarrelStock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BarrelStockModel extends BaseModel implements Serializable {
    private Long id;
    private String name;

    private Integer stock;

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

    public void bind(BarrelStock barrelStock) {
        this.id = barrelStock.getBarrel().getId();
        this.name = barrelStock.getBarrel().getName();
        this.stock = barrelStock.getStock();
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