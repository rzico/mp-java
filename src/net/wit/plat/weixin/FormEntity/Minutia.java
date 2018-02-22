package net.wit.plat.weixin.FormEntity;

import java.util.List;

/**
 * Created by Eric on 2018/1/16.
 */
public class Minutia {

    //素材唯一标识
    private String media_id;

    //素材内容
    private NewsItem content;

    //更新时间
    private long update_time;

    //其他类型（图片、语音、视频）的返回如下
    //文件名称
    private String name;

    //图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public void setContent(NewsItem content) {
        this.content = content;
    }

    public NewsItem getContent() {
        return content;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
