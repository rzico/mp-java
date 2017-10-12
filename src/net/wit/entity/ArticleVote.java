
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

	public static enum VoteType{
		/** 单选 */
		 radiobox,
		/** 多选 */
		 checkbox,
		/** 不限 */
		 nolimit
	};

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

	/** 题目 */
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '题目'")
	private String title;

	/** 答案 */
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '答案'")
	private String value;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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