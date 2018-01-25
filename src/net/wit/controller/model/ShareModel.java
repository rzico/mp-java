package net.wit.controller.model;

import net.wit.Setting;
import net.wit.entity.Article;
import net.wit.entity.ArticleShare;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.util.SettingUtils;

import java.io.Serializable;
import java.util.*;

//文章展示输出模板 H5等

public class ShareModel extends BaseModel implements Serializable {
    /** 标题 */
    private String title;
    /** 缩例图 */
    private String thumbnail;
    /** 描述 */
    private String descr;
    /** 链接 */
    private String url;
    /** 类型 */
    private ArticleShare.ShareType shareType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArticleShare.ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ArticleShare.ShareType shareType) {
        this.shareType = shareType;
    }

    public void bind(Article article, ArticleShare.ShareType shareType, Member member) {
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        this.title = "【"+article.getMember().getNickName()+"】"+article.getTitle();
        this.thumbnail = article.getThumbnail();
        this.descr = article.delHTMLTag();
        if (this.descr==null || this.descr=="") {
            this.descr = "分享来至【"+setting.getSiteName()+"】";
        }
        this.shareType = shareType;
        if (shareType.equals(ArticleShare.ShareType.appWeex)) {
            this.url = "file://view/member/editor/preview.js?articleId=" + article.getId() + "&publish=true";
        } else {
            this.url = "http://"+bundle.getString("weixin.url")+"/#/t"+article.getTemplate().getSn()+"?id="+article.getId();
            if (member!=null) {
                this.url = this.url + "&xuid="+member.getId();
            }
        }
    }

    public void bind(Topic topic, ArticleShare.ShareType shareType, Member member) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Setting setting = SettingUtils.get();
        this.title = topic.getName();
        this.thumbnail = topic.getLogo();
        this.descr = "【"+topic.getMember().getNickName()+"】在"+setting.getSiteName()+"开设的专栏，点击关注订阅它的动态";
        this.shareType = shareType;
        if (shareType.equals(ArticleShare.ShareType.appWeex)) {
            this.url = "file://view/member/author.js?id=" + topic.getId();
        } else {
            this.url = "http://"+bundle.getString("weixin.url")+"/#/c"+topic.getTemplate().getSn()+"?id="+topic.getMember().getId();
            if (member!=null) {
                this.url = this.url + "&xuid="+member.getId();
            }
        }
    }

}