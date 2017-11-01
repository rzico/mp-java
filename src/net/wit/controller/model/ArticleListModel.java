package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.ArticleOptions;

import java.io.Serializable;
import java.util.*;

//文章列表图

public class ArticleListModel implements Serializable {
    
    private Long id;
    /** 创建时间 */
    private Date createDate;
    /** 作者 */
    private String author;
    /** 作者头像 */
    private String logo;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnail;
    /** 内容短论 */
    private String content;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;
    /** 链接 */
    private String url;
    /** 标签名 */
    private List<TagModel> tags = new ArrayList<TagModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReview() {
        return review;
    }

    public void setReview(Long review) {
        this.review = review;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getLaud() {
        return laud;
    }

    public void setLaud(Long laud) {
        this.laud = laud;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public void bind(Article article) {
        this.id = article.getId();
        this.createDate = article.getCreateDate();
        this.author = article.getMember().getNickName();
        this.logo = article.getMember().getLogo();
        this.title = article.getTitle();
        this.thumbnail = article.getThumbnail();
        this.review = article.getReview();
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.tags = TagModel.bindList(article.getTags());
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        this.url = "http://"+bundle.getString("weixin.url")+"/article/#/t"+article.getTemplate().getId()+"?id="+article.getId();
    }
}