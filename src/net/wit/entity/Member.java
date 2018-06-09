package net.wit.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import net.wit.entity.MemberAttribute.Type;
import net.wit.util.JsonUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 会员
 * 
 * @author 降魔战队
 * @version 3.0
 */

@Entity
@Table(name = "wx_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "x_member_sequence")
public class Member extends BaseEntity {

	private static final long serialVersionUID = 29L;

	/**
	 * 性别
	 */
	public enum Gender {

		/** 男 */
		male,

		/** 女 */
		female,

		/** 保密 */
		secrecy
	}

	/**
	 * 星级
	 */
	public enum VIP {
		/** v1 */
		vip1,
		/** v2 */
		vip2,
		/** v3 */
		vip3
	}

	/** "身份信息"参数名称 */
	public static final String PRINCIPAL_ATTRIBUTE_NAME = "MEMBER.PRINCIPAL";

	/** "用户名"Cookie名称 */
	public static final String MOBILE_LOGIN_CAPTCHA = "MEMBER.LOGIN.CAPTCHA";

	/** "用户名"Cookie名称 */
	public static final String MEMBER_PASSWORD_CAPTCHA = "MEMBER.PASSWORD.CAPTCHA";

	/** "用户名"Cookie名称 */
	public static final String MOBILE_BIND_CAPTCHA = "MEMBER.BIND.CAPTCHA";

	/** 会员注册项值属性个数 */
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;

	/** 会员注册项值属性名称前缀 */
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	/** 用户名 */
	@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
	@Column(columnDefinition="varchar(50) comment '用户名'")
	private String username;

	/** 密码 */
	@Pattern(regexp = "^[^\\s&\"<>]+$")
	@Column(columnDefinition="varchar(255) comment '密码'")
	private String password;

	/** E-mail */
	@Email
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '邮箱'")
	private String email;

	/** 积分 */
	@Min(0)
	@Column(columnDefinition="bigint(20) default 0 comment '积分'")
	private Long point;

	/** 消费金额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '消费金额'")
	private BigDecimal amount;

	/** 余额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '余额'")
	private BigDecimal balance;

	/** 冻结 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '冻结'")
	private BigDecimal freezeBalance;

	/** 是否启用 */
	@NotNull
	@Column(columnDefinition="bit not null comment '是否启用'")
	private Boolean isEnabled;

	/** 是否锁定 */
	@Column(columnDefinition="bit not null comment '是否锁定'")
	private Boolean isLocked;

	/** 连续登录失败次数 */
	@Column(columnDefinition="int(11) comment '连续登录失败次数'")
	private Integer loginFailureCount;

	/** 锁定日期 */
	@Column(columnDefinition="datetime comment '锁定日期'")
	private Date lockedDate;

	/** 注册IP */
	@Column(columnDefinition="varchar(255) comment '注册IP'")
	private String registerIp;

	/** 最后登录IP */
	@Column(columnDefinition="varchar(255) comment '最后登录IP'")
	@JsonIgnore
	private String loginIp;

	/** 最后登录日期 */
	@Column(columnDefinition="datetime comment '最后登录日期'")
	@JsonIgnore
	private Date loginDate;

	/** 昵称 */
	@Column(columnDefinition="varchar(255) comment '昵称'")
	private String nickName;

	/** 签名 */
	@Column(columnDefinition="varchar(255) comment '签名'")
	private String autograph;

	/** 头像 */
	@Column(columnDefinition="varchar(255) comment '头像'")
	private String logo;

	/** 姓名 */
	@Column(columnDefinition="varchar(255) comment '姓名'")
	private String name;

	/** 性别 */
	@Column(columnDefinition="int(11) comment '性别 {male:男,female:女,secrecy:保密}'")
	private Gender gender;

	/** 职业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Occupation occupation;

	/** 出生日期 */
	@Column(columnDefinition="datetime comment '出生日期'")
	private Date birth;

//	/** 地址 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '地址'")
//	private String address;
//
//	/** 邮编 */
//	@Column(columnDefinition="varchar(255) comment '邮编'")
//	private String zipCode;
//
//	/** 电话 */
//	@Column(columnDefinition="varchar(255) comment '电话'")
//	private String phone;

	/** 手机 */
	@Column(columnDefinition="varchar(50) comment '手机'")
	@JsonIgnore
	private String mobile;

	/** 设备号 */
	@Column(columnDefinition="varchar(50) comment '设备号'")
	@JsonIgnore
	private String uuid;

	/** 设备环境 IOS Andriod */
	@Lob
	@Column(columnDefinition="longtext comment '设备环境'")
	@JsonIgnore
	private String scene;

	/** 安全码 */
	@Column(columnDefinition="varchar(50) comment '安全码'")
	@JsonIgnore
	private String sign;

	/** 星级 */
	@NotNull
	@Column(columnDefinition="int(11) comment '星级 { vip1:vip1,vip2:vip2,vip3:vip3}'")
	private VIP vip;

//	/** 会员注册项值0 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值0'")
//	@JsonIgnore
//	private String attributeValue0;
//
//	/** 会员注册项值1 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值1'")
//	@JsonIgnore
//	private String attributeValue1;
//
//	/** 会员注册项值2 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值2'")
//	private String attributeValue2;
//
//	/** 会员注册项值3 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值3'")
//	@JsonIgnore
//	private String attributeValue3;
//
//	/** 会员注册项值4 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值4'")
//	private String attributeValue4;
//
//	/** 会员注册项值5 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值5'")
//	@JsonIgnore
//	private String attributeValue5;
//
//	/** 会员注册项值6 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值6'")
//	@JsonIgnore
//	private String attributeValue6;
//
//	/** 会员注册项值7 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值7'")
//	@JsonIgnore
//	private String attributeValue7;
//
//	/** 会员注册项值8 */
//	@Length(max = 200)
//	@Column(columnDefinition="varchar(255) comment '会员注册项值8'")
//	@JsonIgnore
//	private String attributeValue8;

	/** IM状态 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment 'IM状态'")
	private String attributeValue9;

	/** 推广二维码 */
	@Column(columnDefinition="varchar(255) comment '推广二维码'")
	private String qrcode;

	/** 定位 */
	@Embedded
	private Location location;

	/** 地区 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '地区 {}'")
	@JsonIgnore
	private Area area;

	/**
	 * 获取购物车
	 *
	 * @return 购物车
	 */
	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Cart cart;

	/** 专栏*/
	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '专栏'")
	@JsonIgnore
	private Topic topic;

	/** 分享者 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member promoter;

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

	/** 会员标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_member_tag")
	@OrderBy("orders asc")
	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();

	/** 会员卡 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_member_card")
	@OrderBy("modifyDate desc")
	@JsonIgnore
	private List<Card> cards = new ArrayList<Card>();

	/** 预存款 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Deposit> deposits = new HashSet<Deposit>();

	/** 优惠券 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	@Where(clause="is_used=0")
	@OrderBy("createDate desc")
	private List<CouponCode> couponCodes = new ArrayList<>();

	/** 订单 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	@Where(clause="order_status=2")
	@OrderBy("createDate desc")
	private Set<Order> orders = new HashSet<Order>();

	/** 收款单 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Payment> payments = new HashSet<Payment>();

	/** 收货地址 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("isDefault desc, createDate desc")
	@JsonIgnore
	private Set<Receiver> receivers = new HashSet<Receiver>();

	/** 收藏文章*/
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<ArticleFavorite> favorites = new HashSet<ArticleFavorite>();

	/** 我的好友*/
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Friends> friends = new HashSet<Friends>();

	/** 我的关注*/
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<MemberFollow> follows = new HashSet<MemberFollow>();

	/** 我的粉丝*/
	@OneToMany(mappedBy = "follow", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<MemberFollow> fans = new HashSet<MemberFollow>();

	public VIP getVip() {
		return vip;
	}

	public void setVip(VIP vip) {
		this.vip = vip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public BigDecimal getFreezeBalance() {
		return freezeBalance;
	}

	public void setFreezeBalance(BigDecimal freezeBalance) {
		this.freezeBalance = freezeBalance;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean locked) {
		isLocked = locked;
	}

	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getZipCode() {
//		return zipCode;
//	}
//
//	public void setZipCode(String zipCode) {
//		this.zipCode = zipCode;
//	}
//
//	public String getPhone() {
//		return phone;
//	}
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

//	public String getAttributeValue0() {
//		return attributeValue0;
//	}
//
//	public void setAttributeValue0(String attributeValue0) {
//		this.attributeValue0 = attributeValue0;
//	}
//
//	public String getAttributeValue1() {
//		return attributeValue1;
//	}
//
//	public void setAttributeValue1(String attributeValue1) {
//		this.attributeValue1 = attributeValue1;
//	}
//
//	public String getAttributeValue2() {
//		return attributeValue2;
//	}
//
//	public void setAttributeValue2(String attributeValue2) {
//		this.attributeValue2 = attributeValue2;
//	}
//
//	public String getAttributeValue3() {
//		return attributeValue3;
//	}
//
//	public void setAttributeValue3(String attributeValue3) {
//		this.attributeValue3 = attributeValue3;
//	}
//
//	public String getAttributeValue4() {
//		return attributeValue4;
//	}
//
//	public void setAttributeValue4(String attributeValue4) {
//		this.attributeValue4 = attributeValue4;
//	}
//
//	public String getAttributeValue5() {
//		return attributeValue5;
//	}
//
//	public void setAttributeValue5(String attributeValue5) {
//		this.attributeValue5 = attributeValue5;
//	}
//
//	public String getAttributeValue6() {
//		return attributeValue6;
//	}
//
//	public void setAttributeValue6(String attributeValue6) {
//		this.attributeValue6 = attributeValue6;
//	}
//
//	public String getAttributeValue7() {
//		return attributeValue7;
//	}
//
//	public void setAttributeValue7(String attributeValue7) {
//		this.attributeValue7 = attributeValue7;
//	}
//
//	public String getAttributeValue8() {
//		return attributeValue8;
//	}
//
//	public void setAttributeValue8(String attributeValue8) {
//		this.attributeValue8 = attributeValue8;
//	}


	public Member getPromoter() {
		return promoter;
	}

	public void setPromoter(Member promoter) {
		this.promoter = promoter;
	}

	public String getAttributeValue9() {
		return attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Set<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Set<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getLogo() {
		if (logo==null) {
			logo = "http://cdn.rzico.com/weex/resources/images/headimg.png";
		}
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<ArticleFavorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(Set<ArticleFavorite> favorites) {
		this.favorites = favorites;
	}

	public Set<MemberFollow> getFollows() {
		return follows;
	}

	public void setFollows(Set<MemberFollow> follows) {
		this.follows = follows;
	}

	public Set<MemberFollow> getFans() {
		return fans;
	}

	public void setFans(Set<MemberFollow> fans) {
		this.fans = fans;
	}

	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public Set<Friends> getFriends() {
		return friends;
	}

	public void setFriends(Set<Friends> friends) {
		this.friends = friends;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public List<CouponCode> getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(List<CouponCode> couponCodes) {
		this.couponCodes = couponCodes;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	/**
	 * 获取会员注册项值
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 * @return 会员注册项值
	 */
	@Transient
	public Object getAttributeValue(MemberAttribute memberAttribute) {
		if (memberAttribute != null) {
			if (memberAttribute.getType() == Type.name) {
				return getName();
			} else if (memberAttribute.getType() == Type.gender) {
				return getGender();
			} else if (memberAttribute.getType() == Type.birth) {
				return getBirth();
			} else if (memberAttribute.getType() == Type.area) {
				return getArea();
//			} else if (memberAttribute.getType() == Type.address) {
//				return getAddress();
//			} else if (memberAttribute.getType() == Type.zipCode) {
//				return getZipCode();
//			} else if (memberAttribute.getType() == Type.phone) {
//				return getPhone();
			} else if (memberAttribute.getType() == Type.mobile) {
				return getMobile();
			} else if (memberAttribute.getType() == Type.checkbox) {
				if (memberAttribute.getPropertyIndex() != null) {
					try {
						String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
						String propertyValue = (String) PropertyUtils.getProperty(this, propertyName);
						if (propertyValue != null) {
							return JsonUtils.toObject(propertyValue, List.class);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (memberAttribute.getPropertyIndex() != null) {
					try {
						String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
						return (String) PropertyUtils.getProperty(this, propertyName);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 设置会员注册项值
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 * @param attributeValue
	 *            会员注册项值
	 */
	@Transient
	public void setAttributeValue(MemberAttribute memberAttribute, Object attributeValue) {
		if (memberAttribute != null) {
			if (attributeValue instanceof String && StringUtils.isEmpty((String) attributeValue)) {
				attributeValue = null;
			}
			if (memberAttribute.getType() == Type.name && (attributeValue instanceof String || attributeValue == null)) {
				setName((String) attributeValue);
			} else if (memberAttribute.getType() == Type.gender && (attributeValue instanceof Gender || attributeValue == null)) {
				setGender((Gender) attributeValue);
			} else if (memberAttribute.getType() == Type.birth && (attributeValue instanceof Date || attributeValue == null)) {
				setBirth((Date) attributeValue);
			} else if (memberAttribute.getType() == Type.area && (attributeValue instanceof Area || attributeValue == null)) {
				setArea((Area) attributeValue);
//			} else if (memberAttribute.getType() == Type.address && (attributeValue instanceof String || attributeValue == null)) {
//				setAddress((String) attributeValue);
//			} else if (memberAttribute.getType() == Type.zipCode && (attributeValue instanceof String || attributeValue == null)) {
//				setZipCode((String) attributeValue);
//			} else if (memberAttribute.getType() == Type.phone && (attributeValue instanceof String || attributeValue == null)) {
//				setPhone((String) attributeValue);
			} else if (memberAttribute.getType() == Type.mobile && (attributeValue instanceof String || attributeValue == null)) {
				setMobile((String) attributeValue);
			} else if (memberAttribute.getType() == Type.checkbox && (attributeValue instanceof List || attributeValue == null)) {
				if (memberAttribute.getPropertyIndex() != null) {
					if (attributeValue == null || (memberAttribute.getOptions() != null && memberAttribute.getOptions().containsAll((List<?>) attributeValue))) {
						try {
							String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
							PropertyUtils.setProperty(this, propertyName, JsonUtils.toJson(attributeValue));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				if (memberAttribute.getPropertyIndex() != null) {
					try {
						String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
						PropertyUtils.setProperty(this, propertyName, attributeValue);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 移除所有会员注册项值
	 */
	@Transient
	public void removeAttributeValue() {
		setName(null);
		setGender(null);
		setBirth(null);
		setArea(null);
//		setAddress(null);
//		setZipCode(null);
//		setPhone(null);
		setMobile(null);
		for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
			try {
				PropertyUtils.setProperty(this, propertyName, null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	public BigDecimal effectiveBalance() {
		return getBalance();
	}

	public String userId() {
		String uid = getUsername();
		if (uid!=null && "gm_".equals(uid.substring(0,3))) {
			return uid;
		}
		if (getId()!=null) {
			Long userId = getId() + 10200L;
			return "u" + userId.toString();
		} else {
			return "";
		}

	}

	public Card card(Member seller) {
		Card card = null;
		for (Card c:getCards()) {
			if (c.getOwner().equals(seller)) {
				card = c;
				break;
			}
		}
		return card;
	}

	//获取股东
	public Member partner(Member seller) {
		Card card = card(seller);
		if (card==null) {
			return null;
		}

		if (card.getType().equals(Card.Type.partner)) {
			return this;
		} else {
			Member p = card.getPromoter();
			if (p!=null) {
                return p.partner(seller);
			} else {
				return null;
			}
		}
	}

	//是否有分润
	public Boolean leaguer(Member seller) {
		Card card = card(seller);
		Topic topic = seller.getTopic();
		TopicConfig config = topic.getConfig();
		if (config.getPromoterType().equals(TopicConfig.PromoterType.any)) {
			return true;
		}
		if (card==null) {
			return false;
		}
		if ((config.getPromoterType().equals(TopicConfig.PromoterType.team) && card.getType().compareTo(Card.Type.team)>=0)){
			return true;
		} else
		if ((config.getPromoterType().equals(TopicConfig.PromoterType.partner) && card.getType().equals(Card.Type.partner))) {
			return true;
		} else {
			return false;
		}
	}

	public String displayName() {
		if (getNickName()!=null) {
			return getNickName();
		} else if (getName()!=null) {
			return getName();
		} else
		{
			return userId();
		}
	}

	public String realName() {
		if (getName()!=null) {
			return getName();
		} else
		if (getNickName()!=null) {
			return getNickName();
		} else
		{
			return userId();
		}
	}

	public String topicName() {
		if (getTopic()!=null) {
			return getTopic().getName();
		} else {
			return this.displayName();
		}
	}

	public static Long decodeUserId(String userId) {
		if (userId!=null) {
			String uid = userId.substring(2);
			return Long.parseLong(uid)-10200;
		} else {
			return null;
		}

	}

	public MapEntity getMapArea() {
		if (getArea() != null) {
			return new MapEntity(getArea().getId().toString(), getArea().getName());
		} else {
			return null;
		}
	}


	public MapEntity getMapOccupation() {
		if (getOccupation() != null) {
			return new MapEntity(getOccupation().getId().toString(), getOccupation().getName());
		} else {
			return null;
		}
	}

	public MapEntity getMapTags() {
		String tagStr = "";
		if (getTags() != null) {
			for (Tag tag:getTags()) {
				if ("".equals(tagStr)) {
					tagStr = tag.getName();
				} else {
					tagStr = tagStr.concat(","+tag.getName());
				}
			}
			return new MapEntity("",tagStr);
		} else {
			return null;
		}
	}

}