package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.wit.MapEntity;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @ClassName: Article
 * @Description:  文章
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_sequence")
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
    @Column(columnDefinition="int(11) comment '类型 {image:图文,audio:音频,video:视频}'")
    private MediaType mediaType;

    /** 作者 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '作者'")
    private String author;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /** 标题 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '标题'")
    private String title;

    /** 标题模板 */
    @Embedded
    private ArticleTitle articleTitle;

    /** 缩例图 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '缩例图'")
    private String thumbnial;

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

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit comment '是否删除'")
    private Boolean deleted;

    /** 是否草稿 */
    @NotNull
    @Column(columnDefinition="bit comment '是否草稿'")
    private Boolean isDraft;

    /** 安全密匙 */
    @Embedded
    private ArticleOptions articleOptions;

    /** 定位 */
    @Embedded
    private Location location;

    /** 所在地 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    /** 模板 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '模板'")
    private Template template;

    /** 文章标签*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "wx_article_tag")
    @OrderBy("order asc")
    private List<Tag> tags = new ArrayList<Tag>();

    /** 点赞者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleLaud> lauds = new HashSet<ArticleLaud>();

    /** 收藏者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleFavorite> favorites = new HashSet<ArticleFavorite>();

    /** 评论者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleReview> reviews = new HashSet<ArticleReview>();

    /** 商品*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ArticleProduct> products = new HashSet<ArticleProduct>();

    /** 投票项 */
    @ElementCollection
    @CollectionTable(name = "wx_article_vote_option")
    private List<String> options = new ArrayList<String>();

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

    public ArticleTitle getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(ArticleTitle articleTitle) {
        this.articleTitle = articleTitle;
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

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
        isDraft = draft;
    }

    public List<Tag> getTags() {
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

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ArticleOptions getArticleOptions() {
        return articleOptions;
    }

    public void setArticleOptions(ArticleOptions articleOptions) {
        this.articleOptions = articleOptions;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getThumbnial() {
        return thumbnial;
    }

    public void setThumbnial(String thumbnial) {
        this.thumbnial = thumbnial;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public MapEntity getMapTemplate() {
        if (getTemplate() != null) {
            return new MapEntity(getTemplate().getId().toString(), getTemplate().getName());
        } else {
            return null;
        }
    }

    public MapEntity getMapArticleCategory() {
        if (getArticleCategory() != null) {
            return new MapEntity(getArticleCategory().getId().toString(), getArticleCategory().getName());
        } else {
            return null;
        }
    }

    public MapEntity getMapArticleCatalog() {
        if (getArticleCatalog() != null) {
            return new MapEntity(getArticleCatalog().getId().toString(), getArticleCatalog().getName());
        } else {
            return null;
        }
    }

    public MapEntity getMapMember() {
        if (getMember() != null) {
            return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
        } else {
            return null;
        }
    }

    public MapEntity getMapArea() {
        if (getArea() != null) {
            return new MapEntity(getArea().getId().toString(), getArea().getName());
        } else {
            return null;
        }
    }

    public MapEntity getMapTags() {
        String tagStr = "";
        if (getTags() != null) {
            for (Tag tag:getTags()) {
                if ("".equals(tagStr)) {
                    tagStr = tag.getName();
                } else {
                    tagStr = tagStr.concat(","+tag.getName());
                }
            }
            return new MapEntity("",tagStr);
        } else {
            return null;
        }
    }

    public Set<ArticleProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<ArticleProduct> products) {
        this.products = products;
    }
}
