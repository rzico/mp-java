
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity -  金币产品
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "gm_gold_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "gm_gold_product_sequence")
public class GoldProduct extends BaseEntity {

	private static final long serialVersionUID = 23L;

	/** 产品标题 */
	@Column(columnDefinition="varchar(255) not null comment '产品标题'")
	private String title;

	/** 金币面额 */
	@Column(columnDefinition="bigint(20) default 0 comment '金币面额'")
	private Long gold;

	/** 销售价格 */
	@Column(columnDefinition="decimal(21,6) not null comment '销售价格'")
	private BigDecimal price;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getGold() {
		return gold;
	}

	public void setGold(Long gold) {
		this.gold = gold;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}