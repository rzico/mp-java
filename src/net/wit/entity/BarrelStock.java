package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 非卖品
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_barrel_stock")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_barrel_stock_sequence")
public class BarrelStock extends OrderEntity {

	private static final long serialVersionUID = 2L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/**  会员 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Card card;

	/**  实物 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Barrel barrel;

	/** 库存数 */
	@Min(0)
	@Column(columnDefinition="int(11) not null default 0 comment '库存数'")
	private Integer stock;

	/** 期初数 */
	@Min(0)
	@Column(columnDefinition="int(11) not null default 0 comment '期初数'")
	private Integer period ;

	/** 桶押金 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '桶押金'")
	private BigDecimal pledge ;

	public Barrel getBarrel() {
		return barrel;
	}

	public void setBarrel(Barrel barrel) {
		this.barrel = barrel;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getPledge() {
		return pledge;
	}

	public void setPledge(BigDecimal pledge) {
		this.pledge = pledge;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
}