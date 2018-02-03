
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 理财账户
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_account")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_account_sequence")
public class Account extends BaseEntity {

	private static final long serialVersionUID = 49L;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 当前余额 */
	@Column(columnDefinition="decimal(21,6) not null comment '当前余额'")
	private BigDecimal balance;

	/** 结余利息 */
	@Column(columnDefinition="decimal(21,6) not null comment '结余利息'")
	private BigDecimal interest;

	/** 结息日期 */
	@Column(columnDefinition="datetime comment '结息日期'")
	private Date settleDate;


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public Date getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
}