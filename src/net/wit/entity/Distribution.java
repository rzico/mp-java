
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: wx_distribution
 * @Description:  行业
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_distribution")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_distribution_sequence")
public class Distribution extends OrderEntity {

	private static final long serialVersionUID = 920L;

	/**
	 * 类型
	 */
	public enum Type {
		/**
		 * 股东分红
		 */
		dividend,
		/**
		 * 全球公排
		 */
		global,
	}

	/** 策略名称 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 类型 */
	@Column(columnDefinition="int(11) not null comment '类型 {dividend:股东分红,global:全球公排}'")
	private Type type;


	/** 股东分红/公排基数 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '股东分红'")
	private BigDecimal dividend;

	/** 直接代理/返佣比例 百分比 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '直接代理'")
	private BigDecimal percent1;

	/** 间接代理 百分比 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '间接代理'")
	private BigDecimal percent2;

	/** 三级代理 百分比 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '三级代理'")
	private BigDecimal percent3;

	/** 现金比例 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '现金比例'")
	private BigDecimal point;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 商品 */
	@OneToMany(mappedBy = "distribution", fetch = FetchType.LAZY)
	private List<Product> products = new ArrayList<>();

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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public BigDecimal getPoint() {
		return point;
	}

	public void setPoint(BigDecimal point) {
		this.point = point;
	}

	public BigDecimal getDividend() {
		return dividend;
	}

	public void setDividend(BigDecimal dividend) {
		this.dividend = dividend;
	}


	public BigDecimal calePointRate() {
		BigDecimal p = getPoint();
		return (new BigDecimal(10).subtract(p)).multiply(new BigDecimal("0.1"));
	}

	public BigDecimal caleMoneyRate() {
		BigDecimal p = getPoint();
		return p.multiply(new BigDecimal("0.1"));
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/*删除前处理*/
	@PreRemove
	public void preRemove(){
		List<Product> products = getProducts();
		if(products!=null){
			for(Product product:products){
				product.setDistribution(null);
			}

		}
	}


}