package net.wit.entity;

import java.util.List;

/**
 * Created by Eric on 2018/2/4.
 */
public class ArticleTemplate {
    //文章标题
    String Title;

    //文章缩略图
    String thumbnail;

    //文章内容
    List<ArticleContent> templates;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<ArticleContent> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ArticleContent> templates) {
        this.templates = templates;
    }
}
