package net.wit.controller.weex.model;

import java.io.Serializable;
import java.util.Date;

//文章展示输出模板 H5等

public class ArticleReviewModel implements Serializable {
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
}