package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 小程序轮播图
 */

@Entity
@Table(name = "wx_applet_sowing_map")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_applet_sowing_map_sequence")
public class AppletSowingMap extends BaseEntity{

    public static enum ACTION{
        OPENARTICLE,//打开文章
        OPENPRODUCT,//打开产品
        OPENWEBVIEW//打开网页
    }

    @NotNull
    @Column(columnDefinition="int(11) default '0' comment '做什么  {OPENARTICLE:打开文章,OPENPRODUCT:打开产品, OPENWEBVIEW:打开网页}'")
    private ACTION action;
    /**
     * 需要传的id
     */
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '发卡序号'")
    private Long actionId;

    /**跳转链接 可以为空 **/
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '跳转链接'")
    private String url;

    /**  封面  */
    @Column(columnDefinition="varchar(255) comment '封面'")
    private String frontcover;

    /**  企业的用户，这个轮播图是属于那个专栏账号的  **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '专栏'")
    private Topic topic;

    public ACTION getAction() {
        return action;
    }

    public void setAction(ACTION action) {
        this.action = action;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
