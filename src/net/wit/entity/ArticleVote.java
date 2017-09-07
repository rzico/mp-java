
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @ClassName: ArticleVote
 * @Description:  文章投票
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article_vote")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_vote_sequence")
public class ArticleVote extends BaseEntity {

	private static final long serialVersionUID = 126L;

	/** IP */
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) comment 'IP'")
	private String ip;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '会员'")
	private Member member;

	/** 作者 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '作者'")
	private Member author;

	/** 文章 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '文章'")
	private Article article;

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

	public Member getAuthor() {
		return author;
	}

	public void setAuthor(Member author) {
		this.author = author;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}

	public MapEntity getMapRuthor() {
		if (getAuthor() != null) {
			return new MapEntity(getAuthor().getId().toString(), getAuthor().getNickName()+"("+getAuthor().getName()+")");
		} else {
			return null;
		}
	}

	public MapEntity getMapArticle() {
		if (getArticle() != null) {
			return new MapEntity(getArticle().getId().toString(), getArticle().getTitle());
		} else {
			return null;
		}
	}

}