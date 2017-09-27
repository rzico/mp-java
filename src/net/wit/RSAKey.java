/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit;

import java.io.Serializable;

/**
 * RSAKey
 * 
 * @author rsico Team
 * @version 3.0
 */
public class RSAKey implements Serializable {

	private static final long serialVersionUID = 3798882004228239559L;

	/** ID */
	private String module;

	/** 用户名 */
	private String exponent;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getExponent() {
		return exponent;
	}

	public void setExponent(String exponent) {
		this.exponent = exponent;
	}
}