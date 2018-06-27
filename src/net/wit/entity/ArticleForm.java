package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "wx_article_form")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_form_sequence")
public class ArticleForm extends BaseEntity{

    /** 表单提交者 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member sender;

    /** 表单提交者 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member receiver;

    /** 表单所属文章 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Article article;

    /** 表单内容 */
    @Lob
    @Column(columnDefinition="longtext comment '表单内容'")
    @JsonIgnore
    private String data;

    public Member getSender() {
        return sender;
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }

    public Member getReceiver() {
        return receiver;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
