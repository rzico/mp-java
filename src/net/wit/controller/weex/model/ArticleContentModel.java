package net.wit.controller.weex.model;

import net.wit.entity.Article;
import net.wit.entity.Occupation;

import java.io.Serializable;
import java.util.*;

//文章编辑模板

public class ArticleContentModel implements Serializable {

    /** 媒体类型 */
    private Article.MediaType mediaType;
    /** 缩例图 */
    private String thumbnail;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}