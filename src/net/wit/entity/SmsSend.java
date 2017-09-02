package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 实体类 - 短信发送 ============================================================================ 版权所有 2008-2010 rsico.com,并保留所有权利。
 * ---------------------------------------------------------------------------- 提示：在未取得rsico商业授权之前,您不能将本软件应用于商业用途,否则rsico将保留追究的权力。
 * ---------------------------------------------------------------------------- 官方网站：http://www.rsico.com
 * ---------------------------------------------------------------------------- KEY: SHOPUNION5CAA6FDAF2A5662FADB5F15AD00B2070
 * ============================================================================
 */

@Entity
@Table(name = "xm_smssend")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_smssend_sequence")
public class SmsSend extends BaseEntity {

	private static final long serialVersionUID = 121L;

	public static final int SMS_CONTENT_MAX_LENGTH = 255;// 短信最大长度


	public enum Status {
		wait, send, Error
	}

	/** 手机号 */
	@Column(columnDefinition="varchar(255) not null comment '手机号'")
	private String mobile;

	/** 发送内容，最长255字符，127个汉字 */
	@Column(columnDefinition="varchar(255) not null comment '发送内容'")
	private String content;

	/** 短信条数 */
	@Column(columnDefinition="int(11) not null comment '短信条数'")
	private Integer count;

	/** 发送费用 */
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '发送费用'")
	private BigDecimal fee;

	/** 发送状态 0待发送 1发送成功，其他失败 */
	@Column(columnDefinition="int(11) not null comment '发送状态 {wait:等待,send:成功,Error:失败}'")
	private Status status;

	/** 错误说明 */
	@Column(columnDefinition="varchar(255) comment '错误说明'")
	private String descr;

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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public net.wit.entity.SmsSend.Status getStatus() {
		return status;
	}

	public void setStatus(net.wit.entity.SmsSend.Status status) {
		this.status = status;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
}