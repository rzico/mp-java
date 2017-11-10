
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @ClassName: TopicBill
 * @Description:  专题费用
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_topic_bill")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_topic_bill_sequence")
public class TopicBill extends BaseEntity {

	private static final long serialVersionUID = 59L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待支付 */
		wait,

		/** 支付成功 */
		success,

		/** 支付失败 */
		failure
	}

	/** IP */
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) comment 'IP'")
	private String ip;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 付款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '付款单'")
	private Payment payment;

	/** 金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '金额'")
	private BigDecimal amount;

	/** 交易状态 */
	@Column(columnDefinition="int(11) not null comment '交易状态 {wait:等待支付,success:支付成功,failure:支付失败}'")
	private Status status;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}

}