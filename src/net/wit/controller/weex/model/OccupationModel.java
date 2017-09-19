package net.wit.controller.weex.model;
import net.wit.entity.Occupation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class OccupationModel implements Serializable {
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

    public static Set<OccupationModel> bindSet(Set<Occupation> occupations) {
        Set<OccupationModel> ms = new HashSet<OccupationModel>();
        for (Occupation occupation:occupations) {
          OccupationModel m = new OccupationModel();
          m.bind(occupation);
          ms.add(m);
        }
        return ms;
    }

}