package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 会员卡
 * 
 */
@Entity
@Table(name = "wx_card")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_card_sequence")
public class Card extends BaseEntity {

	private static final long serialVersionUID = 906L;

	/** 最近使用日期 */
	@Column(columnDefinition="datetime comment '最近使用日期'")
	private Date usedDate;

	/** 所属商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Member owner;

	/** 办卡门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Shop shop;

	/** 实体卡号 */
	@Column(length = 100,columnDefinition="varchar(100) comment '实体卡号'")
	private String code;

	/** 姓名 */
	@Column(columnDefinition="varchar(255) comment '姓名'")
	@NotNull
	private String name;

	/** 手机 一个手机号只能办一个 */
	@Column(columnDefinition="varchar(50) comment '手机'")
	@JsonIgnore
	@NotNull
	private String mobile;

	/** 安全码 */
	@Column(columnDefinition="varchar(50) comment '安全码'")
	@JsonIgnore
	private String sign;

	/** 余额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '余额'")
	private BigDecimal balance;

	/*  会员 */
	@ManyToMany(mappedBy = "cards",fetch = FetchType.LAZY)
	private List<Member> members = new ArrayList<Member>();

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


	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Member getOwner() {
		return owner;
	}

	public void setOwner(Member owner) {
		this.owner = owner;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
}