package net.wit.controller.model;
import net.wit.entity.Area;
import net.wit.entity.ProductCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductCategoryModel extends BaseModel implements Serializable {
    private Long id;
    private String name;
    private String thumbnail;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void bind(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
        this.count = productCategory.getProducts().size();
        this.thumbnail = productCategory.getThumbnail();
    }
    public static List<ProductCategoryModel> bindList(List<ProductCategory> productCategories) {
        List<ProductCategoryModel> ms = new ArrayList<ProductCategoryModel>();
        for (ProductCategory productCategory:productCategories) {
          ProductCategoryModel m = new ProductCategoryModel();
          m.bind(productCategory);
          ms.add(m);
        }
        return ms;
    }

}