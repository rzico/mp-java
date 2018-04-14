package net.wit.controller.model;

import net.wit.entity.ArticleCatalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//用户文集

public class ArticleCatalogModel extends BaseModel implements Serializable {

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

    public void bind(ArticleCatalog catalog) {
        this.id = catalog.getId();
        this.name = catalog.getName();
        this.count = catalog.getArticles().size();
    }

    public static List<ArticleCatalogModel> bindList(List<ArticleCatalog> catalogs) {
        List<ArticleCatalogModel> fms = new ArrayList<ArticleCatalogModel>();
        for (ArticleCatalog catalog:catalogs) {
            ArticleCatalogModel model = new ArticleCatalogModel();
            model.bind(catalog);
            fms.add(model);
        }
        return fms;
    }
}