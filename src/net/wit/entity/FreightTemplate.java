
package net.wit.entity;

import javax.persistence.*;
import java.math.BigDecimal;

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

	/** 首重 */
	@Column(columnDefinition="decimal(21,6) not null comment '首重'")
	private BigDecimal continued;


	public Freight getFreight() {
		return freight;
	}

	public void setFreight(Freight freight) {
		this.freight = freight;
	}
}