package net.wit.controller.weex.model;

import net.wit.entity.Article;

import java.io.Serializable;
import java.util.Date;

//文章编辑模板

public class TemplateModel implements Serializable {

    /** 媒体类型 */
    private Article.MediaType mediaType;
    /** 内容 */
    private String content;
    /** 链接 */
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}