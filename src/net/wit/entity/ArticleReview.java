
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: Article_Review
 * @Description:  文章评论
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "xm_article_review")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_article_review_sequence")
public class  ArticleReview extends BaseEntity {

	private static final long serialVersionUID = 109L;

	/** 内容 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition=" varchar(255) comment '内容'")
	private String content;

	/** 是否显示 */
	@Column(nullable = false)
	private Boolean isShow;

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

	public Boolean getShow() {
		return isShow;
	}

	public void setShow(Boolean show) {
		isShow = show;
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
}