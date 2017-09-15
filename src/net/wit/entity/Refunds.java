
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 退款单
 *
 * @author 降魔战队
 * @version 3.0
 */
@Entity
@Table(name = "wx_refunds")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_refunds_sequence")
public class Refunds extends BaseEntity {

	private static final long serialVersionUID = 119L;

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

		/** 等待退款 */
		waiting,

		/** 退款成功 */
		success,

		/** 退款失败 */
		failure
	}

	/** 编号 */
	@Column(columnDefinition="varchar(50) not null unique comment '编号'")
	private String sn;

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {payment:消费支付,recharge:钱包充值}'")
	private Type type;

	/** 方式 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '方式 {online:在线退款,offline:线下退款,deposit:钱包退款}'")
	private Method method;

	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {waiting:等待退款,success:退款成功,failure:退款失败}'")
	private Status status;

	/** 支付方式 */
	@Column(columnDefinition="varchar(255) comment '支付方式'")
	private String paymentMethod;

	/** 支付插件 */
	@Column(columnDefinition="varchar(255) comment '支付插件'")
	private String paymentPluginId;

	/** 退款金额 */
	@NotNull
	@Min(0)
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '退款金额'")
	private BigDecimal amount;

	/** 操作员 */
	@Column(columnDefinition="varchar(255) comment '操作员'")
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '备注'")
	private String memo;

	/** 账单记录 */
	@OneToOne(mappedBy = "refunds", fetch = FetchType.LAZY)
	private Deposit deposit;

	/** 付款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '会员'")
	private Member member;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

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

	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

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

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}
}