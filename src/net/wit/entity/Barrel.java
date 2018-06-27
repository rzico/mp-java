package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity - 非卖品
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_barrel")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_barrel_sequence")
public class Barrel extends OrderEntity {

	private static final long serialVersionUID = 2L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 是否删除 */
	@NotNull
	@Column(nullable = false,columnDefinition="bit not null comment '是否删除'")
	private Boolean deleted;

	/** 名称 */
	@NotNull
	@Length(max = 100)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** logo */
	@Column(columnDefinition="varchar(255) comment 'logo'")
	private String logo;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}