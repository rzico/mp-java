package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity - 接龙
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_dragon")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_dragon_sequence")
public class Dragon extends BaseEntity {

	private static final long serialVersionUID = 2L;

	public static enum  Type{
		/** 团购 */
		buying,
		/** 报名 */
		enroll
	};

	/** 是否删除 */
	@NotNull
	@Column(nullable = false,columnDefinition="bit not null comment '是否删除'")
	private Boolean deleted;

	/** 主题 */
	@NotNull
	@Length(max = 100)
	@Column(columnDefinition="varchar(255) not null comment '主题'")
	private String title;

	/** 类型 */
	@NotNull
	@Column(columnDefinition="int(11) comment '类型 {buying:团购,enroll:报名}'")
	private Type type;


	/** 发起人 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;


	/** 发起文章 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Article article;


	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}