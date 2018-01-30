package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;

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

	private static final long serialVersionUID = 16L;

	public static enum Status{
		/** 空卡  */
		none,
		/** 已激活 */
		activate,
		/** 已挂失 */
		loss
	};

	public static enum  VIP{
		/** vip1 普通会员 */
		vip1,
		/** vip2 团队成员 */
		vip2,
		/** vip3 */
		vip3
	};

	/** 状态 */
	@NotNull
	@Column(columnDefinition="int(11) comment '状态 {none:空卡,activate:已激活,loss:已挂失}'")
	private Status status;

	/** 等级 */
	@NotNull
	@Column(columnDefinition="int(11) comment '等级 {vip1:VIP1,vip2:VIP2,vip3:VIP3}'")
	private VIP vip;

	/** 最近使用日期 */
	@Column(columnDefinition="datetime comment '最近使用日期'")
	private Date usedDate;


	/** 所属商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JsonIgnore
	private TopicCard topicCard;

	/** 所属商家 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Member owner;

	/** 办卡门店 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Shop shop;

	/** 卡号 */
	@Column(length = 100,columnDefinition="varchar(100) not null unique comment '卡号'")
	private String code;

	/** 姓名 */
	@Column(columnDefinition="varchar(255) comment '姓名'")
	private String name;

	/** 手机 一个手机号只能办一个 */
	@Column(columnDefinition="varchar(50) comment '手机'")
	private String mobile;

	/** 安全码 */
	@JsonIgnore
	@Column(columnDefinition="varchar(50) comment '安全码'")
	private String sign;

	/** 余额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '余额'")
	private BigDecimal balance;

	/** 积分 */
	@Min(0)
	@Column(columnDefinition="bigint(20) default 0 comment '积分'")
	private Long point;

	/*  会员 */
	@JsonIgnore
	@ManyToMany(mappedBy = "cards",fetch = FetchType.LAZY)
	private List<Member> members = new ArrayList<Member>();

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public VIP getVip() {
		return vip;
	}

	public void setVip(VIP vip) {
		this.vip = vip;
	}

	/** 推广 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Member promoter;

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

	public TopicCard getTopicCard() {
		return topicCard;
	}

	public void setTopicCard(TopicCard topicCard) {
		this.topicCard = topicCard;
	}

	public Member getPromoter() {
		return promoter;
	}

	public void setPromoter(Member promoter) {
		this.promoter = promoter;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public MapEntity getMapOwner(){
		if(getOwner() != null){
			return new MapEntity(getOwner().getId().toString(),getOwner().getName());
		}else{
			return null;
		}
	}

	public MapEntity getMapShop(){
		if(getShop() != null){
			return new MapEntity(getShop().getId().toString(),getShop().getName());
		}else{
			return null;
		}
	}
}