package net.wit.controller.model;
import net.wit.entity.CardBill;
import net.wit.entity.Friends;
import net.wit.entity.Member;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicListModel extends BaseModel implements Serializable {

    private Long id;
    /** 专栏 */
    private String name;
    /** 昵称 */
    private String nickName;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 我关注了 */
    private Boolean follow;
    /** 是否关注 */
    private Boolean followed;
    /** 标签 */
    private List<TagModel> tags = new ArrayList<TagModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public void bind(Topic topic) {
        Member member = topic.getMember();
        this.id = member.getId();
        this.autograph = member.getAutograph();
        if (this.autograph==null) {
            this.autograph = "TA好像忘记签名了";
        }
        this.follow = false;
        this.followed = false;
        this.name = topic.getName();
        this.logo = topic.getLogo();
        this.nickName = member.displayName();
        this.tags = TagModel.bindList(member.getTags());
    }


    public static List<TopicListModel> bindList(List<Topic> topics) {
        List<TopicListModel> ms = new ArrayList<TopicListModel>();
        for (Topic topic:topics) {
            TopicListModel m = new TopicListModel();
            m.bind(topic);
            ms.add(m);
        }
        return ms;
    }

}
