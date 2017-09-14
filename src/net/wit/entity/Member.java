package net.wit.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import net.wit.MapEntity;
import net.wit.entity.MemberAttribute.Type;
import net.wit.util.JsonUtils;

import net.wit.weixin.message.resp.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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

	private static final long serialVersionUID = 113L;

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

	/** "身份信息"参数名称 */
	public static final String PRINCIPAL_ATTRIBUTE_NAME = "MEMBER.PRINCIPAL";

	/** "用户名"Cookie名称 */
	public static final String USERNAME_COOKIE_NAME = "username";

	/** 会员注册项值属性个数 */
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;

	/** 会员注册项值属性名称前缀 */
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	/** 最大收藏文章数 */
	public static final Integer MAX_FAVORITE_COUNT = 50;

	/** 用户名 */
	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
	@Column(columnDefinition="varchar(50) not null unique comment '用户名'")
	private String username;

	/** 密码 */
	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[^\\s&\"<>]+$")
	@Column(columnDefinition="varchar(255) comment '密码'")
	private String password;

	/** E-mail */
	@NotEmpty
	@Email
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '邮箱'")
	private String email;

	/** 积分 */
	@NotNull(groups = Save.class)
	@Min(0)
	@Column(columnDefinition="bigint(20) default 0 comment '积分'")
	private Long point;

	/** 余额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '余额'")
	private BigDecimal balance;

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
	private String loginIp;

	/** 最后登录日期 */
	@Column(columnDefinition="datetime comment '最后登录日期'")
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
	@Column(columnDefinition="int(11) comment '职务'")
	@ManyToOne(fetch = FetchType.LAZY)
	private Occupation occupation;

	/** 出生日期 */
	@Column(columnDefinition="datetime comment '出生日期'")
	private Date birth;

	/** 地址 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '地址'")
	private String address;

	/** 邮编 */
	@Column(columnDefinition="varchar(255) comment '邮编'")
	private String zipCode;

	/** 电话 */
	@Column(columnDefinition="varchar(255) comment '电话'")
	private String phone;

	/** 手机 */
	@Column(columnDefinition="varchar(255) comment '手机'")
	private String mobile;

	/** 会员注册项值0 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值0'")
	private String attributeValue0;

	/** 会员注册项值1 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值1'")
	private String attributeValue1;

	/** 会员注册项值2 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值2'")
	private String attributeValue2;

	/** 会员注册项值3 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值3'")
	private String attributeValue3;

	/** 会员注册项值4 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值4'")
	private String attributeValue4;

	/** 会员注册项值5 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值5'")
	private String attributeValue5;

	/** 会员注册项值6 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值6'")
	private String attributeValue6;

	/** 会员注册项值7 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值7'")
	private String attributeValue7;

	/** 会员注册项值8 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值8'")
	private String attributeValue8;

	/** 会员注册项值9 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '会员注册项值9'")
	private String attributeValue9;

	/** 安全密匙 */
	@Embedded
	private SafeKey safeKey;

	/** 定位 */
	@Embedded
	private Location location;

	/** 地区 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '地区 {}'")
	private Area area;

	/** 会员标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_member_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();

	/** 预存款 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Deposit> deposits = new HashSet<Deposit>();

	/** 收款单 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Payment> payments = new HashSet<Payment>();

	/** 收货地址 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("isDefault desc, createDate desc")
	private Set<Receiver> receivers = new HashSet<Receiver>();

	/** 收藏文章*/
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ArticleFavorite> favorites = new HashSet<ArticleFavorite>();

	/** 我的好友*/
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Friends> friends = new HashSet<Friends>();

	/** 我的关注*/
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<MemberFollow> follows = new HashSet<MemberFollow>();

	/** 我的粉丝*/
	@OneToMany(mappedBy = "follow", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<MemberFollow> fans = new HashSet<MemberFollow>();

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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public Boolean getLocked() {
		return isLocked;
	}

	public void setLocked(Boolean locked) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAttributeValue0() {
		return attributeValue0;
	}

	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	public String getAttributeValue1() {
		return attributeValue1;
	}

	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	public String getAttributeValue2() {
		return attributeValue2;
	}

	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	public String getAttributeValue3() {
		return attributeValue3;
	}

	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	public String getAttributeValue4() {
		return attributeValue4;
	}

	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	public String getAttributeValue5() {
		return attributeValue5;
	}

	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	public String getAttributeValue6() {
		return attributeValue6;
	}

	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	public String getAttributeValue7() {
		return attributeValue7;
	}

	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	public String getAttributeValue8() {
		return attributeValue8;
	}

	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	public String getAttributeValue9() {
		return attributeValue9;
	}

	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	public SafeKey getSafeKey() {
		return safeKey;
	}

	public void setSafeKey(SafeKey safeKey) {
		this.safeKey = safeKey;
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

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getLogo() {
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
			} else if (memberAttribute.getType() == Type.address) {
				return getAddress();
			} else if (memberAttribute.getType() == Type.zipCode) {
				return getZipCode();
			} else if (memberAttribute.getType() == Type.phone) {
				return getPhone();
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
			} else if (memberAttribute.getType() == Type.address && (attributeValue instanceof String || attributeValue == null)) {
				setAddress((String) attributeValue);
			} else if (memberAttribute.getType() == Type.zipCode && (attributeValue instanceof String || attributeValue == null)) {
				setZipCode((String) attributeValue);
			} else if (memberAttribute.getType() == Type.phone && (attributeValue instanceof String || attributeValue == null)) {
				setPhone((String) attributeValue);
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
		setAddress(null);
		setZipCode(null);
		setPhone(null);
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