package net.wit.controller.model;
import net.wit.entity.Occupation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OccupationModel extends BaseModel implements Serializable {

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

    public void bind(Occupation occupation) {
        this.id = occupation.getId();
        this.name = occupation.getName();
    }

    public static List<OccupationModel> bindList(List<Occupation> occupations) {
        List<OccupationModel> ms = new ArrayList<OccupationModel>();
        for (Occupation occupation:occupations) {
          OccupationModel m = new OccupationModel();
          m.bind(occupation);
          ms.add(m);
        }
        return ms;
    }

}