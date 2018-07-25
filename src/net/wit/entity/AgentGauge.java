
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 代理量表

 */
@Entity
@Table(name = "ky_agent_gauge")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_agent_gauge_sequence")
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

	/** 现价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '现价'")
	private BigDecimal price;

	/** 副标题 */
	@Length(max = 200)
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '副标题'")
	private String subTitle;

	/** 缩列图 */
	@NotNull
	@Column(columnDefinition="varchar(255) not null comment '缩列图'")
	private String thumbnail;

	/** 量表标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ky_agent_gauge_tag")
	@OrderBy("orders asc")
	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}