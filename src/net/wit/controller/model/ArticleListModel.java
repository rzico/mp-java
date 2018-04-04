package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.ArticleFavorite;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

//文章列表图

public class ArticleListModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 创建时间 */
    private Date createDate;
    /** 作者id */
    private Long authorId;
    /** 作者 */
    private String author;
    /** 作者头像 */
    private String logo;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnail;
    /** 简说明 */
    private String htmlTag;
    /** 销售价 */
    private BigDecimal price;
    /** 市场价 */
    private BigDecimal marketPrice;
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

    public String getHtmlTag() {
        return htmlTag;
    }

    public void setHtmlTag(String htmlTag) {
        this.htmlTag = htmlTag;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public void bind(Article article) {
        this.id = article.getId();
        this.authorId = article.getMember().getId();
        this.createDate = article.getCreateDate();
        this.author = article.getMember().displayName();
        this.logo = article.getMember().getLogo();
        this.title = article.getTitle();
        this.thumbnail = article.getThumbnail();
        this.review = article.getReview();
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.htmlTag = article.delHTMLTag();
        this.tags = TagModel.bindList(article.getTags());
        if (article.getGoods()!=null) {
            Product product = article.getGoods().product();
            if (product!=null) {
                this.price = product.getPrice();
                this.marketPrice = product.getMarketPrice();
            }
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if (article.getTemplate()==null) {
            this.url = "http://" + bundle.getString("weixin.url") + "/#/t1001?id=" + article.getId();
        } else {
            this.url = "http://" + bundle.getString("weixin.url") + "/#/t" + article.getTemplate().getSn() + "?id=" + article.getId();
        }
    }

    public static List<ArticleListModel> bindList(List<Article> articles) {
        List<ArticleListModel> ms = new ArrayList<ArticleListModel>();
        for (Article article:articles) {
            ArticleListModel m = new ArticleListModel();
            m.bind(article);
            ms.add(m);
        }
        return ms;
    }

    public static List<ArticleListModel> bindFavorite(List<ArticleFavorite> favorites) {
        List<ArticleListModel> ms = new ArrayList<ArticleListModel>();
        for (ArticleFavorite favorite:favorites) {
            ArticleListModel m = new ArticleListModel();
            m.bind(favorite.getArticle());
            ms.add(m);
        }
        return ms;
    }

}