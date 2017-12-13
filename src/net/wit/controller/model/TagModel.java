package net.wit.controller.model;
import net.wit.entity.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TagModel extends BaseModel implements Serializable {
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

    public void bind(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public static List<TagModel> bindList(List<Tag> tags) {
        List<TagModel> ms = new ArrayList<TagModel>();
        for (Tag tag:tags) {
          TagModel m = new TagModel();
          m.bind(tag);
          ms.add(m);
        }
        return ms;
    }

}