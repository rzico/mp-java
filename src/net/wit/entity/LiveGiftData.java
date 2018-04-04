
package net.wit.entity;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity -  打赏数据
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live_gift_data")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_gift_data_sequence")
public class LiveGiftData extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/** 用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '用户'")
	private Member member;

	/** 直播场次 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '直播场次'")
	private LiveTape liveTape;

	/** 礼物 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '礼物'")
	private LiveGift liveGift;

	/**  昵称  */
	@Column(columnDefinition="varchar(255) comment '昵称'")
	private String nickname;

	/**  头像  */
	@Column(columnDefinition="varchar(255) comment '头像'")
	private String headpic;

	/**  礼物名  */
	@Column(columnDefinition="varchar(255) comment '礼物名'")
	private String giftName;

	/**  礼物价值  */
	@Column(columnDefinition="varchar(255) comment '礼物价值'")
	private String price;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LiveTape getLiveTape() {
		return liveTape;
	}

	public void setLiveTape(LiveTape liveTape) {
		this.liveTape = liveTape;
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

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}