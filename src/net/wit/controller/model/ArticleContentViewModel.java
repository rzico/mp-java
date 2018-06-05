package net.wit.controller.model;

import net.wit.entity.Article;

import java.io.Serializable;
import java.math.BigDecimal;

//文章编辑模板

public class ArticleContentViewModel extends BaseModel implements Serializable {

    /** 媒体类型 */
    private Article.MediaType mediaType;
    /** 缩例图 */
    private String thumbnail;
    /** 媒体链接 */
    private String original;
    /** 内容 */
    private String content;
    /** 对像 id */
    private Long id;
    /** 第三方链接 */
    private String url;
    /** 价格 */
    private String name;
    /** 商品名称 */
    private BigDecimal price;
    /** 商品名称 */
    private BigDecimal marketPrice;
    /** 分享歉 */
    private BigDecimal rebate;

    public Article.MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(Article.MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }
}