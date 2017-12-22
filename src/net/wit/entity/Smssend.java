package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

import javax.persistence.*;

/**
 * 实体类 - 短信发送 ============================================================================ 版权所有 2008-2010 rsico.com,并保留所有权利。
 * ---------------------------------------------------------------------------- 提示：在未取得rsico商业授权之前,您不能将本软件应用于商业用途,否则rsico将保留追究的权力。
 * ---------------------------------------------------------------------------- 官方网站：http://www.rsico.com
 * ---------------------------------------------------------------------------- KEY: SHOPUNION5CAA6FDAF2A5662FADB5F15AD00B2070
 * ============================================================================
 */

@Entity
@Table(name = "wx_smssend")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_smssend_sequence")
public class Smssend extends BaseEntity {

	private static final long serialVersionUID = 54L;

	/** 手机号 */
	@Column(columnDefinition="varchar(255) not null comment '手机号'")
	private String mobile;

	/** 发送内容，最长255字符，127个汉字 */
	@Column(columnDefinition="varchar(255) not null comment '发送内容'")
	private String content;

	/** 发送费用 */
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '发送费用'")
	private BigDecimal fee;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}