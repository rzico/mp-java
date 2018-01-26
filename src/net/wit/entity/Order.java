
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 订单
 * 
 */
@Entity
@Table(name = "wx_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_order_sequence")
public class Order extends BaseEntity {

	private static final long serialVersionUID = 34L;

	/** 订单名称分隔符 */
	private static final String NAME_SEPARATOR = " ";

	/**
	 * 结算方式
	 */
	public enum PaymentMethod {

		/** 线上结算 */
		online,

		/** 线下结算 */
		offline,

		/** 余额支付 */
		deposit,

		/** 会员卡 */
		card
	}

	/**
	 * 配送方式
	 */
	public enum ShippingMethod {

		/** 卖家配送 */
		shipping,

		/** 线下提货 */
		pickup
	}

	/**
	 * 订单状态
	 */
	public enum OrderStatus {

		/** 未确认 */
		unconfirmed,

		/** 已确认 */
		confirmed,

		/** 已完成 */
		completed,

		/** 已取消 */
		cancelled
	}

	/**
	 * 支付状态
	 */
	public enum PaymentStatus {

		/** 未支付 */
		unpaid,

		/** 已支付 */
		paid,

		/** 退款中 */
		refunding,

		/** 已退款 */
		refunded
	}

	/**
	 * 配送状态
	 */
	public enum ShippingStatus {

		/** 未发货 */
		unshipped,

		/** 已发货 */
		shipped,

		/** 退货中 */
		returning,

		/** 已退货 */
		returned
	}

	/** 订单编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100,columnDefinition="varchar(100) not null unique comment '订单编号'")
	private String sn;

	/** 订单状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '订单状态'")
	private OrderStatus orderStatus;

	/** 支付状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '支付状态'")
	private PaymentStatus paymentStatus;

	/** 配送状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '配送状态'")
	private ShippingStatus shippingStatus;

	/** 付款方式 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '付款方式'")
	private PaymentMethod paymentMethod;

	/** 配送方式 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '配送方式'")
	private ShippingMethod shippingMethod;

	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit comment '是否删除'")
	@JsonIgnore
	private Boolean deleted;

	/** 交易佣金 */
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '交易佣金'")
	private BigDecimal fee;

	/** 运费 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '运费'")
	private BigDecimal freight;

	/** 积分抵扣 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '积分抵扣'")
	private BigDecimal pointDiscount;

	/** 优惠券折扣 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '优惠券折扣'")
	private BigDecimal couponDiscount;

	/** 调整金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '调整金额'")
	private BigDecimal offsetAmount;

	/** 已付金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '已付金额'")
	private BigDecimal amountPaid;

	/** 分销佣金 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '分销佣金'")
	private BigDecimal rebateAmount;

	/** 赠送积分 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null comment '赠送积分'")
	private Long point;

	/** 收货人 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '收货人'")
	private String consignee;

	/** 地区名称 */
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '地区名称'")
	private String areaName;

	/** 地址 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '地址'")
	private String address;

	/** 邮编 */
	@NotEmpty
	@Length(max = 6)
	@Column(nullable = true,columnDefinition="varchar(255) comment '邮编'")
	private String zipCode;

	/** 电话 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '联系电话'")
	private String phone;

	/** 买家留言 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '买家留言'")
	private String memo;

	/** 到期时间 */
	@Column(columnDefinition="datetime comment '到期时间'")
	private Date expire;

	/** 锁定到期时间 */
	@Column(columnDefinition="datetime comment '锁定到期时间'")
	private Date lockExpire;

	/** 操作人 */
	@Column(columnDefinition="varchar(255) comment '操作人'")
	private String operator;

	/** 是否已分配库存 */
	@Column(nullable = false,columnDefinition="bit comment '是否已分配库存'")
	private Boolean isAllocatedStock;

	/** 地区 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Area area;

	/** 买家 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 卖家 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member seller;

	/** 推广 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member promoter;

	/** 是否已分配佣金 */
	@Column(nullable = false,columnDefinition="bit comment '是否分配佣金'")
	private Boolean isDistribution;

