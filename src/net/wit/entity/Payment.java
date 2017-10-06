package net.wit.entity;

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

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 收款单
 *
 * @author 降魔战队
 * @version 3.0
 */
@Entity
@Table(name = "wx_payment")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_payment_sequence")
public class Payment extends BaseEntity {

	private static final long serialVersionUID = 116L;

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
		waiting,
		/** 支付成功 */
		success,
		/** 支付失败 */
		failure,
		/** 等待退款 */
		refund_waiting,
		/** 退款完成 */
		refund_success,
		/** 退款失败 */
		refund_failure

	}

	/** 编号 */
	@Column(columnDefinition="varchar(50) not null unique comment '编号'")
	private String sn;

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {payment:消费支付,recharge:钱包充值}'")
	private Type type;

	/** 方式 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '方式 {online:在线支付,offline:线下支付,deposit:钱包支付}'")
	private Method method;

	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {waiting:等待支付,success:支付成功,failure:支付失败,refund_waiting:等待支付,refund_success:支付成功,refund_failure:支付失败}'")
	private Status status;

	/** 支付方式 */
	@Column(columnDefinition="varchar(255) comment '支付方式'")
	private String paymentMethod;

	/** 支付插件 */
	@Column(columnDefinition="varchar(255) comment '支付插件'")
	private String paymentPluginId;

	/** 付款金额 */
	@NotNull
	@Min(0)
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '付款金额'")
	private BigDecimal amount;

	/** 操作员 */
	@Column(columnDefinition="varchar(255) comment '操作员'")
	private String operator;

	/** 付款日期 */
	@Column(columnDefinition="datetime comment '付款日期'")
	private Date paymentDate;

	/** 备注 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '备注'")
	private String memo;

	/** 到期时间 */
	@Column(columnDefinition="datetime comment '到期时间'")
	private Date expire;

	/** 账单记录 */
	@OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
	private Deposit deposit;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders",updatable = false)
	private Order order;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private ArticleReward articleReward;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ArticleReward getArticleReward() {
		return articleReward;
	}

	public void setArticleReward(ArticleReward articleReward) {
		this.articleReward = articleReward;
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


	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}
}