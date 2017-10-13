/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 消息
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "wx_message")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_message_sequence")
public class Message extends BaseEntity {

	private static final long serialVersionUID = 115L;
	
	/** 消息类型  */
	public static enum Type {
		/** 订单提醒  */
		order,
		/** 账单提醒   */
		account,
		/** 系统消息   */
		message,
		/** 评论回复   */
		review,
		/** 点赞提醒   */
		laud,
		/** 关注提醒   */
		follow,
		/** 收藏提醒   */
		favorite
	}

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {order:订单提醒,account:账单提醒,message:系统消息,review:评论回复,laud:点赞提醒,follow:关注提醒,favorite:收藏提醒}'")
	private Type type;
	
	/** 标题 */
	@Column(columnDefinition="varchar(255) not null comment '标题'")
	private String title;

	/** 内容 */
	@NotNull
	@Length(max = 1000)
	@Column(columnDefinition="longtext not null comment '内容'")
	private String content;

	/** 缩例图 */
	@Column(columnDefinition="varchar(255) comment '缩例图'")
	private String thumbnial;

	/** 收件人已读 */
	@Column(columnDefinition="bit not null comment '收件人已读'")
	private Boolean readed;

	/** 是否删除 */
	@Column(columnDefinition="bit not null comment '是否删除'")
	private Boolean deleted;

	/** 收件人 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '收件人'")
	@JsonIgnore
	private Member receiver;

	/** 发送人 系统消息时为 null */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '发送人'")
	@JsonIgnore
	private Member member;

	public Message.Type getType() {
		return type;
	}

	public void setType(Message.Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumbnial() {
		return thumbnial;
	}

	public void setThumbnial(String thumbnial) {
		this.thumbnial = thumbnial;
	}

	public Boolean getReaded() {
		return readed;
	}

	public void setReaded(Boolean readed) {
		this.readed = readed;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Member getReceiver() {
		return receiver;
	}

	public void setReceiver(Member receiver) {
		this.receiver = receiver;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}

	public MapEntity getMapReceiver() {
		if (getReceiver() != null) {
			return new MapEntity(getReceiver().getId().toString(), getReceiver().getNickName()+"("+getReceiver().getName()+")");
		} else {
			return null;
		}
	}
}