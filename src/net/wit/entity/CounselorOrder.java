
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: CounselorOrder
 * @Description:  预约咨询
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_counselor_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_counselor_order_sequence")
public class CounselorOrder extends BaseEntity {

	private static final long serialVersionUID = 33L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 预约  */
		enabled,

		/** 关闭  */
		disabled
	}

	/** 状态 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 名称 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 手机 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '手机'")
	private String mobile;

	/** 性别 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '性别'")
	private String sex;

	/** 问题 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '问题'")
	private String worry ;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '备注'")
	private Member member;

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Enterprise enterprise;

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Counselor counselor;

	public CounselorOrder.Status getStatus() {
		return status;
	}

	public void setStatus(CounselorOrder.Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWorry() {
		return worry;
	}

	public void setWorry(String worry) {
		this.worry = worry;
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

	public Counselor getCounselor() {
		return counselor;
	}

	public void setCounselor(Counselor counselor) {
		this.counselor = counselor;
	}
}