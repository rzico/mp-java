package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: Article
 * @Description:  文章
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "xm_article")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_article_sequence")
public class Article extends BaseEntity{

    private static final long serialVersionUID = 103L;

    public static enum MediaType{
        /** 图文 */
        image,
        /** 音频 */
        audio,
        /** 视频 */
        video
    };

    /** 类型 */
    @NotEmpty
    @Column(columnDefinition="int(11) comment '类型 {0:图文,1:音频,2:视频}'")
    private MediaType mediaType;

    /** 作者 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '作者'")
    private String author;

    /** 标题 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '标题'")
    private String title;

    /** 类别 */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '类别 {}'")
    private ArticleCategory articleCategory;

    /** 文集 */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '文集'")
    private ArticleCatalog articleCatalog;

    /** 背景音乐 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '背景音乐'")
    private String music;

    /** 文章内容 */
    @Lob
    @Column(columnDefinition="longtext comment '文章内容'")
    private String content;

    /** 收藏数 */
    @Min(0)
    @Column(columnDefinition="bigint(20) default 0 comment '收藏数'")
    private Long favorite;


    /** 评论数 */
    @Min(0)
    @Column(columnDefinition="bigint(20) default 0 comment '评论数'")
    private Long review;

    /** 阅读数 */
    @Min(0)
    @Column(columnDefinition="bigint(20) default 0 comment '阅读数'")
    private Long hits;

    /** 点赞数 */
    @Min(0)
    @Column(columnDefinition="bigint(20) default 0 comment '点赞数'")
    private Long laud;

    /** 安全密匙 */
    @Embedded
    private ArticleOptions articleOptions;

    /** 文章标签*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xm_article_tag")
    @OrderBy("order asc")
    private Set<Tag> tags = new HashSet<Tag>();

    /** 点赞者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleLaud> lauds = new HashSet<ArticleLaud>();

    /** 收藏者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleFavorite> favorites = new HashSet<ArticleFavorite>();

    /** 评论者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleReview> reviews = new HashSet<ArticleReview>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArticleCategory getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(ArticleCategory articleCategory) {
        this.articleCategory = articleCategory;
    }

    public ArticleCatalog getArticleCatalog() {
        return articleCatalog;
    }

    public void setArticleCatalog(ArticleCatalog articleCatalog) {
        this.articleCatalog = articleCatalog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFavorite() {
        return favorite;
    }

    public void setFavorite(Long favorite) {
        this.favorite = favorite;
    }

    public Long getReview() {
        return review;
    }

    public void setReview(Set<ArticleReview> review) {
        this.reviews = reviews;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<ArticleLaud> getLauds() {
        return lauds;
    }

    public void setLauds(Set<ArticleLaud> lauds) {
        this.lauds = lauds;
    }

    public Set<ArticleFavorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<ArticleFavorite> favorites) {
        this.favorites = favorites;
    }

    public Set<ArticleReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ArticleReview> reviews) {
        this.reviews = reviews;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public ArticleOptions getArticleOptions() {
        return articleOptions;
    }

    public void setArticleOptions(ArticleOptions articleOptions) {
        this.articleOptions = articleOptions;
    }
}
