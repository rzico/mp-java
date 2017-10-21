
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 转账单
 *
 * @author 降魔战队
 * @version 3.0
 */
@Entity
@Table(name = "wx_transfer")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_transfer_sequence")
public class Transfer extends BaseEntity {

	private static final long serialVersionUID = 219L;

	/**
	 * 类型
	 */

	public enum Type {

		/** 微信钱包 */
		weixin,
		/** 支付宝 */
		alipay,
		/** 银行卡 */
		bankcard
	}

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待审核 */
		waiting,

		/** 审核提交 */
		confirmed,

		/** 提现成功 */
		success,

		/** 提现失败 */
		failure
	}

	/** 编号 */
	@Column(columnDefinition="varchar(50) not null unique comment '编号'")
	private String sn;

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {weixin:微信钱包,alipay:支付宝,bankcard:银行卡}'")
	private Type type;

	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {waiting:等待提现,success:提现成功,failure:提现失败}'")
	private Status status;

	/** 提现金额 */
	@NotNull
	@Min(0)
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '提现金额'")
	private BigDecimal amount;

	/** 手续费 */
	@NotNull
	@Min(0)
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '手续费'")
	private BigDecimal fee;

	/** 到账日期 */
	@Column(columnDefinition="datetime comment '到账日期'")
	private Date transferDate;

	/** 操作员 */
	@Column(columnDefinition="varchar(255) comment '操作员'")
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '备注'")
	private String memo;

	/** 账单记录 */
	@OneToOne(mappedBy = "refunds", fetch = FetchType.LAZY)
	@JsonIgnore
	private Deposit deposit;

	/** 银行名称 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '银行名称'")
	@JsonIgnore
	private String bankname;

	/** 卡号 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '卡号'")
	@JsonIgnore
	private String cardno;

	/** 开户名 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '开户名'")
	private String name;

	/** 城市 */
	@Length(max = 50)
	@Column(columnDefinition="varchar(255) not null comment '城市'")
	@JsonIgnore
	private String city;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	@JsonIgnore
	private Member member;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public BigDecimal effectiveAmount() {
		if (getAmount() != null) {
			return getAmount().subtract(getFee());
		} else {
			return BigDecimal.ZERO;
		}
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+(getMember().getName()==null?"":"("+getMember().getName()+")") );
		} else {
			return null;
		}
	}
}