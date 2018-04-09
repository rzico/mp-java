package net.wit.controller.model;
import net.wit.entity.Live;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LiveListModel extends BaseModel implements Serializable {

    private Long id;
    /**  标题  */
    private String title;

    /**  昵称  */
    private String nickname;

    /**  封面  */
    private String frontcover;

    /**  头像  */
    private String headpic;

    /**  状态 0:上线 1:下线  */
    private String online;

    /** 点赞数 */
    private Long likeCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public void bind(Live live) {
        this.id = live.getId();
        this.nickname = live.getNickname();
        this.headpic = live.getHeadpic();
        this.frontcover = live.getFrontcover();
        this.likeCount = live.getLikeCount();
        this.title = live.getTitle();
        this.online = live.getOnline();
    }



    public static List<LiveListModel> bindList(List<Live> lives) {
        List<LiveListModel> ms = new ArrayList<LiveListModel>();
        for (Live live:lives) {
            LiveListModel m = new LiveListModel();
            m.bind(live);
            ms.add(m);
        }
        return ms;
    }

}