package net.wit.controller.weex.model;

import net.wit.entity.ArticleCatalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//用户文集

public class MusicModel implements Serializable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}