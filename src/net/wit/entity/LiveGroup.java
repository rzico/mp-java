
package net.wit.entity;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity -  直播群组
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_live_group")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_group_sequence")
public class LiveGroup extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/**  用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '用户'")
	private Member member;

	/**  主播 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '主播'")
	private Member liveMember;

	/**  昵称  */
	@Column(columnDefinition="varchar(255) comment '昵称'")
	private String nickname;

	/**  头像  */
	@Column(columnDefinition="varchar(255) comment '头像'")
	private String headpic;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getLiveMember() {
		return liveMember;
	}

	public void setLiveMember(Member liveMember) {
		this.liveMember = liveMember;
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
}