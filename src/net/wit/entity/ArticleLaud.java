
package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: ArticleLaud
 * @Description:  文章点赞
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "xm_article_laud")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_article_laud_sequence")
public class ArticleLaud extends BaseEntity {

	private static final long serialVersionUID = 107L;

	/** 是否显示 */
	@Column(nullable = false)
	private Boolean isShow;

	/** IP */
	@Column(nullable = false, updatable = false)
	private String ip;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;

	/** 文章 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Article article;

}