
package net.wit.entity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 商品
 * 
 */

@Entity
@Table(name = "wx_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_product_sequence")
public class Product extends OrderEntity {

	private static final long serialVersionUID = 41L;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	/** 编号 */
	@Length(max = 50)
	@Column(nullable = false, length = 100,columnDefinition="varchar(50) not null comment '编号'")
	private String sn;

	/** 名称 */
	@Length(max = 200)
	@Column(nullable = false, length = 200,columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 缩略图 */
	@Length(max = 200)
	@Column(nullable = false, length = 200,columnDefinition="varchar(255) not null comment '缩略图'")
	private String thumbnail;

	/** 规格1 */
	@Length(max = 50)
	@Column(length = 50,columnDefinition="varchar(50) comment '规格1'")
	private String spec1;

	/** 规格2 */
	@Length(max = 50)
	@Column(length = 50,columnDefinition="varchar(50) comment '规格2'")
	private String spec2;

	/** 销售价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '销售价'")
	private BigDecimal price;

	/** 一级代理价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '一级代理价'")
	private BigDecimal vip1Price;

	/** 二级代理价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '二级代理价'")
	private BigDecimal vip2Price;

	/** 三级代理价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '三级代理价'")
	private BigDecimal vip3Price;

	/** 成本价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '成本价'")
	private BigDecimal cost;

	/** 市场价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '市场价'")
	private BigDecimal marketPrice;

	/** 库存 */
	@Column(nullable = false,columnDefinition="int(11) not null default 0 comment '库存'")
	private Integer stock;

	/** 已分配库存 */
	@Column(nullable = false,columnDefinition="int(11) not null default 0 comment '已分配库存'")
	private Integer allocatedStock;

	/** 单位 */
	@Length(max = 10)
	@Column(nullable = false,columnDefinition="varchar(10) not null comment '单位'")
	private String unit;

	/** 重量 */
	@Length(max = 50)
	@Column(nullable = false,columnDefinition="int(11) not null comment '重量'")
	private Integer weight;

	/** 赠送积分 */
	@Column(nullable = false,columnDefinition="bigint(20) not null comment '赠送积分'")
	private Long point;

	/** 是否上架 */
	@Column(nullable = false,columnDefinition="bit not null comment '是否上架'")
	private Boolean isMarketable;

	/** 是否列出 */
	@Column(nullable = false,columnDefinition="bit not null comment '是否列出'")
	private Boolean isList;

	/** 商品分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private ProductCategory productCategory;

	/** 货品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Goods goods;

	/** 分销方式  */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Distribution distribution;

	/** 是否删除 */
	@Column(nullable = false,columnDefinition="bit not null comment '是否删除'")
	private Boolean deleted;

	/** 商品库存*/
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<ProductStock> productStocks = new ArrayList<>();

	/** 标签*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "wx_product_tag")
	@OrderBy("orders asc")
	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec1() {
		return spec1;
	}

	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	public String getSpec2() {
		return spec2;
	}

	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getVip1Price() {
		return vip1Price;
	}

	public void setVip1Price(BigDecimal vip1Price) {
		this.vip1Price = vip1Price;
	}

	public BigDecimal getVip2Price() {
		return vip2Price;
	}

	public void setVip2Price(BigDecimal vip2Price) {
		this.vip2Price = vip2Price;
	}

	public BigDecimal getVip3Price() {
		return vip3Price;
	}

	public void setVip3Price(BigDecimal vip3Price) {
		this.vip3Price = vip3Price;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean marketable) {
		isMarketable = marketable;
	}

	public Boolean getIsList() {
		return isList;
	}

	public void setIsList(Boolean list) {
		isList = list;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<ProductStock> getProductStocks() {
		return productStocks;
	}

	public void setProductStocks(List<ProductStock> productStocks) {
		this.productStocks = productStocks;
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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}


	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getAllocatedStock() {
		return allocatedStock;
	}

	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	public MapEntity getMapMember() {
		if (getMember() != null) {
			return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
		} else {
			return null;
		}
	}

	public MapEntity getMapProductCategory(){
		if(getProductCategory() != null){
			return new MapEntity(getProductCategory().getId().toString(),getProductCategory().getName());
		}else{
			return null;
		}
	}

	public MapEntity getMapTags() {
		String tagStr = "";
		if (getTags() != null) {
			for (Tag tag:getTags()) {
				if ("".equals(tagStr)) {
					tagStr = tag.getName();
				} else {
					tagStr = tagStr.concat(","+tag.getName());
				}
			}
			return new MapEntity("",tagStr);
		} else {
			return null;
		}
	}

	/**
	 * 获取可用库存
	 *
	 * @return 可用库存
	 */
	@Transient
	public Integer getAvailableStock() {
		Integer availableStock = null;
		if (getStock() != null && getAllocatedStock() != null) {
			availableStock = getStock() - getAllocatedStock();
			if (availableStock < 0) {
				availableStock = 0;
			}
		}
		return availableStock;
	}

	/**
	 * 获取是否库存不足
	 *
	 * @return 是否库存不足
	 */
	@Transient
	public boolean getIsLowStock(Integer quantity) {
		if (quantity > getAvailableStock()) {
			return true;
		} else {
			return false;
		}
	}

//	/**
//	 * 获取库存记录
//	 *
//	 * @return 获取库存记录
//	 */
//	@Transient
//	public ProductStock getProductStock(Member seller) {
//		ProductStock stock = null;
//		if (seller!=null) {
//			for (ProductStock productStock : getProductStocks()) {
//				if (productStock.getSeller().equals(seller)) {
//					stock = productStock;
//					break;
//				}
//			}
//		}
//		return stock;
//	}
//
//	/**
//	 * 获取可用库存
//	 *
//	 * @return 可用库存
//	 */
//	@Transient
//	public Integer getAvailableStock(Member seller) {
//		ProductStock stock = null;
//		if (seller!=null) {
//			for (ProductStock productStock : getProductStocks()) {
//				if (productStock.getSeller().equals(seller)) {
//					stock = productStock;
//					break;
//				}
//			}
//		}
//        if (stock==null) {
//			return 0;
//		} else {
//			return stock.getAvailableStock();
//		}
//	}
//
//	/**
//	 * 获取是否缺货
//	 *
//	 * @return 是否缺货
//	 */
//	@Transient
//	public Boolean getIsOutOfStock(Member seller) {
//		ProductStock stock = null;
//		if (seller!=null) {
//			for (ProductStock productStock : getProductStocks()) {
//				if (productStock.getSeller().equals(seller)) {
//					stock = productStock;
//					break;
//				}
//			}
//		}
//		if (stock==null) {
//			return true;
//		} else {
//			return stock.getIsOutOfStock();
//		}
//	}

	@Transient
	public String getSpec() {
		if (getSpec1()!=null && getSpec2()!=null) {
			return getSpec1().concat(","+getSpec2());
		} else
		if (getSpec1()!=null){
			return getSpec1();
		} else {
			return getSpec2();
		}
	}

	@Transient
	public String getThumbnailSmall() {
		if ((getThumbnail()!=null) && (!"".equals(getThumbnail()))) {

			if (getThumbnail().substring(0,11).equals("http://cdnx")) {
				return getThumbnail()+"?x-oss-process=image/resize,w_100,h_100";
			} else
			if (getThumbnail().substring(0,10).equals("http://cdn")) {
				return getThumbnail()+"@100w_100h_1e_1c_100Q";
			} else {
				return getThumbnail();
			}
		} else{
			return null;
		}
	}

}