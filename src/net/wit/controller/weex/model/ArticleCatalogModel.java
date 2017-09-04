package net.wit.controller.weex.model;

import java.io.Serializable;

//用户文集

public class ArticleCatalogModel implements Serializable {
    private Long id;
    private String name;
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}