
package net.wit.entity;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity -  观看记录
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live_data")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_data_sequence")
public class LiveData extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/** 用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '用户'")
	private Member member;

	/** 直播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '直播'")
	private Live live;

	/** 直播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '直播'")
	private LiveTape liveTape;

	/**  标题  */
	@Column(columnDefinition="varchar(255) comment '标题'")
	private String title;

	/**  昵称  */
	@Column(columnDefinition="varchar(255) comment '昵称'")
	private String nickname;

	/**  封面  */
	@Column(columnDefinition="varchar(255) comment '封面'")
	private String frontcover;

	/**  头像  */
	@Column(columnDefinition="varchar(255) comment '头像'")
	private String headpic;

	/**  位置  */
	@Column(columnDefinition="varchar(255) comment '位置'")
	private String location;

	/**  观看地址  */
	@Column(columnDefinition="varchar(255) comment '观看地址'")
	private String playUrl;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFrontcover() {
		return frontcover;
	}

	public void setFrontcover(String frontcover) {
		this.frontcover = frontcover;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public LiveTape getLiveTape() {
		return liveTape;
	}

	public void setLiveTape(LiveTape liveTape) {
		this.liveTape = liveTape;
	}

	public Live getLive() {
		return live;
	}

	public void setLive(Live live) {
		this.live = live;
	}
}