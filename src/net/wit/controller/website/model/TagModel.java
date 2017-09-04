package net.wit.controller.website.model;
import net.wit.entity.Tag;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TagModel implements Serializable {
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

    public static Set<TagModel> bindSet(Set<Tag> tags) {
        Set<TagModel> ms = new HashSet<TagModel>();
        for (Tag tag:tags) {
          TagModel m = new TagModel();
          m.bind(tag);
          ms.add(m);
        }
        return ms;
    }

}