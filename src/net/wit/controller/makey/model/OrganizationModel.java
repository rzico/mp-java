package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.MemberAttribute;
import net.wit.entity.Organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class OrganizationModel extends BaseModel implements Serializable {
    
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void bind(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
    }

    public static List<OrganizationModel> bindList(List<Organization> organizations) {
        List<OrganizationModel> ms = new ArrayList<OrganizationModel>();
        for (Organization organization:organizations) {
            OrganizationModel m = new OrganizationModel();
            m.bind(organization);
            ms.add(m);
        }
        return ms;
    }

}