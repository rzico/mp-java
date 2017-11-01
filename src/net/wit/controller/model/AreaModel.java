package net.wit.controller.model;
import net.wit.entity.Area;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AreaModel implements Serializable {
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

    public void bind(Area area) {
        this.id = area.getId();
        this.name = area.getName();
    }

    public static Set<AreaModel> bindSet(Set<Area> areas) {
        Set<AreaModel> ms = new HashSet<AreaModel>();
        for (Area area:areas) {
          AreaModel m = new AreaModel();
          m.bind(area);
          ms.add(m);
        }
        return ms;
    }

}