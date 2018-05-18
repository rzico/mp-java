
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: Article_Review
 * @Description:  文章评论
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article_review")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_review_sequence")
public class  ArticleReview extends BaseEntity {

	private static final long serialVersionUID = 10L;

	/** 内容 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition=" varchar(255) comment '内容'")
	private String content;

	/** 晒图 */
	@Lob
	@Column(columnDefinition="longtext comment '晒图'")
	private String images;

	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit comment '是否删除'")
	private Boolean deleted;

	/** IP */
	@Column(columnDefinition=" varchar(255) comment 'IP'")
	private String ip;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment 'IP'")
	private Member member;

	/** 文章 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '文章'")
	private Article article;

	/** 作者 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '作者'")
	private Member author;

	/** 评论谁 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '评论谁'")
	private ArticleReview forArticleReview;

	/** 回复 */
	@OneToMany(mappedBy = "forArticleReview", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<ArticleReview> replyArtcileReviews = new HashSet<ArticleReview>();

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public ArticleReview getForArticleReview() {
		return forArticleReview;
	}

	public void setForArticleReview(ArticleReview forArticleReview) {
		this.forArticleReview = forArticleReview;
	}

	public Set<ArticleReview> getReplyArtcileReviews() {
		return replyArtcileReviews;
	}

	public void setReplyArtcileReviews(Set<ArticleReview> replyArtcileReviews) {
		this.replyArtcileReviews = replyArtcileReviews;
	}

	public Member getAuthor() {
		return author;
	}

	public void setAuthor(Member author) {
		this.author = author;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
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