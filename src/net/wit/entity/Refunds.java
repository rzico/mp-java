
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 退款单
 *
 * @author 降魔战队
 * @version 3.0
 */
@Entity
@Table(name = "xm_refunds")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_refunds_sequence")
public class Refunds extends BaseEntity {

	private static final long serialVersionUID = 502L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 消费支付 */
		payment,

		/** 钱包充值 */
		recharge
	}


	/**
	 * 方式
	 */
	public enum Method {

		/** 在线退款 */
		online,

		/** 线下退款 */
		offline,

		/** 钱包退款 */
		deposit
	}

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待支付 */
		wait,

		/** 支付成功 */
		success,

		/** 支付失败 */
		failure
	}


	/** 类型 */
	@Column(nullable = false, updatable = false)
	private Type type;

	/** 编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 方式 */
	@NotNull
	@Column(nullable = false, updatable = false)
	private Method method;

	/** 状态 */
	@Column(updatable = false)
	private Status status;

	/** 支付方式 */
	@Column(updatable = false)
	private String paymentMethod;

	/** 退款金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal amount;

	/** 操作员 */
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(updatable = false)
	private String memo;

	/** 预存款 */
	@OneToOne(mappedBy = "refunds", fetch = FetchType.LAZY)
	private Deposit deposit;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}
}