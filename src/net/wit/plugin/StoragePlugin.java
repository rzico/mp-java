/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.FileInfo;
import net.wit.entity.PluginConfig;
import net.wit.service.PluginConfigService;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Plugin - 存储
 * 
 * @author rsico Team
 * @version 3.0
 */
public abstract class StoragePlugin implements Comparable<StoragePlugin> {
	public static Logger logger = LogManager.getLogger(StoragePlugin.class);

	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public final String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public abstract String getAuthor();

	/**
	 * 获取是否已安装
	 * 
	 * @return 是否已安装
	 */
	public boolean getIsInstalled() {
		return pluginConfigService.pluginIdExists(getId());
	}

	/**
	 * 获取插件配置
	 * 
	 * @return 插件配置
	 */
	public PluginConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getOrders() : null;
	}

	/**
	 * 文件上传
	 * 
	 * @param path
	 *            上传路径
	 * @param file
	 *            上传文件
	 * @param contentType
	 *            文件类型
	 */
	public abstract void upload(String path, MultipartFile file, String contentType) throws IOException;

	public String getMineType(String name) {
		Map<String ,String> data = new HashMap<String,String>();
		data.put(".jpg","image/jpeg");
		data.put(".jpe","image/jpeg");
		data.put(".jpeg","image/jpeg");
		data.put(".bmp","image/bmp");
		data.put(".png","image/png");
		data.put(".gif","image/gif");
		data.put(".mp4","video/mp4");
		data.put(".mkv","video/x-m4v");
		data.put(".flv","video/x-flv");
		data.put(".swf","application/x-shockwave-flash");
		data.put(".avi","video/x-msvideo");
		data.put(".rm","application/vnd.rn-realmedia");
		data.put(".rmvb","application/vnd.rn-realmedia");
		data.put(".mpeg","video/mpeg");
		data.put(".mpg","video/mpeg");
		data.put(".mp3","audio/mpeg");
		data.put(".wav","audio/wav");
		if (data.containsKey(name.toLowerCase()) ) {
			return data.get(name.toLowerCase());
		} else {
			return "application/octet-stream";
		}
	}
	/**
	 * 获取访问URL
	 * 
	 * @param path
	 *            上传路径
	 * @return 访问URL
	 */
	public abstract String getUrl(String path);

	/**
	 * 文件浏览
	 * 
	 * @param path
	 *            浏览路径
	 * @return 文件信息
	 */
	public abstract List<FileInfo> browser(String path);

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		StoragePlugin other = (StoragePlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	public int compareTo(StoragePlugin storagePlugin) {
		return new CompareToBuilder().append(getOrder(), storagePlugin.getOrder()).append(getId(), storagePlugin.getId()).toComparison();
	}

}