
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 商品库存
 * 
 */

@Entity
@Table(name = "wx_product_stock")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_product_stock_sequence")
public class ProductStock extends BaseEntity {

	private static final long serialVersionUID = 902L;

	/** 库存 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '库存'")
	private Integer stock;

	/** 已分配库存 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '已分配库存'")
	private Integer allocatedStock;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	/** 卖家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member seller;

	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit comment '是否删除'")
	private Boolean deleted;

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getAllocatedStock() {
		return allocatedStock;
	}

	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Member getSeller() {
		return seller;
	}

	public void setSeller(Member seller) {
		this.seller = seller;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * 获取可用库存
	 *
	 * @return 可用库存
	 */
	@Transient
	public Integer getAvailableStock() {
		Integer availableStock = null;
		if (getStock() != null && getAllocatedStock() != null) {
			availableStock = getStock() - getAllocatedStock();
			if (availableStock < 0) {
				availableStock = 0;
			}
		}
		return availableStock;
	}

	/**
	 * 获取是否缺货
	 *
	 * @return 是否缺货
	 */
	@Transient
	public Boolean getIsOutOfStock() {
		return getStock() != null && getAllocatedStock() != null && getAllocatedStock() >= getStock();
	}

}