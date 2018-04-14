package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
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

    private static final long serialVersionUID = 3L;

    /**
     * 权限
     */
    public enum Authority {

        /** 公开  所有人可见，且录入个人专栏  */
        isPublic,

        /** 不公开，自行控制分享范围，仅被分享的人可见  */
        isShare,

        /** 加密，设置密码，凭密码访问  */
        isEncrypt,

        /** 私密，公自已可见  */
        isPrivate
    }


    public static enum MediaType{
        /**   */
        html,
        /** 图文 */
        image,
        /** 音频 */
        audio,
        /** 视频 */
        video,
        /** 商品 */
        product,
        /** 链接 */
        href
        };

    /** 类型 */
    @NotNull
    @Column(columnDefinition="int(11) comment '类型 {image:图文,audio:音频,video:视频}'")
    private MediaType mediaType;

    /** 作者 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '作者'")
    @JsonIgnore
    private String author;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    /** 商品 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Goods goods;

    /** 标题 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '标题'")
    private String title;

    /** 缩例图 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '缩例图'")
    @JsonIgnore
    private String thumbnail;

    /** 类别 */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '类别 {}'")
    @JsonIgnore
    private ArticleCategory articleCategory;

    /** 文集 */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '文集'")
    @JsonIgnore
    private ArticleCatalog articleCatalog;

    /** 背景音乐 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '背景音乐'")
    @JsonIgnore
    private String music;

    /** 文章内容 */
    @Lob
    @Column(columnDefinition="longtext comment '文章内容'")
    @JsonIgnore
    private String content;

    /** 收藏数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '收藏数'")
    private Long favorite;


    /** 评论数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '评论数'")
    private Long review;

    /** 阅读数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '阅读数'")
    private Long hits;

    /** 点赞数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '点赞数'")
    private Long laud;

    /** 分享数 */
    @NotNull
    @Min(0)
    @Column(columnDefinition="bigint(20) not null default 0 comment '分享数'")
    private Long share;

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit not null default 0 comment '是否删除'")
    @JsonIgnore
    private Boolean deleted;

    /** 是否草稿 */
    @NotNull
    @Column(columnDefinition="bit not null default 1 comment '是否草稿'")
    @JsonIgnore
    private Boolean isDraft;

    /** 是否审核 */
    @NotNull
    @Column(columnDefinition="bit not null default 0 comment '是否审核'")
    @JsonIgnore
    private Boolean isAudit;


    /** 是否投稿 */
    @NotNull
    @Column(columnDefinition="bit comment '是否投稿'")
    private Boolean isPublish;

    /** 是否精选 */
    @NotNull
    @Column(columnDefinition="bit comment '是否精选'")
    private Boolean isPitch;

    /** 是否评论 */
    @NotNull
    @Column(columnDefinition="bit comment '是否评论'")
    private Boolean isReview;

    /** 是否赞赏 */
    @NotNull
    @Column(columnDefinition="bit comment '是否赞赏'")
    private Boolean isReward;

    /** 谁可见 */
    @NotNull
    @Column(columnDefinition="int(11) comment '谁可见  {isPublic:公开,isShare:不会开,isEncrypt:加密,isPrivate:私秘}'")
    private Authority authority;

    /** 是否样例 */
    @Column(columnDefinition="bit comment '是否样例'")
    private Boolean isExample;

    /** 是否置顶 */
    @NotNull
    @Column(columnDefinition="bit comment '是否置顶'")
    private Boolean isTop;

    /** 密码 */
    @Column(columnDefinition=" varchar(255) comment '密码'")
    private String password;

    /** 定位 */
    @Embedded
    @JsonIgnore
    private Location location;

    /** 所在地 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Area area;

    /** 模板 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '模板'")
    @JsonIgnore
    private Template template;

    /** 文章标签*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "wx_article_tag")
    @OrderBy("orders asc")
    @JsonIgnore
    private List<Tag> tags = new ArrayList<Tag>();

    /** 点赞者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<ArticleLaud> lauds = new HashSet<ArticleLaud>();

    /** 收藏者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<ArticleFavorite> favorites = new HashSet<ArticleFavorite>();

    /** 评论者*/
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<ArticleReview> reviews = new HashSet<ArticleReview>();
//
//    /** 商品*/
//    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    @JsonIgnore
//    private Set<ArticleProduct> products = new HashSet<ArticleProduct>();

    /** 投票项 */
    private String votes;

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

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public void setIsReview(Boolean isReview) {
        this.isReview = isReview;
    }
    public Boolean getIsReview() {
        return this.isReview;
    }

    public Long getReview() {
        return review;
    }
    public void setReview(Long review) {
        this.review = review;
    }

    public void setReview(Set<ArticleReview> review) {
        this.reviews = reviews;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }

    public Boolean getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(Boolean audit) {
        isAudit = audit;
    }

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean publish) {
        isPublish = publish;
    }

    public Boolean getIsPitch() {
        return isPitch;
    }

    public void setIsPitch(Boolean pitch) {
        isPitch = pitch;
    }

    public Boolean getIsReward() {
        return isReward;
    }

    public void setIsReward(Boolean reward) {
        isReward = reward;
    }

    public Boolean getIsExample() {
        return isExample;
    }

    public void setIsExample(Boolean example) {
        isExample = example;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean top) {
        isTop = top;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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
            return new MapEntity(getMember().getId().toString(), getMember().displayName() );
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
//
//    public Set<ArticleProduct> getProducts() {
//        return products;
//    }
//
//    public void setProducts(Set<ArticleProduct> products) {
//        this.products = products;
//    }

    public String delHTMLTag(){
        String str = getContent();
        String reg = "[^\u4e00-\u9fa5]";
        str = str.replaceAll(reg, "");
        str = str.trim();
        if (str.length()<100) {
            return str;
        } else {
            return str.substring(0,100);
        }
    }

    public MapEntity getMapIsAudit() {
        if ((getId() != null)&&(getIsAudit() != null)) {
            return new MapEntity(getId().toString(), getIsAudit().toString());
        } else {
            return null;
        }
    }
}
