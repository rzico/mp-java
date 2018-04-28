
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * Entity -  兑换数据
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live_gift_exchange")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_gift_exchange_sequence")
public class LiveGiftExchange extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/** 用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '用户'")
	private Member member;

	/** 直播间 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '直播间'")
	private Live live;

	/**  昵称  */
	@Column(columnDefinition="varchar(255) comment '昵称'")
	private String nickname;

	/**  头像  */
	@Column(columnDefinition="varchar(255) comment '头像'")
	private String headpic;

	/** 缩略图 */
	@Length(max = 200)
	@Column(nullable = false, length = 200,columnDefinition="varchar(255) not null comment '缩略图'")
	private String thumbnail;

	/** 印票数 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '印票数'")
	private Long gift;

	/** 兑换金额 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '兑换金额'")
	private BigDecimal amount;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Live getLive() {
		return live;
	}

	public void setLive(Live live) {
		this.live = live;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getGift() {
		return gift;
	}

	public void setGift(Long gift) {
		this.gift = gift;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}