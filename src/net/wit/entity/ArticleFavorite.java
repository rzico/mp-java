
package net.wit.entity;

import javax.persistence.*;

/**
 * @ClassName: Article_Favorite
 * @Description:  文章收藏
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "xm_article_favorite")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_article_favorite_sequence")
public class ArticleFavorite extends BaseEntity {

	private static final long serialVersionUID = 106L;

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