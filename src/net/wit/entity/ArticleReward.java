
package net.wit.entity;

import net.wit.MapEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: ArticleReward
 * @Description:  文章打赏
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Entity
@Table(name = "wx_article_reward")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_reward_sequence")
public class ArticleReward extends BaseEntity {

	private static final long serialVersionUID = 11L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待支付 */
		waiting,

		/** 支付成功 */
		success,

		/** 支付失败 */
		failure
	}

	/**
	 * 类型
	 */
	public enum Type {

		/** 文章 */
		article,

		/** 直播 */
		liveplay
	}

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

	/** 付款单 */
	@OneToMany(mappedBy = "articleReward", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Payment> payments = new HashSet<Payment>();

	/** 打赏金额 */
	@Column(columnDefinition="decimal(21,6) not null comment '打赏金额'")
	private BigDecimal amount;

	/** 交易佣金 */
	@Column(columnDefinition="decimal(21,6) not null comment '交易佣金'")
	private BigDecimal fee;

	/** 交易状态 */
	@Column(columnDefinition="int(11) not null default 0 comment '交易状态 {wait:等待支付,success:支付成功,failure:支付失败}'")
	private Status status;

	/** 打赏类型 */
	@Column(columnDefinition="int(11) not null default 0 comment '打赏类型'")
	private Type type;

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

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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