
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @ClassName: ArticleProduct
 * @Description:  绑定商品
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_product_sequence")
public class ArticleProduct extends BaseEntity {

	private static final long serialVersionUID = 9L;

	/** 文章 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '文章'")
	private Article article;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '商品'")
	private Product product;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}