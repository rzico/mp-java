package net.wit.controller.model;
import net.wit.entity.Live;
import net.wit.entity.Member;
import net.wit.entity.Order;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LiveModel extends BaseModel implements Serializable {

    private Long id;


    private Long liveId;

    private Long liveMemberId;
    /**  标题  */
    private String title;

    /**  昵称  */
    private String nickname;

    /**  封面  */
    private String frontcover;

    /**  头像  */
    private String headpic;

    /**  位置  */
    private String location;

    /**  推流地址  */
    private String pushUrl;

    /**  观看地址  */
    private String playUrl;

    /**  状态 0:上线 1:下线  */
    private String online;

    /**  状态 */
    private Live.Status status;

    /**  回放地址  */
    private String hlsPlayUrl;

    /** 在线数 */
    private Long viewerCount;

    /** 点赞数 */
    private Long likeCount;

    /** 礼物数 */
    private Long gift;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Live.Status getStatus() {
        return status;
    }

    public void setStatus(Live.Status status) {
        this.status = status;
    }

    public String getHlsPlayUrl() {
        return hlsPlayUrl;
    }

    public void setHlsPlayUrl(String hlsPlayUrl) {
        this.hlsPlayUrl = hlsPlayUrl;
    }

    public Long getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(Long viewerCount) {
        this.viewerCount = viewerCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getGift() {
        return gift;
    }

    public void setGift(Long gift) {
        this.gift = gift;
    }

    public Long getLiveMemberId() {
        return liveMemberId;
    }

    public void setLiveMemberId(Long liveMemberId) {
        this.liveMemberId = liveMemberId;
    }

    public void bind(Live live) {
        this.id = live.getId();
        this.liveId=live.getId();
        this.liveMemberId=live.getMember().getId();
        this.nickname = live.getNickname();
        this.headpic = live.getHeadpic();
        this.frontcover = live.getFrontcover();
        this.gift = live.getGift();
        this.hlsPlayUrl = live.getHlsPlayUrl();
        this.likeCount = live.getLikeCount();
        this.title = live.getTitle();
        this.location = live.getLocation();
        this.online = live.getOnline();
        this.viewerCount = live.getViewerCount();
        this.pushUrl = live.getPushUrl();
        this.playUrl = live.getPlayUrl();
        this.status = live.getStatus();
    }



    public static List<LiveModel> bindList(List<Live> lives) {
        List<LiveModel> ms = new ArrayList<LiveModel>();
        for (Live live:lives) {
            LiveModel m = new LiveModel();
            m.bind(live);
            ms.add(m);
        }
        return ms;
    }

}