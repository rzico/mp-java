/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PluginConfig
 * @Description: 插件配置
 * @author Administrator
 * @date 2014年10月14日 上午9:10:23
 */
@Entity
@Table(name = "wx_plugin_config")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_plugin_config_sequence")
public class PluginConfig extends OrderEntity {

	private static final long serialVersionUID = 117L;

	/** 插件ID */
	@Column(columnDefinition="varchar(50) not null unique comment '插件ID'")
	private String pluginId;

	/** 是否启用 */
	@Column(columnDefinition="bit not null comment '插件ID'")
	private Boolean isEnabled;

	/** 属性 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "wx_plugin_config_attribute")
	@MapKeyColumn(name = "name", length = 100)
	private Map<String, String> attributes = new HashMap<String, String>();

	/**
	 * 获取属性值
	 * @param name 属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		if (getAttributes() != null && name != null) {
			return getAttributes().get(name);
		} else {
			return null;
		}
	}

	/**
	 * 设置属性值
	 * @param name 属性名称
	 * @param value 属性值
	 */
	public void setAttribute(String name, String value) {
		if (getAttributes() != null && name != null) {
			getAttributes().put(name, value);
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取插件ID
	 * @return 插件ID
	 */
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * 设置插件ID
	 * @param pluginId 插件ID
	 */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	/**
	 * 获取是否启用
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * @param isEnabled 是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取属性
	 * @return 属性
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * 设置属性
	 * @param attributes 属性
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}