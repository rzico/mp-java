package net.wit.controller.model;

import net.wit.entity.ArticleLaud;
import net.wit.entity.ArticleShare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章展示输出模板 H5等

public class ArticleShareModel extends BaseModel implements Serializable {
    /** 会员 */
    private Long memberId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 创建时间 */
    private Date createDate;
    /** 分享至 */
    private ArticleShare.ShareType shareType;

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

    public ArticleShare.ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ArticleShare.ShareType shareType) {
        this.shareType = shareType;
    }

    public void bind(ArticleShare share) {
        this.memberId = share.getMember().getId();
        this.nickName = share.getMember().displayName();
        this.logo = share.getMember().getLogo();
        this.createDate = share.getCreateDate();
        this.shareType = share.getShareType();
    }

    public static List<ArticleShareModel> bindList(List<ArticleShare> shares) {
        List<ArticleShareModel> ms = new ArrayList<ArticleShareModel>();
        for (ArticleShare share:shares) {
            ArticleShareModel m = new ArticleShareModel();
            m.bind(share);
            ms.add(m);
        }
        return ms;
    }

}