	/** 优惠码 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private CouponCode couponCode;

	/** 优惠券 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_order_coupon")
	private List<Coupon> coupons = new ArrayList<Coupon>();

	/** 订单项 */
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("isGift asc")
	@JsonIgnore
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	/** 订单日志 */
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private List<OrderLog> orderLogs = new ArrayList<OrderLog>();

	/** 收款单 */
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Payment> payments = new HashSet<Payment>();

	/** 退款单 */
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Refunds> refunds = new HashSet<Refunds>();

	/**
	 * 获取订单编号
	 * 
	 * @return 订单编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置订单编号
	 * 
	 * @param sn
	 *            订单编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取订单状态
	 * 
	 * @return 订单状态
	 */
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * 设置订单状态
	 * 
	 * @param orderStatus
	 *            订单状态
	 */
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}


	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取支付状态
	 * 
	 * @return 支付状态
	 */
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * 设置支付状态
	 * 
	 * @param paymentStatus
	 *            支付状态
	 */
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public BigDecimal getPointDiscount() {
		return pointDiscount;
	}

	public void setPointDiscount(BigDecimal pointDiscount) {
		this.pointDiscount = pointDiscount;
	}

	/**
	 * 获取配送状态
	 * 
	 * @return 配送状态
	 */
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	/**
	 * 设置配送状态
	 * 
	 * @param shippingStatus
	 *            配送状态
	 */
	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	/**
	 * 获取支付手续费
	 * 
	 * @return 支付手续费
	 */
	public BigDecimal getFee() {
		return fee;
	}

	/**
	 * 设置支付手续费
	 * 
	 * @param fee
	 *            支付手续费
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(BigDecimal rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	/**
	 * 获取运费
	 * 
	 * @return 运费
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置运费
	 * 
	 * @param freight
	 *            运费
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	/**
	 * 获取优惠券折扣
	 * 
	 * @return 优惠券折扣
	 */
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	/**
	 * 设置优惠券折扣
	 * 
	 * @param couponDiscount
	 *            优惠券折扣
	 */
	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	/**
	 * 获取调整金额
	 * 
	 * @return 调整金额
	 */
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	/**
	 * 设置调整金额
	 * 
	 * @param offsetAmount
	 *            调整金额
	 */
	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	/**
	 * 获取已付金额
	 * 
	 * @return 已付金额
	 */
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	/**
	 * 设置已付金额
	 * 
	 * @param amountPaid
	 *            已付金额
	 */
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	/**
	 * 获取赠送积分
	 * 
	 * @return 赠送积分
	 */
	public Long getPoint() {
		return point;
	}

	/**
	 * 设置赠送积分
	 * 
	 * @param point
	 *            赠送积分
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取收货人
	 * 
	 * @return 收货人
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 设置收货人
	 * 
	 * @param consignee
	 *            收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 获取地区名称
	 * 
	 * @return 地区名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * 
	 * @param areaName
	 *            地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取地址
	 * 
	 * @return 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * 
	 * @param address
	 *            地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * 
	 * @return 邮编
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * 
	 * @param zipCode
	 *            邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * 
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * 
	 * @param phone
	 *            电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取附言
	 * 
	 * @return 附言
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置附言
	 * 
	 * @param memo
	 *            附言
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setIsDistribution(Boolean distribution) {
		isDistribution = distribution;
	}

	public Boolean getIsDistribution() {
		return isDistribution;
	}

	/**
	 * 获取到期时间
	 * 
	 * @return 到期时间
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * 设置到期时间
	 * 
	 * @param expire
	 *            到期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取锁定到期时间
	 * 
	 * @return 锁定到期时间
	 */
	public Date getLockExpire() {
		return lockExpire;
	}

	/**
	 * 设置锁定到期时间
	 * 
	 * @param lockExpire
	 *            锁定到期时间
	 */
	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	/**
	 * 获取是否已分配库存
	 * 
	 * @return 是否已分配库存
	 */
	public Boolean getIsAllocatedStock() {
		return isAllocatedStock;
	}

	/**
	 * 设置是否已分配库存
	 * 
	 * @param isAllocatedStock
	 *            是否已分配库存
	 */
	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	/**
	 * 获取地区
	 * 
	 * @return 地区
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	public void setArea(Area area) {
		this.area = area;
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

	public Member getSeller() {
		return seller;
	}

	public void setSeller(Member seller) {
		this.seller = seller;
	}

	/**
	 * 获取优惠码
	 * 
	 * @return 优惠码
	 */
	public CouponCode getCouponCode() {
		return couponCode;
	}

	/**
	 * 设置优惠码
	 * 
	 * @param couponCode
	 *            优惠码
	 */
	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * 获取优惠券
	 * 
	 * @return 优惠券
	 */
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * 设置优惠券
	 * 
	 * @param coupons
	 *            优惠券
	 */
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * 获取订单项
	 * 
	 * @return 订单项
	 */
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * 设置订单项
	 * 
	 * @param orderItems
	 *            订单项
	 */
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * 获取订单日志
	 * 
	 * @return 订单日志
	 */
	public List<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	/**
	 * 设置订单日志
	 * 
	 * @param orderLogs
	 *            订单日志
	 */
	public void setOrderLogs(List<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	/**
	 * 获取收款单
	 * 
	 * @return 收款单
	 */
	public Set<Payment> getPayments() {
		return payments;
	}

	/**
	 * 设置收款单
	 * 
	 * @param payments
	 *            收款单
	 */
	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * 获取退款单
	 * 
	 * @return 退款单
	 */
	public Set<Refunds> getRefunds() {
		return refunds;
	}

	/**
	 * 设置退款单
	 * 
	 * @param refunds
	 *            退款单
	 */
	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public Member getPromoter() {
		return promoter;
	}

	public void setPromoter(Member promoter) {
		this.promoter = promoter;
	}

	/**
	 * 获取订单名称
	 * 
	 * @return 订单名称
	 */
	@Transient
	public String getName() {
		StringBuffer name = new StringBuffer();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getName() != null) {
					name.append(NAME_SEPARATOR).append(orderItem.getName());
				}
			}
			if (name.length() > 0) {
				name.deleteCharAt(0);
			}
		}
		return name.toString();
	}

	/**
	 * 获取商品重量
	 * 
	 * @return 商品重量
	 */
	@Transient
	public int getWeight() {
		int weight = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					weight += orderItem.getTotalWeight();
				}
			}
		}
		return weight;
	}

	/**
	 * 获取商品数量
	 * 
	 * @return 商品数量
	 */
	@Transient
	public int getQuantity() {
		int quantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getQuantity() != null) {
					quantity += orderItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	/**
	 * 获取已发货数量
	 * 
	 * @return 已发货数量
	 */
	@Transient
	public int getShippedQuantity() {
		int shippedQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getShippedQuantity() != null) {
					shippedQuantity += orderItem.getShippedQuantity();
				}
			}
		}
		return shippedQuantity;
	}

	/**
	 * 获取已退货数量
	 * 
	 * @return 已退货数量
	 */
	@Transient
	public int getReturnQuantity() {
		int returnQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getReturnQuantity() != null) {
					returnQuantity += orderItem.getReturnQuantity();
				}
			}
		}
		return returnQuantity;
	}

	/**
	 * 获取商品价格
	 * 
	 * @return 商品价格
	 */
	@Transient
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getSubtotal() != null) {
					price = price.add(orderItem.getSubtotal());
				}
			}
		}
		return price;
	}

	/**
	 * 获取订单金额
	 * 
	 * @return 订单金额
	 */
	@Transient
	public BigDecimal getAmount() {
		BigDecimal amount = getPrice();
//		if (getFee() != null) {
//			amount = amount.add(getFee());
//		}
		if (getFreight() != null) {
			amount = amount.add(getFreight());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}

	/**
	 * 获取分销佣金
	 *
	 * @return 分销佣金
	 */
	@Transient
	public BigDecimal getDistribution() {
		BigDecimal d = BigDecimal.ZERO;
		if (getPromoter()!=null && getPromoter().leaguer(getSeller())) {
			if (getOrderItems() != null) {
				for (OrderItem orderItem : getOrderItems()) {
					if (orderItem != null && orderItem.getSubtotal() != null) {
						d = d.add(orderItem.calcPercent1());
						Card c2 = getPromoter().card(getSeller());
						Member p2 = c2.getPromoter();
						if (p2!=null  && p2.leaguer(getSeller()) ) {
							d = d.add(orderItem.calcPercent2());
							Card c3 = p2.card(getSeller());
							Member p3 = c3.getPromoter();
							if (p3!=null  && p3.leaguer(getSeller())) {
								d = d.add(orderItem.calcPercent3());
							}
						}
					}
				}
			}
		}
		return d;
	}

	/**
	 * 获取应付金额
	 * 
	 * @return 应付金额
	 */
	@Transient
	public BigDecimal getAmountPayable() {
		BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
		return amountPayable.compareTo(new BigDecimal(0)) > 0 ? amountPayable : new BigDecimal(0);
	}

	/**
	 * 是否已过期  true 过期了
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean isExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/**
	 * 判断是否已锁定
	 * 
	 * @param operator
	 *            操作员
	 * @return 是否已锁定
	 */
	@Transient
	public boolean isLocked(String operator) {
		return getLockExpire() != null && new Date().before(getLockExpire()) && ((operator != null && !operator.equals(getOperator())) || (operator == null && getOperator() != null));
	}

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
	}


	public String getStatusDescr() {
		if (getOrderStatus().equals(OrderStatus.unconfirmed) && getPaymentStatus().equals(PaymentStatus.unpaid)) {
			return "待付款";
		} else
		if (getOrderStatus().equals(OrderStatus.unconfirmed) && getPaymentStatus().equals(PaymentStatus.paid)) {
			return "待确定";
		} else
		if (getOrderStatus().equals(OrderStatus.cancelled)) {
			return "已关闭";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getShippingStatus().equals(ShippingStatus.returning)) {
			return "退货中";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getPaymentStatus().equals(PaymentStatus.refunding)) {
			return "退款中";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getShippingStatus().equals(ShippingStatus.unshipped)) {
			return "待发货";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getShippingStatus().equals(ShippingStatus.shipped)) {
			return "已发货";
		} else
		if (getOrderStatus().equals(OrderStatus.completed) && getShippingStatus().equals(ShippingStatus.returned) ) {
			return "已退货";
		} else
		if (getOrderStatus().equals(OrderStatus.completed) && getPaymentStatus().equals(PaymentStatus.refunded) ) {
			return "已退款";
		} else {
			return "已完成";
		}
	}
	
	public String getStatus() {
		if (getOrderStatus().equals(OrderStatus.unconfirmed) && getPaymentStatus().equals(PaymentStatus.unpaid)) {
			return "unpaid";
		} else
		if (getOrderStatus().equals(OrderStatus.unconfirmed) && getPaymentStatus().equals(PaymentStatus.paid)) {
			return "unshipped";
		} else
		if (getOrderStatus().equals(OrderStatus.cancelled)) {
			return "completed";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getShippingStatus().equals(ShippingStatus.returning)) {
			return "returning";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getPaymentStatus().equals(PaymentStatus.refunding)) {
			return "refunding";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getShippingStatus().equals(ShippingStatus.unshipped)) {
			return "unshipped";
		} else
		if (getOrderStatus().equals(OrderStatus.confirmed) && getShippingStatus().equals(ShippingStatus.shipped)) {
			return "shipped";
		} else
		if (getOrderStatus().equals(OrderStatus.completed) && getShippingStatus().equals(ShippingStatus.returned) ) {
			return "returned";
		} else
		if (getOrderStatus().equals(OrderStatus.completed) && getPaymentStatus().equals(PaymentStatus.refunded) ) {
			return "refunded";
		} else {
			return "completed";
		}
	}

}