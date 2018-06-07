
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

	/**
	 * 状态
	 */
	public enum Status {
		/** 等待支付 */
		waiting,
		/** 开通成功 */
		success,
		/** 失败关闭 */
		failure
	}

	/** 主播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	/** 最后一次直播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
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
	@Column(columnDefinition="varchar(255) comment '是否在线'")
	private String online;

	/**  状态 */
	@Column(columnDefinition="int(11) comment '状态 { waiting:等待支付,success:开通成功,failure:支付失败}'")
	private Status status;

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

	/** 礼物合计 */
	@NotNull
	@Min(0)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '礼物合计'")
	private Long giftTotal;

	/** 文章标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_live_tag")
	@OrderBy("orders asc")
	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();


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

	public String getOnline() {
		return online;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public Long getGiftTotal() {
		return giftTotal;
	}

	public void setGiftTotal(Long giftTotal) {
		this.giftTotal = giftTotal;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().displayName() );
		} else {
			return null;
		}
	}


}