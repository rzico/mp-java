package net.wit.plat.weixin.FormEntity;

/**
 * Created by Eric on 2018/1/15.
 */
public class TagPropa {

    //用于设定图文消息的接收者
    private Filter filter;

    //群发文本消息
    private Details text;

    //群发图文消息
    private Details mpnews;

    //群发音频消息
    private Details voice;

    //群发图片消息
    private Details image;

    //群发视频消息
    private Details mpvideo;

    //群发卡券消息
    private Details wxcard;

    //群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为，mpvideo，卡券为wxcard
    private String msgtype;

    //图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。
    private String send_ignore_reprint;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Details getText() {
        return text;
    }

    public void setText(Details text) {
        this.text = text;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Details getMpnews() {
        return mpnews;
    }

    public void setMpnews(Details mpnews) {
        this.mpnews = mpnews;
    }

    public Details getVoice() {
        return voice;
    }

    public void setVoice(Details voice) {
        this.voice = voice;
    }

    public Details getImage() {
        return image;
    }

    public void setImage(Details image) {
        this.image = image;
    }

    public Details getMpvideo() {
        return mpvideo;
    }

    public void setMpvideo(Details mpvideo) {
        this.mpvideo = mpvideo;
    }

    public Details getWxcard() {
        return wxcard;
    }

    public void setWxcard(Details wxcard) {
        this.wxcard = wxcard;
    }

    public String getSend_ignore_reprint() {
        return send_ignore_reprint;
    }

    public void setSend_ignore_reprint(String send_ignore_reprint) {
        this.send_ignore_reprint = send_ignore_reprint;
    }
}
