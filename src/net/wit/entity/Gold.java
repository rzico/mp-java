
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity -  金币记录
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "gm_gold")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "gm_gold_sequence")
public class Gold extends BaseEntity {

	private static final long serialVersionUID = 23L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 充值  收入 */
		recharge,

		/** 兑换  支出 */
		exchange,

		/** 下注  支出 */
		transaction,

		/** 收益  收入 */
		history,

		/** 打赏  支出 */
		reward,

		/** 弹幕  支出 */
		barrage
	}

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {recharge:充值,transfer:支付,transaction:退款,history:收益,reward:打赏}'")
	private Type type;

	/** 收入 */
	@Column(columnDefinition="bigint(20) not null comment '收入'")
	private Long credit;

	/** 支出 */
	@Column(columnDefinition="bigint(20) not null comment '支出'")
	private Long debit;

	/** 当前余额 */
	@Column(columnDefinition="bigint(20) not null comment '当前余额'")
	private Long balance;

	/** 操作员 */
	@Column(columnDefinition="varchar(255) comment '操作员'")
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '备注'")
	private String memo;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '备注'")
	@JsonIgnore
	private Member member;


	/** 充值 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '充值'")
	@JsonIgnore
	private GoldBuy boldBuy;

	/** 提现 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '提现'")
	@JsonIgnore
	private GoldExchange goldExchange;

	/** 游戏 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '游戏'")
	@JsonIgnore
	private Game game;

	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit comment '是否删除'")
	private Boolean deleted;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Long getCredit() {
		return credit;
	}

	public void setCredit(Long credit) {
		this.credit = credit;
	}

	public Long getDebit() {
		return debit;
	}

	public void setDebit(Long debit) {
		this.debit = debit;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public GoldBuy getBoldBuy() {
		return boldBuy;
	}

	public void setBoldBuy(GoldBuy boldBuy) {
		this.boldBuy = boldBuy;
	}

	public GoldExchange getGoldExchange() {
		return goldExchange;
	}

	public void setGoldExchange(GoldExchange goldExchange) {
		this.goldExchange = goldExchange;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}