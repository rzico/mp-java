package net.wit.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import freemarker.template.TemplateException;

/**
 * Entity - 优惠券
 */
@Entity
@Table(name = "wx_coupon")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_coupon_sequence")
public class Coupon extends BaseEntity {

	private static final long serialVersionUID = 21L;

	public static enum Type{
		/*满减 */
		fullcut,
		/*满折 */
		discount,
		/*红包 */
		redbag,
		/*兑换 */
		exchange,

	};

	public static enum Scope{
		/*全场 */
		all,
		/*店内 */
		shop,
		/*商城 */
		mall
	};


	public enum Color {
		/** 待审核  */
		c1,
		/** 待审核  */
		c2,
		/** 待审核  */
		c3,
		/** 待审核  */
		c4,
		/** 待审核  */
		c5,
		/** 待审核  */
		c6,
		/** 待审核  */
		c7,
		/** 待审核  */
		c8,
		/** 待审核  */
		c9,
		/** 待审核  */
		c10,
	}

	/** 类型 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '类型 {fullcut:满减,discount:满折,redbag:红包,exchange:兑换}'")
	private Type type;

	/** 使用范围 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '使用范围 {all:全场,shop:店内,mall:商城}'")
	private Scope scope;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 使用起始日期 */
	@Column(nullable = false,columnDefinition="datetime not null comment '使用起始日期'")
	private Date beginDate;

	/** 使用结束日期 */
	@Column(nullable = false,columnDefinition="datetime not null comment '使用结束日期'")
	private Date endDate;

	/** 发放者 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member distributor;

	/** 兑换商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Goods goods;

	/** 活动规则
	 * {type:0,min:0,amount:0}
	 * */
	@Lob
	@Column(columnDefinition="longtext comment '活动规则'")
	@JsonIgnore
	private String activity;

	/**
	 * 颜色
	 */
	@Column(columnDefinition="int(11) not null comment '颜色'")
	private Color color;

	/** 优惠金额/折扣比例 */
	@Min(0)
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '优惠金额'")
	private BigDecimal amount;

	/** 发券数量 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '发券数量'")
	private Long stock;

	/** 使用条件 0 代表无门槛 */
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null comment '使用条件'")
	private BigDecimal minimumPrice;

	/** 介绍 */
	@Lob
	@Column(nullable = false,columnDefinition="longtext comment '介绍'")
	private String introduction;

	/** 优惠码 */
	@OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<CouponCode> couponCodes = new HashSet<CouponCode>();

	/** 订单 */
	@ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<Order>();

	/** 是否删除 */
	@Column(nullable = false,columnDefinition="bit not null comment '是否删除'")
	private Boolean deleted;

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取使用起始日期
	 * 
	 * @return 使用起始日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 设置使用起始日期
	 * 
	 * @param beginDate
	 *            使用起始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 获取使用结束日期
	 * 
	 * @return 使用结束日期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置使用结束日期
	 * 
	 * @param endDate
	 *            使用结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取最小商品价格
	 * 
	 * @return 最小商品价格
	 */
	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	/**
	 * 设置最小商品价格
	 * 
	 * @param minimumPrice
	 *            最小商品价格
	 */
	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	/**
	 * 获取介绍
	 * 
	 * @return 介绍
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置介绍
	 * 
	 * @param introduction
	 *            介绍
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 获取优惠码
	 * 
	 * @return 优惠码
	 */
	public Set<CouponCode> getCouponCodes() {
		return couponCodes;
	}

	/**
	 * 设置优惠码
	 * 
	 * @param couponCodes
	 *            优惠码
	 */
	public void setCouponCodes(Set<CouponCode> couponCodes) {
		this.couponCodes = couponCodes;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 判断是否已开始
	 * 
	 * @return 是否已开始
	 */
	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Member getDistributor() {
		return distributor;
	}

	public void setDistributor(Member distributor) {
		this.distributor = distributor;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		List<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.getCoupons().remove(this);
			}
		}
	}

}