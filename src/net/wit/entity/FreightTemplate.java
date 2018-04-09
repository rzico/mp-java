
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity -  运费模板
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_freight_template")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_freight_template_sequence")
public class FreightTemplate extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/**
	 * 运费
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition = "bigint(20) not null comment '店主'")
	private Freight freight;

	/**
	 * 首重
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '首重'")
	private BigDecimal ykg;

	/**
	 * 首重运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '首重运费'")
	private BigDecimal ykgPrice;

	/**
	 * 续重
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重'")
	private BigDecimal continued;

	/**
	 * 续重运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重运费'")
	private BigDecimal continuedPrice;

	/**
	 * 阶段1
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重'")
	private BigDecimal segment1;

	/**
	 * 阶段1运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重运费'")
	private BigDecimal segment1Price;

	/**
	 * 阶段2
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重'")
	private BigDecimal segment2;

	/**
	 * 阶段2运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重运费'")
	private BigDecimal segment2Price;

	/**
	 * 阶段3
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重'")
	private BigDecimal segment3;

	/**
	 * 阶段3运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重运费'")
	private BigDecimal segment3Price;

	/**
	 * 阶段4
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重'")
	private BigDecimal segment4;

	/**
	 * 阶段4运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重运费'")
	private BigDecimal segment4Price;

	/**
	 * 阶段5
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重'")
	private BigDecimal segment5;

	/**
	 * 阶段5运费
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '续重运费'")
	private BigDecimal segment5Price;

	/**
	 * 工资比例 80%
	 */
	@Column(columnDefinition = "decimal(21,6) not null comment '工资比例'")
	private BigDecimal scale;

	/**
	 * 可配送区域
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_freight_area")
	@JsonIgnore
	private List<Area> areas = new ArrayList<Area>();

	public Freight getFreight() {
		return freight;
	}

	public void setFreight(Freight freight) {
		this.freight = freight;
	}

	public BigDecimal getYkg() {
		return ykg;
	}

	public void setYkg(BigDecimal ykg) {
		this.ykg = ykg;
	}

	public BigDecimal getYkgPrice() {
		return ykgPrice;
	}

	public void setYkgPrice(BigDecimal ykgPrice) {
		this.ykgPrice = ykgPrice;
	}

	public BigDecimal getContinued() {
		return continued;
	}

	public void setContinued(BigDecimal continued) {
		this.continued = continued;
	}

	public BigDecimal getContinuedPrice() {
		return continuedPrice;
	}

	public void setContinuedPrice(BigDecimal continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	public BigDecimal getSegment1() {
		return segment1;
	}

	public void setSegment1(BigDecimal segment1) {
		this.segment1 = segment1;
	}

	public BigDecimal getSegment1Price() {
		return segment1Price;
	}

	public void setSegment1Price(BigDecimal segment1Price) {
		this.segment1Price = segment1Price;
	}

	public BigDecimal getSegment2() {
		return segment2;
	}

	public void setSegment2(BigDecimal segment2) {
		this.segment2 = segment2;
	}

	public BigDecimal getSegment2Price() {
		return segment2Price;
	}

	public void setSegment2Price(BigDecimal segment2Price) {
		this.segment2Price = segment2Price;
	}

	public BigDecimal getSegment3() {
		return segment3;
	}

	public void setSegment3(BigDecimal segment3) {
		this.segment3 = segment3;
	}

	public BigDecimal getSegment3Price() {
		return segment3Price;
	}

	public void setSegment3Price(BigDecimal segment3Price) {
		this.segment3Price = segment3Price;
	}

	public BigDecimal getSegment4() {
		return segment4;
	}

	public void setSegment4(BigDecimal segment4) {
		this.segment4 = segment4;
	}

	public BigDecimal getSegment4Price() {
		return segment4Price;
	}

	public void setSegment4Price(BigDecimal segment4Price) {
		this.segment4Price = segment4Price;
	}

	public BigDecimal getSegment5() {
		return segment5;
	}

	public void setSegment5(BigDecimal segment5) {
		this.segment5 = segment5;
	}

	public BigDecimal getSegment5Price() {
		return segment5Price;
	}

	public void setSegment5Price(BigDecimal segment5Price) {
		this.segment5Price = segment5Price;
	}

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}