package net.wit.controller.model;

import net.wit.entity.ArticleReview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章展示输出模板 H5等

public class ArticleReviewModel implements Serializable {
    /**  评论 id */
    private Long id;
    /** 会员 */
    private Long memberId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 创建时间 */
    private Date createDate;
    /** 评论内容 */
    private String content;

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void bind(ArticleReview review) {
        this.id = review.getId();
        this.memberId = review.getMember().getId();
        this.nickName = review.getMember().getNickName();
        this.logo = review.getMember().getLogo();
        this.createDate = review.getCreateDate();
        this.content = review.getContent();
    }

    public static List<ArticleReviewModel> bindList(List<ArticleReview> reviews) {
        List<ArticleReviewModel> ms = new ArrayList<ArticleReviewModel>();
        for (ArticleReview review:reviews) {
            ArticleReviewModel m = new ArticleReviewModel();
            m.bind(review);
            ms.add(m);
        }
        return ms;
    }

}