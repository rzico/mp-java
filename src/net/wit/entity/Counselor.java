
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: Counselor
 * @Description:  咨询师
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_counselor")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_counselor_sequence")
public class Counselor extends OrderEntity {

	private static final long serialVersionUID = 33L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 启用  */
		enabled,

		/** 关闭  */
		disabled
	}

	/** 状态 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 头像 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '头像'")
	private String logo;

	/** 名称 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 签名 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '签名'")
	private String autograph;

	/** 头街 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '头街'")
	private String speciality ;

	/** 介绍 */
	@Lob
	@Column(columnDefinition="longtext comment '介绍'")
	@JsonIgnore
	private String content;

	public Counselor.Status getStatus() {
		return status;
	}

	public void setStatus(Counselor.Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}