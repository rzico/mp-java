package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 地区
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_road")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_road_sequence")
public class Road extends OrderEntity {

	private static final long serialVersionUID = 2L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 编码 */
	@NotNull
	@Length(max = 100)
	@Column(columnDefinition="varchar(50) not null comment '编码'")
	private String code;

	/** 名称 */
	@NotNull
	@Length(max = 100)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 地区 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) comment '地区'")
	@JsonIgnore
	private Area area;

	/** 定位 */
	@Embedded
	@JsonIgnore
	private Location location;

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


	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}