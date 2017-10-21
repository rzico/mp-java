/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity - 序列号
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "wx_sn")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_sn_sequence")
public class Sn extends BaseEntity {

	private static final long serialVersionUID = 122L;

	/** 类型 */
	public enum Type {
		/** 收款单 */
		payment,
		/** 退款单 */
		refunds,
		/** 转账单 */
		transfer
	}

	/** 类型 */
	@Column(nullable = false, updatable = false, unique = true)
	private Type type;

	/** 末值 */
	@Column(nullable = false)
	private Long lastValue;

	// ===========================================getter/setter===========================================//
	/**
	 * 获取类型
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取末值
	 * @return 末值
	 */
	public Long getLastValue() {
		return lastValue;
	}

	/**
	 * 设置末值
	 * @param lastValue 末值
	 */
	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}

}