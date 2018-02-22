package net.wit.plat.weixin.FormEntity;

/**
 * Created by Eric on 2018/1/12.
 */
public class Details {

    //标题
    private String title;

    //图文消息的封面图片素材id（必须是永久mediaID）
    private String thumb_media_id;

    //作者
    //该字段上传时可以为空
    private String author;

    //图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前64个字。
    //该字段上传时可以为空
    private String digest;

    //是否显示封面，0为false，即不显示，1为true，即显示
    private String show_cover_pic;

    //图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
    //同时也是群发文本消息时的文本内容
    private String content;

    //图文消息的原文地址，即点击“阅读原文”后的URL
    private String content_source_url;

    //以下字段全是微信返回字段 上传时过滤该些字段
    //图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
    private String url;

    //媒体ID 微信上唯一标识
    private String media_id;

    //文件名称
    private String name;

    //这篇图文消息素材的最后更新时间
    private String update_time;

    //以下字段是群发所需字段
    //需要发送的卡券消息
    private String card_id;

    //已下字段是错误信息
    private String errcode;

    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb_media_id() {
        return thumb_media_id;
    }

    public void setThumb_media_id(String thumb_media_id) {
        this.thumb_media_id = thumb_media_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getShow_cover_pic() {
        return show_cover_pic;
    }

    public void setShow_cover_pic(String show_cover_pic) {
        this.show_cover_pic = show_cover_pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_source_url() {
        return content_source_url;
    }

    public void setContent_source_url(String content_source_url) {
        this.content_source_url = content_source_url;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
