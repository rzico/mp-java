
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @ClassName: wx_distribution
 * @Description:  行业
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_distribution")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_distribution_sequence")
public class Distribution extends BaseEntity {

	private static final long serialVersionUID = 920L;

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
	@NotNull
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 策略名称 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 一级代理 百分比 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '一级代理'")
	private BigDecimal percent1;

	/** 二级代理 百分比 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '二级代理'")
	private BigDecimal percent2;

	/** 三级代理 百分比 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '三级代理'")
	private BigDecimal percent3;

	public Distribution.Status getStatus() {
		return status;
	}

	public void setStatus(Distribution.Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPercent1() {
		return percent1;
	}

	public void setPercent1(BigDecimal percent1) {
		this.percent1 = percent1;
	}

	public BigDecimal getPercent2() {
		return percent2;
	}

	public void setPercent2(BigDecimal percent2) {
		this.percent2 = percent2;
	}

	public BigDecimal getPercent3() {
		return percent3;
	}

	public void setPercent3(BigDecimal percent3) {
		this.percent3 = percent3;
	}
}