
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @ClassName: Rebate
 * @Description:  专题费用
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_rebate")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_rebate_sequence")
public class Rebate extends BaseEntity {

	private static final long serialVersionUID = 59L;

	/** 推广者 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '推广者'")
	private Member member;

	/** 合作商 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '合作商'")
	private Enterprise enterprise;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders",updatable = false)
	@JsonIgnore
	private Order order;

	/** 消费金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '消费金额'")
	private BigDecimal amount;

	/** 直接佣金 */
	@Column(columnDefinition="decimal(21,6) not null comment '直接佣金'")
	private BigDecimal direct;

	/** 间接佣金 */
	@Column(columnDefinition="decimal(21,6) not null comment '间接佣金'")
	private BigDecimal indirect;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDirect() {
		return direct;
	}

	public void setDirect(BigDecimal direct) {
		this.direct = direct;
	}

	public BigDecimal getIndirect() {
		return indirect;
	}

	public void setIndirect(BigDecimal indirect) {
		this.indirect = indirect;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

}