
package net.wit.entity;

import javax.persistence.*;

/**
 * Entity -  运费设置
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_freight")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_freight_sequence")
public class Freight extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/**
	 *  类型
	 */
	public enum Type {
		/** 快递计件 */
		piece,
		/** 快递计重 */
		weight,
		/** 阶梯计件 */
		segment
	}

	/**
	 *  付款方式
	 */
	public enum Method {
		/** 买家付款 */
		member,
		/** 卖家包邮 */
		seller
	}

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {recharge:充值,payment:支付,refunds:退款}'")
	private Type type;

	/** 付款方式 */
	@Column(columnDefinition="int(11) not null comment '付款方式 {member:买家付款,seller:卖家包邮}'")
	private Method method;

	/** 店主 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '店主'")
	private Member member;

	/**  名称  */
	@Column(columnDefinition="varchar(255) comment '名称'")
	private String name;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
}