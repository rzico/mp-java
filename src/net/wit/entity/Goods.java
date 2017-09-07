
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity - 货品
 * 
 */
@Entity
@Table(name = "wx_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_goods_sequence")
public class Goods extends BaseEntity {

	private static final long serialVersionUID = 901L;

	/** 商品 */
	private Set<Product> products = new HashSet<Product>();

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	@OneToMany(mappedBy = "goods", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * 
	 * @param products
	 *            商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}