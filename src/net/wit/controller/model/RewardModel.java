package net.wit.controller.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//文章编辑模板

public class RewardModel implements Serializable {

    /** 交易昵称 */
    private String nickName;
    /** 交易方头像 */
    private String logo;
    /** 摘要 */
    private String memo;
    /** 金额 */
    private BigDecimal amount;

    /** 文章id */
    private Long articleId;
    /** 文章标题 */
    private String title;

    /** 时间 */
    private Date createDate;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getAmt() {
        return amount;
    }

    public void setAmt(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}