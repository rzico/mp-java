package net.wit.entity.eqxiuentity;

import java.util.List;

/**
 * Created by Eric on 2018/2/24.
 */
public class HList {
    private long id;
    private long sceneId;
    private int num;
    private String name;
    private Properties properties;
    private List<Elements> elements;
    private String price;
    private String isPaid;
    private String forms;
    private List<String> groups;
    private String extend;
    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setSceneId(long sceneId) {
        this.sceneId = sceneId;
    }
    public long getSceneId() {
        return sceneId;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public int getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public Properties getProperties() {
        return properties;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }
    public List<Elements> getElements() {
        return elements;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }
    public String getIsPaid() {
        return isPaid;
    }

    public void setForms(String forms) {
        this.forms = forms;
    }
    public String getForms() {
        return forms;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
    public List<String> getGroups() {
        return groups;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
    public String getExtend() {
        return extend;
    }
}
