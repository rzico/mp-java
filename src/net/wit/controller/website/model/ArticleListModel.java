package net.wit.controller.website.model;
import net.wit.entity.Article;
import net.wit.entity.ArticleOptions;

import java.io.Serializable;
import java.util.*;

//文章列表图

public class ArticleListModel implements Serializable {
    
    private Long id;
    /** 媒体类型 */
    private Article.MediaType mediaType;
    /** 创建时间 */
    private Date createDate;
    /** 作者 */
    private String author;
    /** 作者头像 */
    private String logo;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnial;
    /** 缩例图 */
    private List<String> images = new ArrayList<String>();
    /** 链接 */
    private String url;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;
    /** 谁可见 */
    private ArticleOptions.Authority authority;
    /** 标签名 */
    private Set<TagModel> tags = new HashSet<TagModel>();

    public void bind(Article article) {
        this.id = article.getId();
        this.mediaType = article.getMediaType();
        this.createDate = article.getCreateDate();
        this.author = article.getAuthor();
        this.logo = article.getMember().getLogo();
        this.title = article.getTitle();
        this.thumbnial = article.getThumbnial();
        this.review = article.getReview();
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.authority = article.getArticleOptions().getAuthority();
        this.tags = TagModel.bindSet(article.getTags());

        this.images = null;
        this.url = null;
    }
}