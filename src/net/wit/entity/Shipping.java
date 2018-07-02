
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
 * Entity - 发货单
 * 

 */
@Entity
@Table(name = "wx_shipping")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_shipping_sequence")
public class Shipping extends BaseEntity {

	private static final long serialVersionUID = 51L;

	/**
	 * 单据状态
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
	 * 配送状态
	 */
	public enum ShippingStatus {
		/** 未确认 */
		unconfirmed,

		/** 已派单 */
		dispatch,

		/** 已发货 */
		delivery,

		/** 已送达 */
		receive,

		/** 已核销 */
		completed
	}

	/** 卖家 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member seller;

	/** 买家 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 配送单位 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Enterprise enterprise;

	/** 配送门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Shop shop;

	/** 送货员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Admin admin;

	/** 预约时间 */
	@Column(columnDefinition="datetime comment '预约时间'")
	private Date hopeDate;

	/** 编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100,columnDefinition="varchar(100) not null unique comment '编号'")
	private String sn;

	/** 订单状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '订单状态'")
	private OrderStatus orderStatus;

	/** 配送状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '配送状态'")
	private ShippingStatus shippingStatus;

	/** 配送方式 */
	@Column(columnDefinition="varchar(255) comment '配送方式'")
	private String shippingMethod;

	/** 物流公司 */
	@Column(columnDefinition="varchar(255) comment '物流公司'")
	private String deliveryCorp;

	/** 运单号 */
	@Column(columnDefinition="varchar(255) comment '运单号'")
	private String trackingNo;

	/** 是否转派单 */
	@NotNull
	@Column(columnDefinition="bit comment '是否转派单'")
	@JsonIgnore
	private Boolean transfer;

	/** 楼层 */
	@NotNull
	@Min(1)
	@Column(nullable = false,columnDefinition="int(11) not null default 0 comment '楼层'")
	private Integer level;

	/** 成本 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '成本'")
	private BigDecimal cost;

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

	/** 收货人 */
	@Column(columnDefinition="varchar(255) comment '收货人'")
	private String consignee;

	/** 地区 */
	@Column(columnDefinition="varchar(255) comment '地区'")
	private String areaName;

	/** 地址 */
	@Column(columnDefinition="varchar(255) comment '地址'")
	private String address;

	/** 邮编 */
	@Column(columnDefinition="varchar(255) comment '邮编'")
	private String zipCode;

	/** 电话 */
	@Column(columnDefinition="varchar(255) comment '电话'")
	private String phone;

	/** 备注 */
	@Column(columnDefinition="varchar(255) comment '备注'")
	private String memo;

	/** 锁定到期时间 */
	@Column(columnDefinition="datetime comment '锁定到期时间'")
	private Date lockExpire;

	/** 操作人 */
	@Column(columnDefinition="varchar(255) comment '操作人'")
	private String operator;

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	/** 订单 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	/** 发货项 */
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "shipping", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ShippingItem> shippingItems = new ArrayList<ShippingItem>();

	/** 包装项 */
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "shipping", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ShippingBarrel> shippingBarrels = new ArrayList<ShippingBarrel>();

	public BigDecimal getAdminFreight() {
		return adminFreight;
	}

	public void setAdminFreight(BigDecimal adminFreight) {
		this.adminFreight = adminFreight;
	}

	public Date getHopeDate() {
		return hopeDate;
	}

	public void setHopeDate(Date hopeDate) {
		this.hopeDate = hopeDate;
	}

	public Boolean getTransfer() {
		return transfer;
	}

	public void setTransfer(Boolean transfer) {
		this.transfer = transfer;
	}

	/**
	 * 获取编号
	 * 
	 * @return 编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * 
	 * @param sn
	 *            编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}


	/**
	 * 获取配送方式
	 * 
	 * @return 配送方式
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethod
	 *            配送方式
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * 获取物流公司
	 * 
	 * @return 物流公司
	 */
	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	/**
	 * 设置物流公司
	 * 
	 * @param deliveryCorp
	 *            物流公司
	 */
	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	/**
	 * 获取运单号
	 * 
	 * @return 运单号
	 */
	public String getTrackingNo() {
		return trackingNo;
	}

	/**
	 * 设置运单号
	 * 
	 * @param trackingNo
	 *            运单号
	 */
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	/**
	 * 获取物流费用
	 * 
	 * @return 物流费用
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置物流费用
	 * 
	 * @param freight
	 *            物流费用
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
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

	public Member getSeller() {
		return seller;
	}

	public void setSeller(Member seller) {
		this.seller = seller;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
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
	 * 获取操作员
	 * 
	 * @return 操作员
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
	 * 获取发货项
	 * 
	 * @return 发货项
	 */
	public List<ShippingItem> getShippingItems() {
		return shippingItems;
	}

	/**
	 * 设置发货项
	 * 
	 * @param shippingItems
	 *            发货项
	 */
	public void setShippingItems(List<ShippingItem> shippingItems) {
		this.shippingItems = shippingItems;
	}


