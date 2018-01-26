package net.wit.plat.weixin.FormEntity;

import java.util.List;

/**
 * Created by Eric on 2018/1/16.
 */
public class NewsItem {

    private List<Content> news_item;

    //这篇图文消息素材的创建时间
    private long create_time;

    //这篇图文消息素材的最后更新时间
    private long update_time;

    public List<Content> getNews_item() {
        return news_item;
    }

    public void setNews_item(List<Content> news_item) {
        this.news_item = news_item;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
