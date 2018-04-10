
package net.wit.entity;

import net.wit.controller.model.GoodsListModel;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity - 货品
 * 
 */
@Entity
@Table(name = "wx_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_goods_sequence")
public class Goods extends BaseEntity {

	private static final long serialVersionUID = 26L;

	/** 商品 */
	@OneToMany(mappedBy = "goods", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Where(clause="deleted=0")
	@OrderBy("orders asc")
	private List<Product> products = new ArrayList<Product>();

	/** 公排 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '公排'")
	private Long ranking;

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * 
	 * @param products
	 *            商品
	 */
	public void setProducts(List<Product> products) {
		this.products = products;
	}


	public Product product() {
		Product product = null;
		for (Product p:getProducts()) {
			if (!p.getDeleted()) {
				product = p;
				break;
			}
		}
		return product;
	}

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}
}