package net.wit.controller.model;
import net.wit.entity.Friends;
import net.wit.entity.Member;
import net.wit.entity.Template;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class TopicViewModel extends BaseModel implements Serializable {

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
    /** 是否关注 */
    private Boolean followed;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 场景图 */
    private String thumbnail;
    /** 地址 */
    private String url;
    /** 状态 */
    private Friends.Status friendStatus;
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

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public Friends.Status getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(Friends.Status friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void bind(Member member, Member shareUser) {
        this.id = member.getId();
        this.autograph = member.getAutograph();
        if (this.autograph==null) {
            this.autograph = "TA好像忘记签名了";
        }
        this.fans = member.getFans().size();
        this.favorite = member.getFavorites().size();
        this.follow = member.getFollows().size();
        this.logo = member.getLogo();
        Template template = null;
        Topic topic = member.getTopic();
        if (topic!=null) {
            this.name = topic.getName();
            this.thumbnail = topic.getLogo();
            this.hits = topic.getHits().intValue();
            template = topic.getTemplate();
        } else {
            this.name = member.displayName();
            this.thumbnail = member.getLogo();
            this.hits = 0;
        }
        this.tags = TagModel.bindList(member.getTags());
        this.followed = false;
        this.setCatalogs(new ArrayList<ArticleCatalogModel>());
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if (shareUser==null) {
            shareUser = member;
        }
        if (template==null) {
            this.url = "http://" + bundle.getString("weixin.url") + "/#/c1001?id=" + member.getId() + "&xuid=" + shareUser.getId();
        } else {
            this.url = "http://" + bundle.getString("weixin.url") + "/#/c"+template.getSn()+"?id=" + member.getId() + "&xuid=" + shareUser.getId();
        }
    }
}
