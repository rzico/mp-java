package net.wit.controller.model;

import net.wit.entity.ArticleLaud;
import net.wit.entity.ArticleReview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章展示输出模板 H5等

public class ArticleLaudModel implements Serializable {
    /** 会员 */
    private Long memberId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 创建时间 */
    private Date createDate;

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

    public void bind(ArticleLaud laud) {
        this.memberId = laud.getMember().getId();
        this.nickName = laud.getMember().getNickName();
        this.logo = laud.getMember().getLogo();
        this.createDate = laud.getCreateDate();
    }

    public static List<ArticleLaudModel> bindList(List<ArticleLaud> lauds) {
        List<ArticleLaudModel> ms = new ArrayList<ArticleLaudModel>();
        for (ArticleLaud laud:lauds) {
            ArticleLaudModel m = new ArticleLaudModel();
            m.bind(laud);
            ms.add(m);
        }
        return ms;
    }

}