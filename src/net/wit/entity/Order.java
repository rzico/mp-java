
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
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

		/** 普通快递 */
		shipping,

		/** 到店提货 */
		pickup,

		/** 统仓统配 */
		warehouse,

		/** 存入卡包 */
		cardbkg
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

	/** 结算插件 */
	@Column(columnDefinition="varchar(255) comment '结算插件'")
	private String paymentPluginId;

	/** 清算方式 */
	@Column(columnDefinition="int(11) not null default 0 comment '方向 {yundian:代付,merchant:直清}'")
	private Payment.Way way;

	/** 插件名称 */
	@Column(columnDefinition="varchar(255) comment '插件名称'")
	private String paymentPluginName;

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
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '运费'")
	private BigDecimal freight;

	/** 配送费用 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '配送费用'")
	private BigDecimal shippingFreight;

	/** 配送工资 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '配送工资'")
	private BigDecimal adminFreight;

	/** 楼层费 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '楼层费'")
	private BigDecimal levelFreight;

	/** 积分抵扣 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '积分抵扣'")
	private BigDecimal pointDiscount;

	/** 优惠券折扣 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '优惠券折扣'")
	private BigDecimal couponDiscount;


	/** 提货券抵扣 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '提货券抵扣'")
	private BigDecimal exchangeDiscount;


	/** 调整金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '调整金额'")
	private BigDecimal offsetAmount;

	/** 应付金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '应付金额'")
	private BigDecimal amountPayable;

	/** 已付金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '已付金额'")
	private BigDecimal amountPaid;

	/** 分销佣金 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '分销佣金'")
	private BigDecimal rebateAmount;

	/** 股东分红 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '股东分红'")
	private BigDecimal partnerAmount;

	/** 赠送积分 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '赠送积分'")
	private Long point;

	/** 收货地址 id */
	@Column(columnDefinition="bigint(20) not null comment '收货地址 id'")
	private Long receiverId;

	/** 来源编码 */
	@Column(columnDefinition="varchar(50) comment '来源编码'")
	private String groupNo;

	/** 接龙 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Dragon dragon;

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

	/** 定位 */
	@Embedded
	private Location location;

	/** 预约时间 */
	@Column(columnDefinition="datetime comment '预约时间'")
	private Date hopeDate;

	/** 发货时间 */
	@Column(columnDefinition="datetime comment '发货时间'")
	private Date shippingDate;

	/** 退货时间 */
	@Column(columnDefinition="datetime comment '退货时间'")
	private Date returnedDate;

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

	/** 股东 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member partner;

	/** 推广员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Enterprise personal;

	/** 合作商 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Enterprise agent;

	/** 代理商 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Enterprise operate;

	/** 是否已分配佣金 */
	@Column(nullable = false,columnDefinition="bit comment '是否分配佣金'")
	private Boolean isDistribution;

	/** 是否已分配佣金 */
	@Column(nullable = false,columnDefinition="bit comment '是否分配佣金'")
	private Boolean isPartner;

	/** 运单号 */
	@Column(columnDefinition="varchar(255) comment '运单号'")
	private String trackingNo;

	/** 优惠码 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private CouponCode couponCode;

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
	private List<Payment> payments = new ArrayList<Payment>();

	/** 退款单 */
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private List<Refunds> refunds = new ArrayList<Refunds>();

	/** 账单 */
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private List<Deposit> deposits = new ArrayList<Deposit>();

	/** 配送单 */
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private List<Shipping> shippings = new ArrayList<>();

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public Dragon getDragon() {
		return dragon;
	}

	public void setDragon(Dragon dragon) {
		this.dragon = dragon;
	}


	public Payment.Way getWay() {
		return way;
	}

	public void setWay(Payment.Way way) {
		this.way = way;
	}

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

	public Date getHopeDate() {
		return hopeDate;
	}

	public void setHopeDate(Date hopeDate) {
		this.hopeDate = hopeDate;
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

	public List<Shipping> getShippings() {
		return shippings;
	}

	public void setShippings(List<Shipping> shippings) {
		this.shippings = shippings;
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

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Date getReturnedDate() {
		return returnedDate;
	}

	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
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


	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
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
	public List<Payment> getPayments() {
		return payments;
	}

	/**
	 * 设置收款单
	 *
	 * @param payments
	 *            收款单
	 */
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * 获取退款单
	 *
	 * @return 退款单
	 */
	public List<Refunds> getRefunds() {
		return refunds;
	}

	/**
	 * 设置退款单
	 *
	 * @param refunds
	 *            退款单
	 */
	public void setRefunds(List<Refunds> refunds) {
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

	public BigDecimal getPartnerAmount() {
		return partnerAmount;
	}

	public void setPartnerAmount(BigDecimal partnerAmount) {
		this.partnerAmount = partnerAmount;
	}

	public void setPartner(Member partner) {
		this.partner = partner;
	}

	public void setIsPartner(Boolean isPartner) {
		this.isPartner = isPartner;
	}

	public Boolean getIsPartner() {
	    return this.isPartner;
	}

	public Member getPartner() {
		return partner;
	}

	public Enterprise getPersonal() {
		return personal;
	}

	public void setPersonal(Enterprise personal) {
		this.personal = personal;
	}

	public Enterprise getAgent() {
		return agent;
	}

	public void setAgent(Enterprise agent) {
		this.agent = agent;
	}

	public Enterprise getOperate() {
		return operate;
	}

	public void setOperate(Enterprise operate) {
		this.operate = operate;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public BigDecimal getExchangeDiscount() {
		return exchangeDiscount;
	}

	public void setExchangeDiscount(BigDecimal exchangeDiscount) {
		this.exchangeDiscount = exchangeDiscount;
	}

	public BigDecimal getShippingFreight() {
		return shippingFreight;
	}

	public void setShippingFreight(BigDecimal shippingFreight) {
		this.shippingFreight = shippingFreight;
	}

	public BigDecimal getAdminFreight() {
		return adminFreight;
	}

	public void setAdminFreight(BigDecimal adminFreight) {
		this.adminFreight = adminFreight;
	}

	public BigDecimal getLevelFreight() {
		return levelFreight;
	}

	public void setLevelFreight(BigDecimal levelFreight) {
		this.levelFreight = levelFreight;
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
	 * 获取电子券数量
	 *
	 * @return 获取电子券数量
	 */
	@Transient
	public int getExchangeQuantity() {
		int exchangeQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getCouponQuantity() != null) {
					exchangeQuantity += orderItem.getCouponQuantity();
				}
			}
		}
		return exchangeQuantity;
	}

    /**
     * 获取电子券余额
     *
     * @return 获取电子券余额
     */
    @Transient
    public int getExchangeBalance() {
        int exchangeQuantity = 0;
        if (getOrderItems() != null) {
            for (OrderItem orderItem : getOrderItems()) {
                if (orderItem != null && orderItem.getCouponCode() != null && orderItem.getPrice().compareTo(BigDecimal.ZERO)>0) {
                    exchangeQuantity += orderItem.getCouponCode().getStock();
                }
            }
        }
        return exchangeQuantity;
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

	@Transient
	public BigDecimal calcFreight(Receiver receiver) {
		BigDecimal price = BigDecimal.ZERO;
		if (receiver!=null && receiver.getLevel()!=null && receiver.getLevel()>4) {
			if (getOrderItems() != null) {
				for (OrderItem orderItem : getOrderItems()) {
					if (orderItem != null && orderItem.getQuantity() != null) {
						price = price.add(
								new BigDecimal(
										orderItem.getQuantity() * (receiver.getLevel() - 4)
								)
						);
					}
				}
			}
		}
		return price;
	}

	/**
	 * 获取分销商品价格
	 *
	 * @return 获取分销商品价格
	 */
	@Transient
	public BigDecimal getDistPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getSubtotal() != null && orderItem.getProduct()!=null && orderItem.getProduct().getDistribution()!=null) {
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

		if (getFreight() != null) {
			amount = amount.add(getFreight());
		}

		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}

	/**
	 * 获取订单金额
	 *
	 * @return 订单金额
	 */
	@Transient
	public BigDecimal getCost() {
		BigDecimal price = new BigDecimal(0);
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					price = price.add(orderItem.getCost().multiply(new BigDecimal(orderItem.getQuantity())));
				}
			}
		}
		return price;
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
						if (c2!=null) {
							Member p2 = c2.getPromoter();
							if (p2 != null && p2.leaguer(getSeller())) {
								d = d.add(orderItem.calcPercent2());
								Card c3 = p2.card(getSeller());
								if (c3!=null) {
									Member p3 = c3.getPromoter();
									if (p3 != null && p3.leaguer(getSeller())) {
										d = d.add(orderItem.calcPercent3());
									}
								}
							}
						}
					}
				}
			}
		}
		return d;
	}

	/**
	 * 获取股东红利
	 *
	 * @return 获取股东红利
	 */
	@Transient
	public BigDecimal calcPartner() {
		BigDecimal d = BigDecimal.ZERO;
		if (getPartner()!=null ) {
			if (getOrderItems() != null) {
				for (OrderItem orderItem : getOrderItems()) {
					if (orderItem != null && orderItem.getSubtotal() != null) {
						d = d.add(orderItem.calcPartner());
					}
				}
			}
		}
		return d;
	}

	public void setAmountPayable(BigDecimal amountPayable) {
		this.amountPayable = amountPayable;
	}

	public BigDecimal getAmountPayable() {
		return amountPayable;
	}

	/**
	 * 获取应付金额
	 *
	 * @return 应付金额
	 */
	@Transient
	public BigDecimal calcAmountPayable() {
		BigDecimal amountPayable = getAmount().
				subtract(getPointDiscount()).
				subtract(getExchangeDiscount()).
				subtract(getCouponDiscount());
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
		    if (getShippings().size()>0) {
			    Shipping shipping = getShippings().get(0);
				return shipping.getShippingDescr();
			} else {
				return "已发货";
			}
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

	public boolean refundOrReturn() {
		return getShippingStatus().equals(ShippingStatus.returning) ||  getShippingStatus().equals(ShippingStatus.returned) ||
			   getPaymentStatus().equals(PaymentStatus.refunding)   ||  getPaymentStatus().equals(PaymentStatus.refunded);
	}

	/**
	 *
	 * 获取本单收益
	 *
	 * @return 本单收益
	 */

	@Transient
	public BigDecimal getRebate() {
		BigDecimal rebate = BigDecimal.ZERO;
		if (getDeposits() != null) {
			for (Deposit deposit : getDeposits()) {
				if (deposit != null && deposit.getType().equals(Deposit.Type.rebate)) {
					rebate = rebate.add(deposit.getCredit());
				}
			}
		}
		return rebate;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}