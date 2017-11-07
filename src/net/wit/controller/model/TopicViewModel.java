package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicViewModel implements Serializable {

    private Long id;
    /** 名称 */
    private String name;
    /** 收藏 */
    private int favorite;
    /** 点击 */
    private int hits;
    /** 粉丝 */
    private int fans;
    /** 关注 */
    private int follow;
    /** 文章 */
    private int article;
    /** 商品 */
    private int product;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 标签 */
    private List<TagModel> tags = new ArrayList<TagModel>();

    private List<ArticleCatalogModel> catalogs = new ArrayList<ArticleCatalogModel>();

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public List<ArticleCatalogModel> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<ArticleCatalogModel> catalogs) {
        this.catalogs = catalogs;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.autograph = member.getAutograph();
        this.fans = member.getFans().size();
        this.favorite = member.getFavorites().size();
        this.follow = member.getFollows().size();
        Topic topic = member.getTopic();
        if (topic!=null) {
            this.name = topic.getName();
            this.logo = member.getLogo();
            this.hits = topic.getHits().intValue();
        } else {
            this.name = member.getNickName();
            this.logo = member.getLogo();
            this.hits = 0;
        }
        this.tags = TagModel.bindList(member.getTags());
    }
}
