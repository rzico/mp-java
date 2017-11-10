
package net.wit.entity;

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * Entity - 银行卡
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_bankcard")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_bankcard_sequence")
public class Bankcard extends BaseEntity {

	private static final long serialVersionUID = 14L;

	/** 银行名称 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '银行名称'")
	private String bankname;

	/** 银行编号 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '银行编号'")
	private String banknum;

	/** logo */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment 'logo'")
	private String bankimage;

	/** 卡种 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '卡种'")
	private String cardname;

	/** 卡类型 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '卡类型'")
	private String cardtype;

	/** 卡号 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '卡号'")
	private String cardno;

	/** 开户名 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '开户名'")
	private String name;

	/** 证件号 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '证件号'")
	private String identity;

	/** 手机号 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '手机号'")
	private String mobile;

	/** 城市 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '城市'")
	private String city;

	/** 省 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '省'")
	private String province;

	/** 是否默认 */
	@Length(max = 50)
	@Column(columnDefinition="bit not null comment '默认'")
	private Boolean isDefault;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBanknum() {
		return banknum;
	}

	public void setBanknum(String banknum) {
		this.banknum = banknum;
	}

	public String getBankimage() {
		return bankimage;
	}

	public void setBankimage(String bankimage) {
		this.bankimage = bankimage;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean aDefault) {
		isDefault = aDefault;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}

}