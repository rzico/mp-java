
package net.wit.entity;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity -  直播场次
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live_tape")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_tape_sequence")
public class LiveTape extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/** 房间 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '房间'")
	private Live live;

	/** 主播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '主播'")
	private Member member;

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

	/**  推流地址  */
	@Column(columnDefinition="varchar(255) comment '推流地址'")
	private String pushUrl;

	/**  观看地址  */
	@Column(columnDefinition="varchar(255) comment '观看地址'")
	private String playUrl;

	/**  回放地址  */
	@Column(columnDefinition="varchar(255) comment '回放地址'")
	private String hlsPlayUrl;

	/** 在线数 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '在线数'")
	private Long viewerCount;

	/** 点赞数 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '点赞数'")
	private Long likeCount;

	/** 礼物数 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '礼物数'")
	private Long gift;

	/** 结束时间 */
	@DateBridge(resolution = Resolution.SECOND)
	@Column(updatable = false,columnDefinition="datetime not null comment '结束时间'")
	private Date endTime;

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

	public String getHlsPlayUrl() {
		return hlsPlayUrl;
	}

	public void setHlsPlayUrl(String hlsPlayUrl) {
		this.hlsPlayUrl = hlsPlayUrl;
	}

	public Long getViewerCount() {
		return viewerCount;
	}

	public void setViewerCount(Long viewerCount) {
		this.viewerCount = viewerCount;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public Long getGift() {
		return gift;
	}

	public void setGift(Long gift) {
		this.gift = gift;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}