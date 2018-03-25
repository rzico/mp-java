
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity -  金币兑换
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "gm_gold_exchange")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "gm_gold_exchange_sequence")
public class GoldExchange extends BaseEntity {

	private static final long serialVersionUID = 23L;

	/** 状态 */
	public enum Status {
		/** 等待支付 */
		none,
		/** 支付成功 */
		success,
		/** 支付失败 */
		failure
	}


	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {none:待支付,success:支付成功,failure:支付失败}'")
	private Status status;

	/** 收益金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '收益金额'")
	private BigDecimal  amount;

	/** 兑换金币 */
	@Column(columnDefinition="bigint(20) not null comment '兑换金币'")
	private Long  gold;

	/** 操作员 */
	@Column(columnDefinition="varchar(255) comment '操作员'")
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '备注'")
	private String memo;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '备注'")
	private Member member;

	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit comment '是否删除'")
	private Boolean deleted;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getGold() {
		return gold;
	}

	public void setGold(Long gold) {
		this.gold = gold;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}