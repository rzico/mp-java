
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;

/**
 * @ClassName: ArticleFollow
 * @Description:  我关注的
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_member_follow")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_member_follow_sequence")
public class MemberFollow extends BaseEntity {

	private static final long serialVersionUID = 128L;

	/** IP */
	@Column(columnDefinition=" varchar(255) comment 'IP'")
	private String ip;

	/** 我自已 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '我自已'")
	private Member member;

	/** 关注的 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '关注的'")
	private Member follow;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getFollow() {
		return follow;
	}

	public void setFollow(Member follow) {
		this.follow = follow;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}

	public MapEntity getMapFollow() {
		if (getFollow() != null) {
			return new MapEntity(getFollow().getId().toString(), getFollow().getNickName()+"("+getFollow().getName()+")");
		} else {
			return null;
		}
	}
}