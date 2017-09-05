/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Order.Direction;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 分页信息
 * 
 * @author rsico Team
 * @version 3.0
 */
public class Pageable implements Serializable {

	private static final long serialVersionUID = -3930180379790344299L;

	/** 默认页码 */
	private static final int DEFAULT_PAGE_NUMBER = 0;

	/** 默认每页记录数 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/** 最大每页记录数 */
	private static final int MAX_PAGE_SIZE = 1000;

	/** 页码 */
	private int pageStart = DEFAULT_PAGE_NUMBER;

	/** 每页记录数 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** 搜索属性 */
	private String searchProperty;

	/** 搜索值 */
	private String searchValue;

	/** 排序属性 */
	private String orderProperty;

	/** 排序方向 */
	private Direction orderDirection;

	//  接收计数器
	private int draw;

	//  接收DataTable 参数
	private String aoData;

	/** 筛选 */
	private List<Filter> filters = new ArrayList<Filter>();

	/** 排序 */
	private List<Order> orders = new ArrayList<Order>();

	/**
	 * 初始化一个新创建的Pageable对象
	 */
	public Pageable() {
	}

	/**
	 * 初始化一个新创建的Pageable对象
	 * 
	 * @param pageStart
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 */
	public Pageable(Integer pageStart, Integer pageSize) {
		if (pageStart != null && pageStart >= 1) {
			this.pageStart = pageStart;
		}
		if (pageSize != null && pageSize >= 1 && pageSize <= MAX_PAGE_SIZE) {
			this.pageSize = pageSize;
		}
	}

	public void bindAo(){
		System.out.println(aoData);
		JSONArray ja = new JSONArray();
		ja.fromObject(aoData);
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<ja.size();i++){
			JSONObject jsonObj = ja.getJSONObject(i);
			map.put(jsonObj.getString("name"), jsonObj.get("value"));
		}

		this.draw = new Long(map.get("sEcho").toString()).intValue();
		this.pageStart = new Long(map.get("iDisplayStart").toString()).intValue();
		this.pageSize = new Long(map.get("iDisplayLength").toString()).intValue();

		int idx = new Long(map.get("iSortCol_0").toString()).intValue();

		this.orderProperty = map.get("iSortCol_0").toString();


	}


	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public String getAoData() {
		return aoData;
	}

	public void setAoData(String aoData) {
		this.aoData = aoData;
		bindAo();
	}

	/**
	 * 获取页码
	 * 
	 * @return 页码
	 */
	public int getPageStart() {
		return pageStart;
	}

	/**
	 * 设置页码
	 * 
	 * @param pageStart
	 *            页码
	 */
	public void setPageStart(int pageStart) {
		if (pageStart < 1) {
			pageStart = DEFAULT_PAGE_NUMBER;
		}
		this.pageStart = pageStart;
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页记录数
	 * 
	 * @param pageSize
	 *            每页记录数
	 */
	public void setPageSize(int pageSize) {
		if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	/**
	 * 获取搜索属性
	 * 
	 * @return 搜索属性
	 */
	public String getSearchProperty() {
		return searchProperty;
	}

	/**
	 * 设置搜索属性
	 * 
	 * @param searchProperty
	 *            搜索属性
	 */
	public void setSearchProperty(String searchProperty) {
		this.searchProperty = searchProperty;
	}

	/**
	 * 获取搜索值
	 * 
	 * @return 搜索值
	 */
	public String getSearchValue() {
		return searchValue;
	}

	/**
	 * 设置搜索值
	 * 
	 * @param searchValue
	 *            搜索值
	 */
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	/**
	 * 获取排序属性
	 * 
	 * @return 排序属性
	 */
	public String getOrderProperty() {
		return orderProperty;
	}

	/**
	 * 设置排序属性
	 * 
	 * @param orderProperty
	 *            排序属性
	 */
	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	/**
	 * 获取排序方向
	 * 
	 * @return 排序方向
	 */
	public Direction getOrderDirection() {
		return orderDirection;
	}

	/**
	 * 设置排序方向
	 * 
	 * @param orderDirection
	 *            排序方向
	 */
	public void setOrderDirection(Direction orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * 获取筛选
	 * 
	 * @return 筛选
	 */
	public List<Filter> getFilters() {
		return filters;
	}

	/**
	 * 设置筛选
	 * 
	 * @param filters
	 *            筛选
	 */
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置排序
	 * 
	 * @param orders
	 *            排序
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

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
		Pageable other = (Pageable) obj;
		return new EqualsBuilder().append(getPageStart(), other.getPageStart()).append(getPageSize(), other.getPageSize()).append(getSearchProperty(), other.getSearchProperty()).append(getSearchValue(), other.getSearchValue()).append(getOrderProperty(), other.getOrderProperty()).append(getOrderDirection(), other.getOrderDirection()).append(getFilters(), other.getFilters())
				.append(getOrders(), other.getOrders()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPageStart()).append(getPageSize()).append(getSearchProperty()).append(getSearchValue()).append(getOrderProperty()).append(getOrderDirection()).append(getFilters()).append(getOrders()).toHashCode();
	}

}