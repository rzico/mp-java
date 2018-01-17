package net.wit.controller.model;
import net.wit.entity.Distribution;
import net.wit.entity.ProductCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DistributionViewModel extends BaseModel implements Serializable {
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

    public void bind(Distribution distribution) {
        this.id = distribution.getId();
        this.name = distribution.getName();
    }

}