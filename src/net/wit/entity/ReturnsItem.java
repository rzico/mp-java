
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 退货项

 */
@Entity
@Table(name = "wx_returns_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_returns_item_sequence")
public class ReturnsItem extends BaseEntity {

	private static final long serialVersionUID = -4112374596087084162L;

	/** 商品编号 */
	@NotEmpty
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '商品编号'")
	private String sn;

	/** 商品名称 */
	@NotEmpty
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) not null comment '商品名称'")
	private String name;

	/** 数量 */
	@NotNull
	@Min(1)
	@Column(nullable = false, updatable = false,columnDefinition="int(11) not null comment '数量'")
	private Integer quantity;

	/** 退货单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Returns returns;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	/** 仓库 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private ProductStock productStock;

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
	 * 获取退货单
	 * 
	 * @return 退货单
	 */
	public Returns getReturns() {
		return returns;
	}

	/**
	 * 设置退货单
	 * 
	 * @param returns
	 *            退货单
	 */
	public void setReturns(Returns returns) {
		this.returns = returns;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductStock getProductStock() {
		return productStock;
	}

	public void setProductStock(ProductStock productStock) {
		this.productStock = productStock;
	}
}