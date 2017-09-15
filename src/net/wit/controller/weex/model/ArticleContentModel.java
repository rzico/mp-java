package net.wit.controller.weex.model;

import net.wit.entity.Article;

import java.io.Serializable;
import java.util.Date;

//文章编辑模板

public class ArticleContentModel implements Serializable {

    /** 媒体类型 */
    private Article.MediaType mediaType;
    /** 缩例图 */
    private String thumbnial;
    /** 媒体链接 */
    private String original;
    /** 内容 */
    private String content;

    public Article.MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(Article.MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnial() {
        return thumbnial;
    }

    public void setThumbnial(String thumbnial) {
        this.thumbnial = thumbnial;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public ArticleContentModel() {

    }

}