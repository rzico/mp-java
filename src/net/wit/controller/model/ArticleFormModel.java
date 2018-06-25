package net.wit.controller.model;

import net.wit.entity.ArticleForm;

import java.util.ArrayList;
import java.util.List;

public class ArticleFormModel {

    private Long senderId;

    private Long receiceId;

    private Long articleId;

    private String data;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiceId() {
        return receiceId;
    }

    public void setReceiceId(Long receiceId) {
        this.receiceId = receiceId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public void bind(ArticleForm form) {
        this.senderId = form.getSender().getId();
        this.articleId = form.getArticle().getId();
        this.receiceId = form.getReceiver().getId();
        this.data = form.getData();
    }


    public static List<ArticleFormModel> bindList(List<ArticleForm> articles) {
        List<ArticleFormModel> ms = new ArrayList<>();
        for (ArticleForm article:articles) {
            ArticleFormModel m = new ArticleFormModel();
            m.bind(article);
            ms.add(m);
        }
        return ms;
    }
}
