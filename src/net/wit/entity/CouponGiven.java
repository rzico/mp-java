package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity - 转赠
 * 
 */
@Entity
@Table(name = "wx_coupon_given")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_coupon_given_sequence")
public class CouponGiven extends BaseEntity {

	private static final long serialVersionUID = 22L;

	/** 优惠券 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	@JsonIgnore
	private Coupon coupon;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member given;

	/** 数量 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '数量'")
	private Long quantity;

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getGiven() {
		return given;
	}

	public void setGiven(Member given) {
		this.given = given;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}