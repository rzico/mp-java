
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity - 游戏
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "nt_game")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "nt_game_sequence")
public class Game extends BaseEntity {

	private static final long serialVersionUID = 49L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 投注 */
		transaction,

		/** 结果 */
		history

	}


	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 游戏名 */
	@Column(columnDefinition="varchar(255) not null comment '游戏名'")
	private String game;

	/** 桌号 */
	@Column(columnDefinition="varchar(255) not null comment '桌号'")
	private String tableNo;

	/** 局号 */
	@Column(columnDefinition="varchar(255) not null comment '局号'")
	private String roundNo;

	/** 支出金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '支出金额'")
	private BigDecimal debit;

	/** 收入金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '收入金额'")
	private BigDecimal credit;

	/** 状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '状态'")
	private Status status;

	/** 摘要 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '摘要'")
	private String memo;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getRoundNo() {
		return roundNo;
	}

	public void setRoundNo(String roundNo) {
		this.roundNo = roundNo;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}