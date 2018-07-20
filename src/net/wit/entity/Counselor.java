
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

	/** 电话 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '电话'")
	private String phone;

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

	/** 简介 */
	@Lob
	@Column(columnDefinition="longtext comment '简介'")
	@JsonIgnore
	private String content1;

	/** 教育经历 */
	@Lob
	@Column(columnDefinition="longtext comment '教育经历'")
	@JsonIgnore
	private String content2;

	/** 工作经历 */
	@Lob
	@Column(columnDefinition="longtext comment '工作经历'")
	@JsonIgnore
	private String content3;

	/** 行业执照 */
	@Lob
	@Column(columnDefinition="longtext comment '行业执照'")
	@JsonIgnore
	private String content4;

	/** 治疗取向 */
	@Lob
	@Column(columnDefinition="longtext comment '治疗取向'")
	@JsonIgnore
	private String content5;

	/** 咨询经验 */
	@Lob
	@Column(columnDefinition="longtext comment '咨询经验'")
	@JsonIgnore
	private String content6;

	/** 擅长领域 */
	@Lob
	@Column(columnDefinition="longtext comment '擅长领域'")
	@JsonIgnore
	private String content7;

	/** 咨询方式与费用 */
	@Lob
	@Column(columnDefinition="longtext comment '咨询方式与费用'")
	@JsonIgnore
	private String content8;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '会员'")
	@JsonIgnore
	private Member member;

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@JsonIgnore
	private Enterprise enterprise;

	/** 标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_counselor_tag")
	@OrderBy("orders asc")
	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();

	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit not null default 0 comment '是否删除'")
	@JsonIgnore
	private Boolean deleted;

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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getContent3() {
		return content3;
	}

	public void setContent3(String content3) {
		this.content3 = content3;
	}

	public String getContent4() {
		return content4;
	}

	public void setContent4(String content4) {
		this.content4 = content4;
	}

	public String getContent5() {
		return content5;
	}

	public void setContent5(String content5) {
		this.content5 = content5;
	}

	public String getContent6() {
		return content6;
	}

	public void setContent6(String content6) {
		this.content6 = content6;
	}

	public String getContent7() {
		return content7;
	}

	public void setContent7(String content7) {
		this.content7 = content7;
	}

	public String getContent8() {
		return content8;
	}

	public void setContent8(String content8) {
		this.content8 = content8;
	}
}