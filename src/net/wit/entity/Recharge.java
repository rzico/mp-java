
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 充值单
 *
 * @author 降魔战队
 * @version 3.0
 */
@Entity
@Table(name = "wx_recharge")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_recharge_sequence")
public class Recharge extends BaseEntity {

	private static final long serialVersionUID = 62L;

	/**
	 * 方式
	 */
	public enum Method {

		/** 在线支付 */
		online,

		/** 线下支付 */
		offline
	}

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待付款 */
		waiting,

		/** 提交成功 */
		confirmed,

		/** 充值成功 */
		success,

		/** 充值失败 */
		failure
	}

	/** 编号 */
	@Column(columnDefinition="varchar(50) not null unique comment '编号'")
	private String sn;

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '方式 {online:在线支付,offline:线下支付}'")
	private Method method;

	/** 状态 */
	@Column(columnDefinition="int(11) not null comment '状态 {waiting:等待付款,confirmed:提交成功,success:充值成功,failure:充值失败}'")
	private Status status;

	/** 充值金额 */
	@NotNull
	@Min(0)
	@Column(columnDefinition="decimal(21,3) not null default 0 comment '充值金额'")
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
	@OneToMany(mappedBy = "transfer", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Deposit> deposits = new ArrayList<Deposit>();

	/** 凭证号 */
	@Column(columnDefinition="varchar(255) comment '凭证号'")
	private String voucher;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	@JsonIgnore
	private Member member;

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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
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