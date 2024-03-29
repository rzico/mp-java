package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName:  Log
 * @Description:  日志
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_log_sequence")
public class Log extends BaseEntity {

	private static final long serialVersionUID = 28L;

	/** "日志内容"属性名称 */
	public static final String LOG_CONTENT_ATTRIBUTE_NAME = Log.class.getName() + ".CONTENT";

	/** 操作 */
	@Column(columnDefinition="varchar(255) not null comment '操作'")
	private String operation;

	/** 操作员 */
	@Column(columnDefinition="varchar(255) not null comment '操作员'")
	private String operator;

	/** 内容 */
	@Column(columnDefinition="longtext comment '内容'")
	private String content;

	/** 请求参数 */
	@Lob
	@Column(columnDefinition="longtext comment '请求参数'")
	private String parameter;

	/** IP */
	@Column(columnDefinition="varchar(255) comment 'IP'")
	private String ip;

	// ===========================================getter/setter===========================================//
	/**
	 * 获取操作
	 * @return 操作
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * 设置操作
	 * @param operation 操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 获取操作员
	 * @return 操作员
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * @param operator 操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取内容
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取请求参数
	 * @return 请求参数
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * 设置请求参数
	 * @param parameter 请求参数
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * 获取IP
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * @param ip IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

}