
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

	/** 运费 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition="bigint(20) not null comment '店主'")
	private Freight freight;

	/** 首重 */
	@Column(columnDefinition="decimal(21,6) not null comment '首重'")
	private BigDecimal ykg;

	/** 首重运费 */
	@Column(columnDefinition="decimal(21,6) not null comment '首重运费'")
	private BigDecimal ykgPrice;

	/** 续重 */
	@Column(columnDefinition="decimal(21,6) not null comment '续重'")
	private BigDecimal continued;

	/** 续重运费 */
	@Column(columnDefinition="decimal(21,6) not null comment '续重运费'")
	private BigDecimal continuedPrice;

	/** 可配送区域*/
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

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}