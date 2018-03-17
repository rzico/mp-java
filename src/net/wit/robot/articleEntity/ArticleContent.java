package net.wit.robot.articleEntity;

/**
 * Created by Eric on 2018/2/3.
 */
public class ArticleContent {

    //文章媒体类型
    String mediaType;

    //文章缩略图
    String thumbnail;

    //文章详情图
    String original;

    //文章内容
    String content;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
