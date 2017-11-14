package net.wit.entity;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
		failure
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

	/** 业务日期 */
	@DateBridge(resolution = Resolution.DAY)
	@Column(updatable = false,columnDefinition="datetime not null comment '业务日期'")
	private Date billDate;

	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {none:线上结算,success:支付成功,failure:支付失败}'")
	private Status status;

	/** 结算方式 */
	@Column(columnDefinition="int(11) not null comment '结算方式 {online:线上结算,offline:线下结算}'")
	private Method method;

	/** 消费者 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 付款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;

	/** 收款账户 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member owner;

	/** 所属企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Enterprise enterprise;

	/** 所属门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Shop shop;

	/** 优惠券 */
	@ManyToOne(fetch = FetchType.LAZY)
	private CouponCode couponCode;

	/** 会员卡 */
	@ManyToOne(fetch = FetchType.LAZY)
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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	//有效付款金额
	public BigDecimal getEffectiveAmount() {
		return this.getAmount().subtract(this.getCouponDiscount()).subtract(this.getCardDiscount());
	}

	//商户结算金额
	public BigDecimal getSettleAmount() {
		if (getMethod().equals(Method.offline)) {
			return BigDecimal.ZERO;
		} else {
			return this.getEffectiveAmount().subtract(this.getFee());
		}
	}

}