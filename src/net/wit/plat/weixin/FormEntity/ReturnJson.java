package net.wit.plat.weixin.FormEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Eric on 2018/1/16.
 */
public class ReturnJson {

    //群发速度级别0-4
    private int speed;

    //群发速度的真实值 单位：万/分钟
    private int realspeed;

    //返回的错误代码
    private int errcode;

    //返回的错误信息
    private String errmsg;

    //群发消息后返回的消息任务的id
    private int msg_id;

    //消息发送后的状态，SEND_SUCCESS表示发送成功，SENDING表示发送中，SEND_FAIL表示发送失败，DELETE表示已删除
    private String msg_status;

    //消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，
    // 获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
    private String msg_data_id;

    //媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
    private String type;

    //媒体文件上传后，获取标识
    private String media_id;

    //媒体文件上传时间戳
    private long created_at;

    //微信服务器的url路径
    private String url;

    //语音总数量
    private int voice_count;

    //视频总数量
    private int video_count;

    //图片总数量
    private int image_count;

    //图文总数量
    private int news_count;

    //图文素材的详情列表
    List<Minutia> item;

    //该类型的素材的总数
    private int total_count;

    //本次调用获取的素材的数量
    private int item_count;

    public List<Minutia> getItem() {
        return item;
    }

    public void setItem(List<Minutia> item) {
        this.item = item;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRealspeed() {
        return realspeed;
    }

    public void setRealspeed(int realspeed) {
        this.realspeed = realspeed;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public String getMsg_data_id() {
        return msg_data_id;
    }

    public void setMsg_data_id(String msg_data_id) {
        this.msg_data_id = msg_data_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVoice_count() {
        return voice_count;
    }

    public void setVoice_count(int voice_count) {
        this.voice_count = voice_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }
}
