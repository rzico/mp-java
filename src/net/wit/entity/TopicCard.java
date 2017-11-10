package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;

import javax.persistence.*;

/**
 * Created by WangChao on 2017-06-23 15:14:12
 */
@Entity
@Table(name = "wx_topic_card")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_topic_card_sequence")
public class TopicCard extends BaseEntity {
    private static final long serialVersionUID = 60L;

    public enum Status {
        /**
         * 待审核
         */
        waiting,
        /**
         * 审核失败
         */
        failture,
        /**
         * 通过审核
         */
        success
    }

    public enum Color {
        /** 待审核  */
        c1,
        /** 待审核  */
        c2,
        /** 待审核  */
        c3,
        /** 待审核  */
        c4,
        /** 待审核  */
        c5,
        /** 待审核  */
        c6,
        /** 待审核  */
        c7,
        /** 待审核  */
        c8,
        /** 待审核  */
        c9,
        /** 待审核  */
        c10,
    }

    /**
     * 状态
     */
    @Column(columnDefinition="int(11) not null comment '状态 {waiting:待审核,failture:审核失败,success:审核通过}'")
    private Status status;

    /**
     * 微信卡券Id
     */
    @Column(columnDefinition="varchar(255) comment '微信卡券Id'")
    private String weixinCardId;

    /**
     * 支付宝卡券Id
     */
    @Column(columnDefinition="varchar(255) comment '支付宝卡券Id'")
    private String alipayCardId;

    /**
     * 标题
     */
    @Column(columnDefinition="varchar(255) not null comment '标题'")
    private String title;

    /**
     * 卡背景
     */
    @Column(columnDefinition="varchar(255) comment '卡背景'")
    private String background;

    /**
     * 卡颜色
     */
    @Column(columnDefinition="int(11) not null comment '卡颜色'")
    private Color color;


    /**
     *  特权说明
     */
    @Column(columnDefinition="varchar(255) comment '特权说明'")
    private String prerogative;


    /**
     *  描述说明
     */
    @Column(columnDefinition="varchar(255) comment '描述说明'")
    private String description;

    /**
     * 专栏
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    @JsonIgnore
    private Topic topic;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getWeixinCardId() {
        return weixinCardId;
    }

    public void setWeixinCardId(String weixinCardId) {
        this.weixinCardId = weixinCardId;
    }

    public String getAlipayCardId() {
        return alipayCardId;
    }

    public void setAlipayCardId(String alipayCardId) {
        this.alipayCardId = alipayCardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPrerogative() {
        return prerogative;
    }

    public void setPrerogative(String prerogative) {
        this.prerogative = prerogative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public MapEntity getMapTopic() {
        if (getTopic() != null) {
            return new MapEntity(getTopic().getId().toString(), getTopic().getName());
        } else {
            return null;
        }
    }
}
