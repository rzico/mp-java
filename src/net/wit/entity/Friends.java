
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 我的好友
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_friends")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_friends_sequence")
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

	/**
	 * 类型
	 */
	public enum Type {

		/** 好友 */
		friend,

		/** 会员 */
		customer,

		/** 一级代理 */
		vip1,

		/** 二级代理 */
		vip2,

		/** 三级代理 */
		vip3
	}

	/** 状态 */
	@JoinColumn(columnDefinition="int(11) not null comment '状态 {ask:申请,adopt:通过,black:拉黑}'")
	private Status status;

	/** 类型 */
	@JoinColumn(columnDefinition="int(11) not null comment '类型 {friend:好友,customer:会员,vip1:一级代理,vip2:二级代理,vip3:三级代理}'")
	private Type type;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 好友 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '好友'")
	private Member friend;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}


	public MapEntity getMapFriend() {
		if (getFriend() != null) {
			return new MapEntity(getFriend().getId().toString(), getFriend().getNickName()+"("+getFriend().getName()+")");
		} else {
			return null;
		}
	}

}