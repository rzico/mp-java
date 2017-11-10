package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: BindUser
 * @Description: 第三方账号绑定
 * @author Administrator
 * @date 2014年10月13日 下午2:18:09
 */
@Entity
@Table(name = "wx_bind_user")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_bind_user_sequence")
public class BindUser extends BaseEntity {

	private static final long serialVersionUID = 15L;

	/** 类型 */
	public enum Type {
		/** 微信 */
		weixin,
		/** 支付宝 */
		alipay
	}

	/** 用户号 */
	@NotNull
	@Column(nullable = false)
	private String openId;

	/** 联盟号 */
	@NotNull
	@Column(nullable = false)
	private String unionId;

	/** 应用 */
	@NotNull
	@Column(nullable = false)
	private String appId;

	/** 类型 */
	@NotNull
	@Column(nullable = false)
	private Type type;

	/** 绑定的会员 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Member member;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

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
}
