package net.wit.controller.weex.model;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//文章展示输出模板 H5等

public class ArticleViewModel implements Serializable {
    
    private Long id;
    /** 会员 */
    private MemberModel member;
    /** 作者 */
    private String author;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnial;
    /** 创建时间 */
    private Date createDate;
    /** 背景音乐 */
    private String music;
    /** 链接 */
    private String url;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;
    /** 内容 */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnial() {
        return thumbnial;
    }

    public void setThumbnial(String thumbnial) {
        this.thumbnial = thumbnial;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}