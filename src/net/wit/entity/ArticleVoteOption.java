
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ArticleVoteOption
 * @Description:  文章题库
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article_vote_option")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_vote_option_sequence")
public class ArticleVoteOption extends BaseEntity {

	private static final long serialVersionUID = 126L;

	/**  题目 */
	@Column(nullable = false, updatable = false,columnDefinition="varchar(255) comment '题目'")
	private String title;

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

	/** 投票项 */
	@ElementCollection
	@CollectionTable(name = "wx_article_vote_option_items")
	private List<String> items = new ArrayList<String>();

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

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}