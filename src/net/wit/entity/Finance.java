
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity - 理财
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_finance")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_finance_sequence")
public class Finance extends BaseEntity {

	private static final long serialVersionUID = 49L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 存款 */
		credit,

		/** 取消 */
		transfer,
	}

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {recharge:充值,payment:支付,refunds:退款}'")
	private Deposit.Type type;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 存款金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '存款金额'")
	private BigDecimal credit;

	/** 支出金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '取款金额'")
	private BigDecimal debit;

	/** 当前余额 */
	@Column(columnDefinition="decimal(21,6) not null comment '当前余额'")
	private BigDecimal balance;

	/** 摘要 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '摘要'")
	private String memo;

	public Deposit.Type getType() {
		return type;
	}

	public void setType(Deposit.Type type) {
		this.type = type;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}