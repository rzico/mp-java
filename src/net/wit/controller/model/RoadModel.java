package net.wit.controller.model;
import net.wit.entity.ProductCategory;
import net.wit.entity.Road;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoadModel extends BaseModel implements Serializable {
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

    public void bind(Road road) {
        this.id = road.getId();
        this.name = road.getName();
    }
    public static List<RoadModel> bindList(List<Road> roads) {
        List<RoadModel> ms = new ArrayList<RoadModel>();
        for (Road road:roads) {
          RoadModel m = new RoadModel();
          m.bind(road);
          ms.add(m);
        }
        return ms;
    }

}