
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
 * @ClassName: CourseOrder
 * @Description:  我的学习
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_course_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_course_order_sequence")
public class CourseOrder extends BaseEntity {

	private static final long serialVersionUID = 33L;

	/**
	 * 订单状态
	 */
	public enum OrderStatus {

		/** 未确认 */
		unconfirmed,

		/** 已确认 */
		confirmed,

		/** 已完成 */
		completed,

		/** 已取消 */
		cancelled
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
	private OrderStatus orderStatus;

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

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Enterprise enterprise;

	/** 课程 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Course course;

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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}