
package net.wit.entity;

import javax.persistence.*;

/**
 * @ClassName: ArticleFollow
 * @Description:  我关注的
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "xm_member_follow")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_member_follow_sequence")
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

}