
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
@Table(name = "xm_artcile_review")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_artcile_review_sequence")
public class  ArtcileReview extends BaseEntity {

	private static final long serialVersionUID = 106L;

	/** 内容 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String content;

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
	private Artcile artcile;

	/** 评论 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private ArtcileReview forArtcileReview;

	/** 回复 */
	@OneToMany(mappedBy = "forArtcileReview", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<ArtcileReview> replyArtcileReviews = new HashSet<ArtcileReview>();

	/**
	 * 获取访问路径
	 * 
	 * @return 访问路径
	 */
	@Transient
	public String getPath() {
		if (getProduct() != null && getProduct().getId() != null) {
			return PATH_PREFIX + "/" + getProduct().getId() + PATH_SUFFIX;
		}
		return null;
	}

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

	public Artcile getArtcile() {
		return artcile;
	}

	public void setArtcile(Artcile artcile) {
		this.artcile = artcile;
	}

	public ArtcileReview getForArtcileReview() {
		return forArtcileReview;
	}

	public void setForArtcileReview(ArtcileReview forArtcileReview) {
		this.forArtcileReview = forArtcileReview;
	}

	public Set<ArtcileReview> getReplyArtcileReviews() {
		return replyArtcileReviews;
	}

	public void setReplyArtcileReviews(Set<ArtcileReview> replyArtcileReviews) {
		this.replyArtcileReviews = replyArtcileReviews;
	}
}