package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.util.*;

//文章预览页 H5等

public class ArticlePreviewModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 作者 */
    private Long memberId;
    /** 作者昵称 */
    private String nickName;
    /** 作者头像 */
    private String logo;
    /** 模版 */
    private String template;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;
    /** 分享数 */
    private Long share;
    /** 是否点赞 */
    private Boolean hasLaud;
    /** 是否收藏 */
    private Boolean hasFavorite;
    /** 是否关注 */
    private Boolean hasFollow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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

    public Boolean getHasLaud() {
        return hasLaud;
    }

    public void setHasLaud(Boolean hasLaud) {
        this.hasLaud = hasLaud;
    }

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }

    public Boolean getHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(Boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    public void bind(Article article) {
        this.id = article.getId();
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.share = article.getShare();
        this.review = article.getReview();
        this.nickName = article.getMember().getNickName();
        this.logo = article.getMember().getLogo();
        this.memberId = article.getMember().getId();
        this.hasFavorite = false;
        this.hasLaud = false;
        this.hasFollow = false;
        this.template = article.getTemplate().getSn();
    }

}