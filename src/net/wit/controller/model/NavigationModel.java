package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Navigation;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NavigationModel extends BaseModel implements Serializable {

    private Navigation.Type type;
    /** 名称 */
    private String name;
    /** 图标 */
    private String logo;
    /** 路径 */
    private String url;
    /** 分类 */
    private Long id;

    public Navigation.Type getType() {
        return type;
    }

    public void setType(Navigation.Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void bind(Navigation navigation) {
        this.name = navigation.getName();
        this.logo = navigation.getLogo();
        this.type = navigation.getType();
        this.url = navigation.getUrl();
        this.id = 0L;
        if (navigation.getType().equals(Navigation.Type.article)) {
            this.id =  navigation.getArticleCatalogId();
        } else
        if (navigation.getType().equals(Navigation.Type.product)) {
            this.id = navigation.getProductCategoryId();
        } else {
            this.id = 0L;
        }
    }

    public static List<NavigationModel> bindList(List<Navigation> navigations) {
        List<NavigationModel> models = new ArrayList<>();
        for (Navigation navigation:navigations) {
            NavigationModel m = new NavigationModel();
            m.bind(navigation);
            models.add(m);
        }
        return models;
    }
}