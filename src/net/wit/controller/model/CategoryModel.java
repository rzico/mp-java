package net.wit.controller.model;
import net.wit.entity.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryModel extends BaseModel implements Serializable {
    private Long id;
    private String name;

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

    public void bind(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public static List<CategoryModel> bindList(List<Category> categories) {
        List<CategoryModel> ms = new ArrayList<CategoryModel>();
        for (Category category:categories) {
          CategoryModel m = new CategoryModel();
          m.bind(category);
          ms.add(m);
        }
        return ms;
    }

}