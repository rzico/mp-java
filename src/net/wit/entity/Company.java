package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * Entity - 商家认证
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "xm_company")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_company_sequence")
public class Company extends BaseEntity {
    private static final long serialVersionUID = 127L;

    public static enum Type{
        /** 公司/企业 */
         company,
        /** 个体工商户 */
         individual,
        /** 个人 */
         personal
    };

    /** 全称 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '全称'")
    private String name;

    /** 简称 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '简称'")
    private String shortName;

    /** 营业执照/证件 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '营业执照/证件'")
    private String license;

    /** 门头照片 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '门头照片'")
    private String thedoor;

    /** 店内场景 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '店内场景'")
    private String scene;

    /** 类型 */
    @NotEmpty
    @Column(columnDefinition="int(11) not null comment '类型 {company:公司/企业,individual:个体工商户,personal:个人}'")
    private Type type;

    /** 地区 null 代表没有区域限制 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '地区'")
    private Area area;

    /** 地址 */
    @Column(columnDefinition="varchar(255) comment '地址'")
    private String address;

    /** 行业 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '行业'")
    private CommanyCategory commanyCategory;

    /** 交易佣金 百分比 */
    @Min(0)
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '交易佣金'")
    private BigDecimal brokerage;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getThedoor() {
        return thedoor;
    }

    public void setThedoor(String thedoor) {
        this.thedoor = thedoor;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CommanyCategory getCommanyCategory() {
        return commanyCategory;
    }

    public void setCommanyCategory(CommanyCategory commanyCategory) {
        this.commanyCategory = commanyCategory;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
