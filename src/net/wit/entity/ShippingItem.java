
package net.wit.entity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

/**
 * Entity - 发货项

 */
@Entity
@Table(name = "wx_shipping_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_shipping_item_sequence")
public class ShippingItem extends BaseEntity {

	private static final long serialVersionUID = 52L;
	/** 缩例图 */
	private String thumbnail;

	/** 商品编号 */
	@NotEmpty
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '商品编号'")
	private String sn;

	/** 商品名称 */
	@NotEmpty
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '商品名称'")
	private String name;

	/** 商品规格 */
	@Column(updatable = false,columnDefinition="varchar(255) comment '商品规格'")
	private String spec;

	/** 数量 */
	@NotNull
	@Min(1)
	@Column(nullable = false, updatable = false,columnDefinition="int(11) not null comment '数量'")
	private Integer quantity;

	/** 销售价 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '销售价'")
	private BigDecimal price;

	/** 成本价 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '成本价'")
	private BigDecimal cost;

	/** 发货单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Shipping shipping;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Product product;

	/**
	 * 获取商品编号
	 * 
	 * @return 商品编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置商品编号
	 * 
	 * @param sn
	 *            商品编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

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
	 * 获取发货单
	 * 
	 * @return 发货单
	 */
	public Shipping getShipping() {
		return shipping;
	}

	/**
	 * 设置发货单
	 * 
	 * @param shipping
	 *            发货单
	 */
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
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

}