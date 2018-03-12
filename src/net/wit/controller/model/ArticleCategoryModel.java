package net.wit.controller.model;

import net.wit.entity.ArticleCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章分类

public class ArticleCategoryModel extends BaseModel implements Serializable {
    private Long id;
    private String name;
    private String thumbnail;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void bind(ArticleCategory catalog) {
        this.id = catalog.getId();
        this.name = catalog.getName();
        this.thumbnail = catalog.getThumbnail();
    }

    public static List<ArticleCategoryModel> bindList(List<ArticleCategory> catalogs) {
        List<ArticleCategoryModel> fms = new ArrayList<ArticleCategoryModel>();
        for (ArticleCategory catalog:catalogs) {
            ArticleCategoryModel model = new ArticleCategoryModel();
            model.bind(catalog);
            fms.add(model);
        }
        return fms;
    }
}