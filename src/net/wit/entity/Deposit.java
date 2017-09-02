
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 账单记录
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "xm_deposit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_deposit_sequence")
public class Deposit extends BaseEntity {

	private static final long serialVersionUID = 903L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 钱包充值 */
		recharge,

		/** 消费支付 */
		payment,

		/** 消费退款 */
		refunds
	}

	/** 类型 */
	@Column(nullable = false, updatable = false)
	private Type type;

	/** 收入金额 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal credit;

	/** 支出金额 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal debit;

	/** 当前余额 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal balance;

	/** 操作员 */
	@Column(updatable = false)
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(updatable = false)
	private String memo;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 收款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;

	/** 退款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Refunds refunds;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取收入金额
	 * 
	 * @return 收入金额
	 */
	public BigDecimal getCredit() {
		return credit;
	}

	/**
	 * 设置收入金额
	 * 
	 * @param credit
	 *            收入金额
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	/**
	 * 获取支出金额
	 * 
	 * @return 支出金额
	 */
	public BigDecimal getDebit() {
		return debit;
	}

	/**
	 * 设置支出金额
	 * 
	 * @param debit
	 *            支出金额
	 */
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前余额
	 * 
	 * @return 当前余额
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置当前余额
	 * 
	 * @param balance
	 *            当前余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * 获取操作员
	 * 
	 * @return 操作员
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取收款单
	 * 
	 * @return 收款单
	 */
	public Payment getPayment() {
		return payment;
	}

	/**
	 * 设置收款单
	 * 
	 * @param payment
	 *            收款单
	 */
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Refunds getRefunds() {
		return refunds;
	}

	public void setRefunds(Refunds refunds) {
		this.refunds = refunds;
	}
}