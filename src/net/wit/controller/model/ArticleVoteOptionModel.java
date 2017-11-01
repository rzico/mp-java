package net.wit.controller.model;

import net.wit.entity.ArticleVote;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

//文章题库模板 H5等

public class ArticleVoteOptionModel implements Serializable {
    /** 题目 */
    private String  title;
    /** 有效期 */
    private Date expire;
    /** 类型 */
    private ArticleVote.VoteType voteType;
    /** 答案 */
    private List<String> options = new ArrayList<String>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public ArticleVote.VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(ArticleVote.VoteType voteType) {
        this.voteType = voteType;
    }
}