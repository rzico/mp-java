
package net.wit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import net.wit.MapEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: ArticleCategory
 * @Description:  文章分类
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_article_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_article_category_sequence")
public class ArticleCategory extends OrderEntity {

	private static final long serialVersionUID = 105L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

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
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 页面标题 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '页面标题'")
	private String seoTitle;

	/** 页面关键词 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '页面关键词'")
	private String seoKeywords;

	/** 页面描述 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '页面描述'")
	private String seoDescription;

	/** 树路径 */
	@Column(nullable = false)
	private String treePath;

	/** 层级 */
	@Column(nullable = false)
	private Integer grade;

	/** 上级分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '上级分类'")
	private ArticleCategory parent;

	/** 下级分类 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("orders asc")
	private Set<ArticleCategory> children = new HashSet<ArticleCategory>();

	/** 文章 */
	@OneToMany(mappedBy = "articleCategory", fetch = FetchType.LAZY)
	private Set<Article> articles = new HashSet<Article>();

	public ArticleCategory.Status getStatus() {
		return status;
	}

	public void setStatus(ArticleCategory.Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public net.wit.entity.ArticleCategory getParent() {
		return parent;
	}

	public void setParent(net.wit.entity.ArticleCategory parent) {
		this.parent = parent;
	}

	public Set<ArticleCategory> getChildren() {
		return children;
	}

	public void setChildren(Set<ArticleCategory> children) {
		this.children = children;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	@Transient
	public List<Long> getTreePaths() {
		List<Long> treePaths = new ArrayList<Long>();
		String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		if (ids != null) {
			for (String id : ids) {
				treePaths.add(Long.valueOf(id));
			}
		}
		return treePaths;
	}

	public MapEntity getMapArticles() {
		if (getArticles() != null) {
			return new MapEntity("","文章("+new Long(getArticles().size()).toString()+")");
		} else {
			return new MapEntity("","文章(0)");
		}
	}
}