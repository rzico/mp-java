/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit;

import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 消息
 * 
 * @author rsico Team
 * @version 3.0
 */
public class Message {

	/** 无效会员 */
	public static final String SESSION_INVAILD = "session.invaild";

	/** 登录成功 */
	public static final String LOGIN_SUCCESS = "login.success";

	/** 缓存没变动 */
	public static final String CACHE_SUCCESS = "cache.success";

	/**
	 * 类型
	 */
	public enum Type {

		/** 成功 */
		success,

		/** 警告 */
		warn,

		/** 错误 */
		error	
	}

	/** 类型 */
	private Type type;

	/** 内容 */
	private String content;

	/** 数据 */
	private Object data;

	/** 缓存 */
	private Object md5;

	/**
	 * 初始化一个新创建的 Message 对象，使其表示一个空消息。
	 */
	public Message() {

	}

	/**
	 * 初始化一个新创建的 Message 对象
	 * 
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 */
	public Message(Type type, String content) {
		this.type = type;
		this.content = content;
	}

	/**
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 */
	public Message(Type type, String content, Object... args) {
		this.type = type;
		this.content = SpringUtils.getMessage(content, args);
	}

	/**
	 * 返回成功消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static Message success(String content, Object... args) {
		return new Message(Type.success, content, args);
	}

	/**
	 * 返回带数据的成功消息
	 *
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static Message success(Object entity,String content, Object... args) {
		Message m = new Message(Type.success, content, args);
		m.data = entity;
		return m;
	}

	/**
	 * 返回警告消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 警告消息
	 */
	public static Message warn(String content, Object... args) {
		return new Message(Type.warn, content, args);
	}

	/**
	 * 返回错误消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 错误消息
	 */
	public static Message error(String content, Object... args) {
		return new Message(Type.error, content, args);
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return SpringUtils.getMessage(content);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getMd5() {
		return md5;
	}

	public void setMd5(Object md5) {
		this.md5 = md5;
	}

	public static Message bind(Object data,HttpServletRequest request){
		String js = JsonUtils.toJson(data);
		String md5 = MD5Utils.getMD5Str(js);
		String rmd5 = request.getParameter("md5");
		if (rmd5!=null && md5.equals(rmd5)) {
			return Message.warn(Message.CACHE_SUCCESS);
		}
		Message message = Message.success(data,"success");
		message.setMd5(MD5Utils.getMD5Str(js));
		return message;
	}

}