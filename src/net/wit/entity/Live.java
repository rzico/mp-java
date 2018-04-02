
package net.wit.entity;

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity -  直播数据
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_sequence")
public class Live extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/** 主播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '主播'")
	private Member member;

	/** 群组 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '群组'")
	private LiveGroup liveGroup;

	/** 最后一次直播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '最后一次直播'")
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

	/**  推流地址  */
	@Column(columnDefinition="varchar(255) comment '推流地址'")
	private String pushUrl;

	/**  观看地址  */
	@Column(columnDefinition="varchar(255) comment '观看地址'")
	private String playUrl;

	/**  状态 0:上线 1:下线  */
	@Column(columnDefinition="varchar(255) comment '状态'")
	private String status;

	/**  回放地址  */
	@Column(columnDefinition="varchar(255) comment '回放地址'")
	private String hls_play_url;

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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LiveGroup getLiveGroup() {
		return liveGroup;
	}

	public void setLiveGroup(LiveGroup liveGroup) {
		this.liveGroup = liveGroup;
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

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHls_play_url() {
		return hls_play_url;
	}

	public void setHls_play_url(String hls_play_url) {
		this.hls_play_url = hls_play_url;
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

	public Long getGift() {
		return gift;
	}

	public void setGift(Long gift) {
		this.gift = gift;
	}

	public LiveTape getLiveTape() {
		return liveTape;
	}

	public void setLiveTape(LiveTape liveTape) {
		this.liveTape = liveTape;
	}
}