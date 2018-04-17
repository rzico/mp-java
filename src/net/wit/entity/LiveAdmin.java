
package net.wit.entity;

import javax.persistence.*;

/**
 * Entity -  直播管理
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live_admin")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_admin_sequence")
public class LiveAdmin extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/**  主播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '主播'")
	private Live live;

	/**  管理员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '管理员'")
	private Member member;

	public Live getLive() {
		return live;
	}

	public void setLive(Live live) {
		this.live = live;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}