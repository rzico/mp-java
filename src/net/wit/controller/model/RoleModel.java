package net.wit.controller.model;
import net.wit.entity.Role;
import net.wit.entity.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleModel extends BaseModel implements Serializable {
    private Long id;
    private String name;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void bind(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }

    public static List<RoleModel> bindList(List<Role> roles) {
        List<RoleModel> ms = new ArrayList<RoleModel>();
        for (Role role:roles) {
          RoleModel m = new RoleModel();
          m.bind(role);
          ms.add(m);
        }
        return ms;
    }

}