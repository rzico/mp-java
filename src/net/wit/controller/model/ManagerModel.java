package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.entity.TopicConfig;

import java.io.Serializable;
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
    /** 开通收银台 */
    private Boolean useCashier;
    /**  是否开通专栏 */
    private Boolean hasTopic;
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

    public Boolean getUseCashier() {
        return useCashier;
    }

    public void setUseCashier(Boolean useCashier) {
        this.useCashier = useCashier;
    }

    public Boolean getHasTopic() {
        return hasTopic;
    }

    public void setHasTopic(Boolean hasTopic) {
        this.hasTopic = hasTopic;
    }

    public void bind(Member member) {

        this.id = member.getId();
        this.autograph = member.getAutograph();

        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindList(member.getTags());
        this.hasTopic = (member.getTopic()!=null);

        Topic topic = member.getTopic();
        if (topic==null) {
            this.topic = "未开通";
        } else {
            if (topic.getStatus().equals(Topic.Status.waiting)) {
                this.topic = "待点亮";
            }
            if (topic.getStatus().equals(Topic.Status.success)) {
                this.topic = "已认证";
            }
            if (topic.getStatus().equals(Topic.Status.failure)) {
                this.topic = "已过期";
            }
        }
        this.useCashier = false;
    }

}