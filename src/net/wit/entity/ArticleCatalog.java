
package net.wit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
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
@Table(name = "wx_article_catalog")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_catalog_sequence")
public class ArticleCatalog extends OrderEntity {

	private static final long serialVersionUID = 4L;

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
	@NotEmpty
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@NotEmpty
	@JoinColumn(columnDefinition="bigint(20) not null comment '点赞数'")
	@JsonIgnore
	private Member member;

	/** 文章 */
	@OneToMany(mappedBy = "articleCatalog", fetch = FetchType.LAZY)
	@JsonIgnore
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

	public MapEntity getMapArticles() {
		if (getArticles() != null) {
			return new MapEntity("","文章("+new Long(getArticles().size()).toString()+")");
		} else {
			return new MapEntity("","文章(0)");
		}
	}
}