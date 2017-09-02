
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 我的好友
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "xm_friends")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_friends_sequence")
public class Friends extends BaseEntity {

	private static final long serialVersionUID = 111L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 申请 */
		ask,

		/** 通过 */
		adopt,

		/** 拉黑 */
		black
	}

	/** 状态 */
	@JoinColumn(columnDefinition="int(11) not null comment '状态'")
	private Status Status;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 好友 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '好友'")
	private Member friend;

	public Status getStatus() {
		return Status;
	}

	public void setStatus(Status status) {
		Status = status;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getFriend() {
		return friend;
	}

	public void setFriend(Member friend) {
		this.friend = friend;
	}
}