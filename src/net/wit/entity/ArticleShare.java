
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;

/**
 * @ClassName: ArticleShare
 * @Description:  文章分享
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article_share")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_share_sequence")
public class ArticleShare extends BaseEntity {

	private static final long serialVersionUID = 12L;

	public static enum ShareType{
		/** 朋友圈  */
		timeline,
		/** 微信好友 */
		appMessage,
		/** QQ好友 */
		shareQQ,
		/** QQ空间 */
		shareQZone,
		/** 芸店好友 */
		appWeex,
		/** 复制链接 */
		copyHref,
		/** 浏览器 */
		browser
	};

	/** 分享至 */
	@Column(nullable = false)
	private ShareType shareType;

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

	/** 作者 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '作者'")
	private Member author;

	/** 文章 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Article article;

	public ShareType getShareType() {
		return shareType;
	}

	public void setShareType(ShareType shareType) {
		this.shareType = shareType;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean show) {
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