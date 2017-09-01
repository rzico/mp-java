package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 收款单
 *
 * @author 降魔战队
 * @version 3.0
 */
@Entity
@Table(name = "xm_payment")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_payment_sequence")
public class Payment extends BaseEntity {

	private static final long serialVersionUID = 111L;

	/** 支付方式分隔符 */
	public static final String PAYMENT_METHOD_SEPARATOR = " - ";

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

		/** 在线支付 */
		online,

		/** 线下支付 */
		offline,

		/** 钱包支付 */
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

	/** 编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 类型 */
	@Column(nullable = false, updatable = false)
	private Type type;

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

	/** 付款金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal amount;

	/** 操作员 */
	@Column(updatable = false)
	private String operator;

	/** 付款日期 */
	private Date paymentDate;

	/** 备注 */
	@Length(max = 200)
	private String memo;

	/** 支付插件ID */
	@JoinColumn(updatable = false)
	private String paymentPluginId;

	/** 到期时间 */
	@JoinColumn(updatable = false)
	private Date expire;

	/** 预存款 */
	@OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
	private Deposit deposit;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public net.shopxx.entity.Payment.Type getType() {
		return type;
	}

	public void setType(net.shopxx.entity.Payment.Type type) {
		this.type = type;
	}

	public net.shopxx.entity.Payment.Method getMethod() {
		return method;
	}

	public void setMethod(net.shopxx.entity.Payment.Method method) {
		this.method = method;
	}

	public net.shopxx.entity.Payment.Status getStatus() {
		return status;
	}

	public void setStatus(net.shopxx.entity.Payment.Status status) {
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

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		if (getDeposit() != null) {
			getDeposit().setPayment(null);
		}
	}

}