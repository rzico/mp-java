package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * Entity - 订单项
 *
 */

@Entity
@Table(name = "wx_shipping_barrel")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_shipping_barrel_sequence")
public class ShippingBarrel extends BaseEntity {

	private static final long serialVersionUID = 36L;

	/** 名称 */
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 数量 */
	@NotNull
	@Column(nullable = false,columnDefinition="int(11) not null comment '数量'")
	private Integer quantity;

	/** 数量 */
	@NotNull
	@Column(nullable = false,columnDefinition="int(11) not null comment '数量'")
	private Integer returnQuantity;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Barrel barrel;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	/** 送货单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipping", nullable = false, updatable = false)
	private Shipping shipping;

	/** 卖家 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member seller;

	/** 买家 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 配送单位 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Enterprise enterprise;

	/** 配送门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Shop shop;

	/** 送货员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Admin admin;

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

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public Member getSeller() {
		return seller;
	}

	public void setSeller(Member seller) {
		this.seller = seller;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}