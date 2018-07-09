
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 代理分类

 */
@Entity
@Table(name = "ky_agent_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_agent_category_sequence")
public class AgentCategory extends OrderEntity {

	private static final long serialVersionUID = 42L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 英文称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '英文称'")
	private String english;

	/** 树路径 */
	@Column(nullable = false,columnDefinition="varchar(255) not null comment '树路径'")
	@JsonIgnore
	private String treePath;

	/** 层级 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '层级'")
	@JsonIgnore
	private Integer grade;

	/** 上级分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private AgentCategory parent;

	/** 所属代理 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Enterprise enterprise;

	/** 下级分类 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("orders asc")
	@JsonIgnore
	private Set<AgentCategory> children = new HashSet<AgentCategory>();

	/** 量表 */
	@OneToMany(mappedBy = "gaugeCategory", fetch = FetchType.LAZY)
	@Where(clause="deleted=0")
	@JsonIgnore
	private Set<Gauge> gauges = new HashSet<Gauge>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * 
	 * @param treePath
	 *            树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取层级
	 * 
	 * @return 层级
	 */
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 * 
	 * @param grade
	 *            层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	/**
	 * 获取上级分类
	 * 
	 * @return 上级分类
	 */
	public AgentCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * 
	 * @param parent
	 *            上级分类
	 */
	public void setParent(AgentCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * 
	 * @return 下级分类
	 */
	public Set<AgentCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * 
	 * @param children
	 *            下级分类
	 */
	public void setChildren(Set<AgentCategory> children) {
		this.children = children;
	}

	public Set<Gauge> getGauges() {
		return gauges;
	}

	public void setGauges(Set<Gauge> gauges) {
		this.gauges = gauges;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	/**
	 * 获取树路径
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

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {

	}

}