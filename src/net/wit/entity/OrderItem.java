package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 订单项
 * 
 */
@Entity
@Table(name = "wx_order_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_order_item_sequence")
public class OrderItem extends BaseEntity {

	private static final long serialVersionUID = 36L;

	/** 商品名称 */
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '商品名称'")
	private String name;

	/** 商品规格 */
	@Column(updatable = false,columnDefinition="varchar(255) comment '商品规格'")
	private String spec;

	/** 商品价格 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '商品价格'")
	private BigDecimal price;

	/** 商品重量 */
	@Column(updatable = false,columnDefinition="decimal(21,6) not null comment '商品重量'")
	private Integer weight;

	/** 商品缩略图 */
	@Column(updatable = false,columnDefinition="varchar(255) not null comment '商品缩略图'")
	private String thumbnail;

	/** 是否为赠品 */
	@Column(nullable = false, updatable = false,columnDefinition="bit not null comment '是否为赠品'")
	private Boolean isGift;

	/** 数量 */
	@NotNull
	@Min(1)
	@Max(10000)
	@Column(nullable = false,columnDefinition="int(11) not null comment '数量'")
	private Integer quantity;

	/** 已发货数量 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '已发货数量'")
	private Integer shippedQuantity;

	/** 已退货数量 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '已退货数量'")
	private Integer returnQuantity;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	/**
	 * 获取商品价格
	 * 
	 * @return 商品价格
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置商品价格
	 * 
	 * @param price
	 *            商品价格
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取商品重量
	 * 
	 * @return 商品重量
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * 设置商品重量
	 * 
	 * @param weight
	 *            商品重量
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * 获取商品缩略图
	 * 
	 * @return 商品缩略图
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * 设置商品缩略图
	 * 
	 * @param thumbnail
	 *            商品缩略图
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * 获取是否为赠品
	 * 
	 * @return 是否为赠品
	 */
	public Boolean getIsGift() {
		return isGift;
	}

	/**
	 * 设置是否为赠品
	 * 
	 * @param isGift
	 *            是否为赠品
	 */
	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取已发货数量
	 * 
	 * @return 已发货数量
	 */
	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	/**
	 * 设置已发货数量
	 * 
	 * @param shippedQuantity
	 *            已发货数量
	 */
	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	/**
	 * 获取已退货数量
	 * 
	 * @return 已退货数量
	 */
	@Column(nullable = false)
	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	/**
	 * 设置已退货数量
	 * 
	 * @param returnQuantity
	 *            已退货数量
	 */
	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * 
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 获取商品总重量
	 * 
	 * @return 商品总重量
	 */
	@JsonProperty
	@Transient
	public int getTotalWeight() {
		if (getWeight() != null && getQuantity() != null) {
			return getWeight() * getQuantity();
		} else {
			return 0;
		}
	}

	/**
	 * 获取小计
	 * 
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public BigDecimal getSubtotal() {
		if (getPrice() != null && getQuantity() != null) {
			return getPrice().multiply(new BigDecimal(getQuantity()));
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * 计算分润金额
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public BigDecimal calcPercent1() {
		Product product = getProduct();
		//第一级分润
		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend) ){
			BigDecimal d1 =
					getSubtotal().multiply(
							distribution.getPercent1().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			        d1 = d1.multiply(distribution.caleMoneyRate()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			return d1;
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 计算分润积分
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public Long calcPoint1() {
		Product product = getProduct();
		//第一级分润
		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend)) {
			BigDecimal d1 =
					getSubtotal().multiply(
							distribution.getPercent1().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			Long p1 = d1.multiply(distribution.calePointRate()).setScale(0,BigDecimal.ROUND_HALF_DOWN).longValue();
			return p1;
		} else {
			return 0L;
		}
	}

	/**
	 * 计算分润金额
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public BigDecimal calcPercent2() {
		Product product = getProduct();
		//第一级分润
		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend)) {
			BigDecimal d2 =
					getSubtotal().multiply(
							distribution.getPercent2().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			d2 = d2.multiply(distribution.caleMoneyRate()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			return d2;
		} else {
			return BigDecimal.ZERO;
		}
	}


	/**
	 * 计算分润积分
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public Long calcPoint2() {
		Product product = getProduct();
		//第一级分润
		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend)) {
			BigDecimal d2 =
					getSubtotal().multiply(
							distribution.getPercent2().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			Long p2 = d2.multiply(distribution.calePointRate()).setScale(0,BigDecimal.ROUND_HALF_DOWN).longValue();
			return p2;
		} else {
			return 0L;
		}
	}

	/**
	 * 计算分润金额
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public BigDecimal calcPercent3() {
		Product product = getProduct();
		//第一级分润
		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend)) {
			BigDecimal d3 =
					getSubtotal().multiply(
							distribution.getPercent3().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			d3 = d3.multiply(distribution.caleMoneyRate()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			return d3;
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 计算分润积分
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public Long calcPoint3() {
		Product product = getProduct();
		//第一级分润
		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend)) {
			BigDecimal d3 =
					getSubtotal().multiply(
							distribution.getPercent3().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			Long p3 = d3.multiply(distribution.calePointRate()).setScale(0,BigDecimal.ROUND_HALF_DOWN).longValue();
			return p3;
		} else {
			return 0L;
		}
	}

	/**
	 * 计算分红金额
	 *
	 * @return 小计
	 */
	@JsonProperty
	@Transient
	public BigDecimal calcPartner() {
		Product product = getProduct();

		Distribution distribution = product.getDistribution();
		if (distribution!=null && distribution.getType().equals(Distribution.Type.dividend)) {
			BigDecimal d =
					getSubtotal().multiply(
							distribution.getDividend().multiply(new BigDecimal("0.01")))
							.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			return d;
		} else {
			return BigDecimal.ZERO;
		}
	}

}