
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Course
 * @Description:  咨询师
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_course")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_course_sequence")
public class Course extends OrderEntity {

	private static final long serialVersionUID = 33L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 启用  */
		enabled,

		/** 关闭  */
		disabled
	}

	/**
	 * 类型
	 */
	public enum Type {
		/** 公共  */
		_public,

		/** 私有  */
		_private
	}

	/** 状态 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 类型 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '类型 {_public:公共,_private:私有}'")
	private Type type;

	/** 名称 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 缩例图 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '缩例图'")
	private String thumbnail;

	/** 销售价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '销售价'")
	private BigDecimal price;


	/** 报名数 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="bigint(20) not null default 0 comment '报名数'")
	private Long signup;

	/** 阅读数 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="bigint(20) not null default 0 comment '阅读数'")
	private Long hits;

	/** 介绍 */
	@Lob
	@Column(columnDefinition="longtext comment '介绍'")
	@JsonIgnore
	private String content;

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Enterprise enterprise;


	/** 标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_course_tag")
	@OrderBy("orders asc")
	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();


	/** 是否删除 */
	@NotNull
	@Column(columnDefinition="bit not null default 0 comment '是否删除'")
	@JsonIgnore
	private Boolean deleted;

	public Course.Status getStatus() {
		return status;
	}

	public void setStatus(Course.Status status) {
		this.status = status;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getSignup() {
		return signup;
	}

	public void setSignup(Long signup) {
		this.signup = signup;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}