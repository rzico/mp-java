package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 订单项
 *
 */

@Entity
@Table(name = "wx_order_barrel")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_order_barrel_sequence")
public class OrderBarrel extends BaseEntity {

	private static final long serialVersionUID = 36L;

	/** 名称 */
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 数量 */
	@NotNull
	@Column(nullable = false,columnDefinition="int(11) not null comment '数量'")
	private Integer quantity;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Barrel barrel;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	/**
	 * 获取商品名称
	 * 
	 * @return 商品名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置商品名称
	 * 
	 * @param name
	 *            商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Barrel getBarrel() {
		return barrel;
	}

	public void setBarrel(Barrel barrel) {
		this.barrel = barrel;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}