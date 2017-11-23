package net.wit.controller.model;

import net.wit.entity.Article;
import net.wit.entity.ArticleVote;

import java.io.Serializable;
import java.util.*;

//文章题库模板 H5等

public class ArticleVoteListModel implements Serializable {
    private Long id;
    private Long memberId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 题目 */
    private String title;
    /** 有效期 */
    private String value;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void bind(ArticleVote articleVote) {
        this.id = articleVote.getId();
        this.memberId = articleVote.getMember().getId();
        this.nickName = articleVote.getMember().getNickName();
        this.logo = articleVote.getMember().getLogo();
        this.title = articleVote.getTitle();
        this.value = articleVote.getValue();
    }

    public static List<ArticleVoteListModel> bindList(List<ArticleVote> articleVotes) {
        List<ArticleVoteListModel> ms = new ArrayList<ArticleVoteListModel>();
        for (ArticleVote articleVote:articleVotes) {
            ArticleVoteListModel m = new ArticleVoteListModel();
            m.bind(articleVote);
            ms.add(m);
        }
        return ms;
    }

}