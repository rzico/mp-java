package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 线下收单
 * 
 */
@Entity
@Table(name = "wx_pay_bill")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_pay_bill_sequence")
public class PayBill extends BaseEntity {

	private static final long serialVersionUID = 38L;

	/** 状态 */
	public enum Status {
		/** 等待支付 */
		none,
		/** 支付成功 */
		success,
		/** 支付失败 */
		failure,
		/** 退款中 */
		refund_waiting,
		/** 已退款 */
		refund_success
	}

	/**
	 * 结算方式
	 */
	public enum Method {

		/** 线上结算 */
		online,

		/** 线下结算 */
		offline
	}

	/**
	 * 类型
	 */
	public enum Type {
		/** 线下收款     */
		cashier,
		/** 线下退款    */
		cashierRefund,
		/** 会员卡充值     */
		card,
		/** 会员卡退款     */
		cardRefund
	}


	/** 业务日期 */
	@DateBridge(resolution = Resolution.DAY)
	@Column(updatable = false,columnDefinition="datetime not null comment '业务日期'")
	private Date billDate;

	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {none:线上结算,success:支付成功,failure:支付失败}'")
	private Status status;

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {cashier:线下收款,cashierRefund:线下退款,card:会员卡充值,cardRefund:会员卡退款}'")
	private Type type;

	/** 结算方式 */
	@Column(columnDefinition="int(11) not null comment '结算方式 {online:线上结算,offline:线下结算}'")
	private Method method;

	/** 结算插件 */
	@Column(columnDefinition="varchar(255) comment '结算插件'")
	private String paymentPluginId;

	/** 插件名称 */
	@Column(columnDefinition="varchar(255) comment '插件名称'")
	private String paymentPluginName;

	/** 消费者 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	/** 付款单 */
	@Column(nullable = false,columnDefinition="bigint(20) comment '付款单'")
	private Long payment;

	/** 退款单 */
	@Column(nullable = false,columnDefinition="bigint(20) comment '退款单'")
	private Long refunds;

	/** 收款账户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member owner;

	/** 收银员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Admin admin;

	/** 所属企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Enterprise enterprise;

	/** 所属门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Shop shop;

	/** 优惠券 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private CouponCode couponCode;

	/** 会员卡 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Card card;

	/** 优惠金额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '收单金额'")
	private BigDecimal couponDiscount;

	/** 会员卡抵扣 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '会员卡抵扣'")
	private BigDecimal cardDiscount;

	/** 消费金额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '消费金额'")
	private BigDecimal amount;

	/** 不参与优惠金额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '不参与优惠金额'")
	private BigDecimal noDiscount;

	/** 手续费 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '手续费'")
	private BigDecimal fee;

	/** 卡入账金额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '卡入账金额'")
	private BigDecimal cardAmount;

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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getOwner() {
		return owner;
	}

	public void setOwner(Member owner) {
		this.owner = owner;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public CouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public BigDecimal getCardDiscount() {
		return cardDiscount;
	}

	public void setCardDiscount(BigDecimal cardDiscount) {
		this.cardDiscount = cardDiscount;
	}

	public BigDecimal getNoDiscount() {
		return noDiscount;
	}

	public void setNoDiscount(BigDecimal noDiscount) {
		this.noDiscount = noDiscount;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Long getPayment() {
		return payment;
	}

	public void setPayment(Long payment) {
		this.payment = payment;
	}

	public Long getRefunds() {
		return refunds;
	}

	public void setRefunds(Long refunds) {
		this.refunds = refunds;
	}

	public BigDecimal getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(BigDecimal cardAmount) {
		this.cardAmount = cardAmount;
	}

	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	public String getPaymentPluginName() {
		return paymentPluginName;
	}

	public void setPaymentPluginName(String paymentPluginName) {
		this.paymentPluginName = paymentPluginName;
	}

	//有效发生金额
	public BigDecimal getPayBillAmount() {
		return this.getAmount().subtract(this.getCouponDiscount()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
	}
	//有效付款金额
	public BigDecimal getEffectiveAmount() {
		return this.getAmount().subtract(this.getCouponDiscount()).subtract(this.getCardDiscount()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
	}

	//商户结算金额
	public BigDecimal getSettleAmount() {
		if (getMethod().equals(Method.offline)) {
			return BigDecimal.ZERO;
		} else {
			return this.getEffectiveAmount().subtract(this.getFee()).setScale(4,BigDecimal.ROUND_HALF_DOWN);
		}
	}

}