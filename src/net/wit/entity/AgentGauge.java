
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 代理量表

 */
@Entity
@Table(name = "ky_agent_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_agent_category_sequence")
public class AgentGauge extends OrderEntity {

	private static final long serialVersionUID = 42L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private AgentCategory agentCategory;

	/** 量表 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Gauge gauge;

	/** 所属代理 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Enterprise enterprise;

	/** 主标题 */
	@Length(max = 200)
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '主标题'")
	private String title;

	/** 副标题 */
	@Length(max = 200)
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '副标题'")
	private String subTitle;

	/** 缩列图 */
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '缩列图'")
	private String thumbnail;


	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {

	}

	public AgentCategory getAgentCategory() {
		return agentCategory;
	}

	public void setAgentCategory(AgentCategory agentCategory) {
		this.agentCategory = agentCategory;
	}

	public Gauge getGauge() {
		return gauge;
	}

	public void setGauge(Gauge gauge) {
		this.gauge = gauge;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}


}