	public Date getLockExpire() {
		return lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public BigDecimal getLevelFreight() {
		return levelFreight;
	}

	public void setLevelFreight(BigDecimal levelFreight) {
		this.levelFreight = levelFreight;
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

	public BigDecimal getShippingFreight() {
		return shippingFreight;
	}

	public void setShippingFreight(BigDecimal shippingFreight) {
		this.shippingFreight = shippingFreight;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	@Transient
	public int getQuantity() {
		int quantity = 0;
		if (getShippingItems() != null) {
			for (ShippingItem shippingItem : getShippingItems()) {
				if (shippingItem != null && shippingItem.getQuantity() != null) {
					quantity += shippingItem.getQuantity();
				}
			}
		}
		return quantity;
	}
	@Transient
	public String getShippingDescr() {
		if (getShippingStatus().equals(ShippingStatus.unconfirmed)) {
			return "已接单";
		} else
		if (getShippingStatus().equals(ShippingStatus.dispatch)) {
			return "已派单";
		} else
		if (getShippingStatus().equals(ShippingStatus.delivery)) {
			return "送货中";
		} else
		if (getShippingStatus().equals(ShippingStatus.receive)) {
			return "已送达";
		} else
		if (getShippingStatus().equals(ShippingStatus.completed)) {
			return "已核销";
		} else {
			return "已接单";
		}
	}

	@Transient
	public String getStatusDescr() {
		if (getShippingStatus().equals(ShippingStatus.unconfirmed)) {
			return "订单正在备货";
		} else
		if (getShippingStatus().equals(ShippingStatus.dispatch)) {
			return "订单正在提货";
		} else
		if (getShippingStatus().equals(ShippingStatus.delivery)) {
			return "订单正在送货";
		} else
		if (getShippingStatus().equals(ShippingStatus.receive)) {
			return "订单已送达";
		} else
		if (getShippingStatus().equals(ShippingStatus.completed)) {
			return "订单已完成";
		} else {
			return "等待商家确认";
		}
	}

	public List<ShippingBarrel> getShippingBarrels() {
		return shippingBarrels;
	}

	public void setShippingBarrels(List<ShippingBarrel> shippingBarrels) {
		this.shippingBarrels = shippingBarrels;
	}

	@Transient
	public BigDecimal calcCost() {
		BigDecimal amount = BigDecimal.ZERO;
		if (getShippingItems() != null) {
			for (ShippingItem shippingItem : getShippingItems()) {
				if (shippingItem != null) {
					amount = amount.add(shippingItem.getCost());
				}
			}
		}
		return amount;
	}

	@Transient
	public List<Barrel> barrels() {
		List<Barrel> bs = new ArrayList<>();
		if (getShippingItems() != null) {
			for (ShippingItem shippingItem : getShippingItems()) {
				if (shippingItem != null && shippingItem.getProduct() != null) {
					Barrel b = shippingItem.getProduct().getBarrel();
					if (b!=null && !bs.contains(b)) {
						bs.add(b);
					}
				}
			}
		}
		return bs;
	}

	@Transient
	public BigDecimal calcFreight(Receiver receiver) {
		BigDecimal price = BigDecimal.ZERO;
		int quantity = getQuantity();

		if (quantity<6) {
			price = new BigDecimal(5);
		} else
		if (quantity<20) {
			price = new BigDecimal(4.5);
		} else
		if (quantity<50) {
			price = new BigDecimal(3.5);
		} else {
			price = new BigDecimal(2.5);
		}

		if (receiver.getLevel()>2) {
			price = price.add(
					new BigDecimal(receiver.getLevel() - 2)
			);
		}

		BigDecimal amount = price.multiply(new BigDecimal(quantity));

		if (quantity==1) {
			 amount = amount.add(new BigDecimal(2));
		}
		return amount;
	}

	@Transient
	public BigDecimal calcAdminFreight(Receiver receiver) {
		BigDecimal price = BigDecimal.ZERO;
		int quantity = getQuantity();

		if (quantity<6) {
			price = new BigDecimal(3);
		} else
		if (quantity<20) {
			price = new BigDecimal(2.5);
		} else
		if (quantity<50) {
			price = new BigDecimal(2);
		} else {
			price = new BigDecimal(1.5);
		}

		if (receiver.getLevel()>2) {
			price = price.add(
					new BigDecimal(receiver.getLevel() - 2)
			);
		}

		BigDecimal amount = price.multiply(new BigDecimal(quantity));

		if (quantity==1) {
			amount = amount.add(new BigDecimal(2));
		}
		return amount;

	}


	@Transient
	public BigDecimal calcLevelFreight(Receiver receiver) {
		BigDecimal price = BigDecimal.ZERO;
		int quantity = getQuantity();

		if (receiver.getLevel()>2) {
			price = price.add(
					new BigDecimal(receiver.getLevel() - 2)
			);
		}

		BigDecimal amount = price.multiply(new BigDecimal(quantity));

		if (quantity==1) {
			amount = amount.add(new BigDecimal(2));
		}
		return amount;

	}

}