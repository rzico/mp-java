
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.util.JsonUtils;
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

	/** 副标题  */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '副标题'")
	private String subTitle;

	/** 课程标签   */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '课程标签'")
	private String tagNames ;

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

	/** 课程目标 */
	@Lob
	@Column(columnDefinition="longtext comment '课程目标'")
	@JsonIgnore
	private String content1;

	/** 课程内容 */
	@Lob
	@Column(columnDefinition="longtext comment '课程内容'")
	@JsonIgnore
	private String content2;


	/** 授课形式 */
	@Lob
	@Column(columnDefinition="longtext comment '授课形式'")
	@JsonIgnore
	private String content3;

	/** 讲师头像 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '讲师头像'")
	private String contentLogo;

	/** 讲师简介 */
	@Lob
	@Column(columnDefinition="longtext comment '讲师简介'")
	@JsonIgnore
	private String content4;

	/** 课程大纲 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '讲师头像'")
	private String content5;

	/** 适合谁听 */
	@Lob
	@Column(columnDefinition="longtext comment '适合谁听'")
	@JsonIgnore
	private String content6;

	/** 您将获得 */
	@Lob
	@Column(columnDefinition="longtext comment '您将获得'")
	@JsonIgnore
	private String content7;

	/** 往期回顾 图片json */
	@Lob
	@Column(columnDefinition="longtext comment '往期回顾'")
	@JsonIgnore
	private String images;

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@JsonIgnore
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getContent3() {
		return content3;
	}

	public void setContent3(String content3) {
		this.content3 = content3;
	}

	public String getContentLogo() {
		return contentLogo;
	}

	public void setContentLogo(String contentLogo) {
		this.contentLogo = contentLogo;
	}

	public String getContent4() {
		return content4;
	}

	public void setContent4(String content4) {
		this.content4 = content4;
	}

	public String getContent5() {
		return content5;
	}

	public void setContent5(String content5) {
		this.content5 = content5;
	}

	public String getContent6() {
		return content6;
	}

	public void setContent6(String content6) {
		this.content6 = content6;
	}

	public String getContent7() {
		return content7;
	}

	public void setContent7(String content7) {
		this.content7 = content7;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTagNames() {
		return tagNames;
	}

	public void setTagNames(String tagNames) {
		this.tagNames = tagNames;
	}



	public List<String> getArrayImages() {
		if (getImages()!=null) {
		  List<String> data = JsonUtils.toObject(getImages(),List.class);
		  return data;
		} else {
			return null;
		}
	}
}