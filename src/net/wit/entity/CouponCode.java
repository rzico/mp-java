package net.wit.entity;

import sun.jvm.hotspot.oops.BooleanField;

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

/**
 * Entity - 优惠码
 * 
 */
@Entity
@Table(name = "wx_coupon_code")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_coupon_code_sequence")
public class CouponCode extends BaseEntity {

	private static final long serialVersionUID = 22L;

	/** 号码 */
	@Column(nullable = false, updatable = false, unique = true, length = 100,columnDefinition="varchar(100) not null unique comment '号码'")
	private String code;

	/** 是否使用 */
	@Column(nullable = false,columnDefinition="bit not null comment '是否使用'")
	private Boolean isUsed;

	/** 使用日期 */
	@Column(columnDefinition="datetime comment '使用日期'")
	private Date usedDate;

	/** 优惠券 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Coupon coupon;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 订单 */
	@OneToOne(mappedBy = "couponCode", fetch = FetchType.LAZY)
	@JoinColumn(name = "orders")
	private Order order;

	/**
	 * 获取号码
	 * 
	 * @return 号码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置号码
	 * 
	 * @param code
	 *            号码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取是否已使用
	 * 
	 * @return 是否已使用
	 */
	public Boolean getIsUsed() {
		return isUsed;
	}

	/**
	 * 设置是否已使用
	 * 
	 * @param isUsed
	 *            是否已使用
	 */
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * 获取使用日期
	 * 
	 * @return 使用日期
	 */
	public Date getUsedDate() {
		return usedDate;
	}

	/**
	 * 设置使用日期
	 * 
	 * @param usedDate
	 *            使用日期
	 */
	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	/**
	 * 获取优惠券
	 * 
	 * @return 优惠券
	 */
	public Coupon getCoupon() {
		return coupon;
	}

	/**
	 * 设置优惠券
	 * 
	 * @param coupon
	 *            优惠券
	 */
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
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
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		if (getOrder() != null) {
			getOrder().setCouponCode(null);
		}
	}

	public Boolean getEnabled() {
		return !isUsed && getCoupon().hasExpired() && getCoupon().hasExpired() && !getCoupon().getDeleted();
	}

	public BigDecimal calculate(BigDecimal amount) {
      if (amount.compareTo(BigDecimal.ZERO)<0) {
          return  BigDecimal.ZERO;
	  }
	  if (!getEnabled()) {
		  return  BigDecimal.ZERO;
	  }
	  Coupon coupon = getCoupon();
      BigDecimal discount = BigDecimal.ZERO;
		if (coupon.getType().equals(Coupon.Type.fullcut)){
			if (amount.compareTo(coupon.getMinimumPrice()) >= 0) {
				discount = coupon.getAmount();
			}
		} else
		if (coupon.getType().equals(Coupon.Type.discount)){
			if (amount.compareTo(coupon.getMinimumPrice()) >= 0) {
				discount = amount.multiply(coupon.getAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			}
		}
		return discount.compareTo(amount)>0?amount:discount;
	}
}