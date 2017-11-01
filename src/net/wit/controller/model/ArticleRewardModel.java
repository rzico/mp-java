package net.wit.controller.model;

import net.wit.entity.ArticleReview;
import net.wit.entity.ArticleReward;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章展示输出模板 H5等

public class ArticleRewardModel implements Serializable {
    /** 会员 */
    private Long memberId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 创建时间 */
    private Date createDate;
    /** 金额 */
    private BigDecimal amount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void bind(ArticleReward reward) {
        this.memberId = reward.getMember().getId();
        this.nickName = reward.getMember().getNickName();
        this.logo = reward.getMember().getLogo();
        this.createDate = reward.getCreateDate();
        this.amount = reward.getAmount();
    }

    public static List<ArticleRewardModel> bindList(List<ArticleReward> rewards) {
        List<ArticleRewardModel> ms = new ArrayList<ArticleRewardModel>();
        for (ArticleReward reward:rewards) {
            ArticleRewardModel m = new ArticleRewardModel();
            m.bind(reward);
            ms.add(m);
        }
        return ms;
    }

}