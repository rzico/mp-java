
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 排序基类
 */
@MappedSuperclass
public abstract class OrderEntity extends BaseEntity implements Comparable<OrderEntity> {

	private static final long serialVersionUID = 35L;

	/** "排序"属性名称 */
	public static final String ORDER_PROPERTY_NAME = "orders";

	/** 排序 */
	private Integer orders;

	/**
	 * 获取排序
	 *
	 * @return 排序
	 */
	@Min(0)
	@Column(name = "orders")
	public Integer getOrders() {
		return orders;
	}

	/**
	 * 设置排序
	 *
	 * @param orders
	 *            排序
	 */
	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	/**
	 * 实现compareTo方法
	 *
	 * @param orderEntity
	 *            排序对象
	 * @return 比较结果
	 */
	public int compareTo(OrderEntity orderEntity) {
		return new CompareToBuilder().append(getOrders(), orderEntity.getOrders()).append(getId(), orderEntity.getId()).toComparison();
	}

}