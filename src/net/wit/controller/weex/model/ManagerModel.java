package net.wit.controller.weex.model;
import net.wit.entity.Member;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ManagerModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 专栏 */
    private String topic;
    /** 标签 */
    private List<TagModel> tags = new ArrayList<TagModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.autograph = member.getAutograph();

        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindList(member.getTags());
        Topic topic = member.getTopic();
        if (topic==null) {
            this.topic = "未开通";
        } else {
            if (topic.getStatus().equals(Topic.Status.waiting)) {
                this.topic = "待缴费";
            }
            if (topic.getStatus().equals(Topic.Status.success)) {
                this.topic = "已认证";
            }
            if (topic.getStatus().equals(Topic.Status.failure)) {
                this.topic = "已过期";
            }
        }
     }
}