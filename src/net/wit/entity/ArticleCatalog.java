
package net.wit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: ArticleCatalog
 * @Description:  文章文集
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "xm_article_catalog")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_article_catalog_sequence")
public class ArticleCatalog extends OrderEntity {

	private static final long serialVersionUID = 604L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 开启  */
		enabled,

		/** 关闭  */
		disabled
	}

	/** 状态 */
	@Column(updatable = false)
	private Status status;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 文章 */
	@OneToMany(mappedBy = "articleCatalog", fetch = FetchType.LAZY)
	private Set<Article> articles = new HashSet<Article>();

	public ArticleCatalog.Status getStatus() {
		return status;
	}

	public void setStatus(ArticleCatalog.Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}