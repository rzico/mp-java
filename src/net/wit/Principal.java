/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 身份信息
 * 
 * @author rsico Team
 * @version 3.0
 */
public class Principal implements Serializable {

	private static final long serialVersionUID = 5798882004228239559L;

	/** ID */
	private Long id;

	/** 用户名 */
	private String username;

	public Principal() {
	}
	
	/**
	 * @param id
	 *            ID
	 * @param username
	 *            用户名
	 */
	public Principal(Long id, String username) {
		this.id = id;
		this.username = username;
	}

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return username;
	}
	
}