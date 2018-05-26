package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Navigation;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class NavigationModel extends BaseModel implements Serializable {

    private Navigation.Type type;
    /** 名称 */
    private String name;
    /** 图标 */
    private String logo;
    /** 标签 */
    private Long tagId;
    /** 文章分类 */
    private Long articleCategoryId;
    /** 产品分类 */
    private Long productCategoryId;

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

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(Long articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void bind(Navigation navigation) {
        this.name = navigation.getName();
        this.logo = navigation.getLogo();
        this.type = navigation.getType();
        this.articleCategoryId = 0L;
        this.productCategoryId = 0L;
        this.tagId = 0L;
        Map<String,Long> data = JsonUtils.toObject(navigation.getExtend(),Map.class);
        if (data.containsKey("tag")) {
            this.tagId = data.get("tag");
        }
        if (data.containsKey("category")) {
            this.articleCategoryId = data.get("category");
            this.productCategoryId = data.get("category");
        }
    }
}