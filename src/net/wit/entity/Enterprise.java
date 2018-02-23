package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Entity - 账单记录
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "wx_enterprise")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_enterprise_sequence")
public class Enterprise extends BaseEntity {

    private static final long serialVersionUID = 24L;

    public enum Type{
        /** 运营商 */
        operate,
        /** 代理商 */
        agent,
        /** 个人 */
        personal,
        /** 商家 */
        shop
    };

    /** 企业 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '企业名称'")
    private String name;

    /** logo */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment 'logo'")
    private String logo;

    /** 类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '类型 {operate:运营商,agent:代理商,personal:个人代理商,shop:入驻商家}'")
    private Type type;

    /** 地区 null 代表没有区域限制 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '地区'")
    @JsonIgnore
    private Area area;

    /** 授信额度 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '授信额度'")
    private BigDecimal creditLine;

    /** 结算佣金 10% */
    @Min(0)
    @NotNull
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '结算佣金'")
    private BigDecimal brokerage;

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit comment '是否删除'")
    @JsonIgnore
    private Boolean deleted;

    /** 业主,入驻商家时不能为空 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

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

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BigDecimal getCreditLine() {
        return creditLine;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public MapEntity getMapArea() {
        if (getArea() != null) {
            return new MapEntity(getArea().getId().toString(), getArea().getName());
        } else {
            return null;
        }
    }

    @JsonIgnore
    public BigDecimal calcFee(BigDecimal amount) {
        BigDecimal rate = this.brokerage.multiply(new BigDecimal("0.01"));
        return amount.multiply(rate).setScale(4,BigDecimal.ROUND_HALF_DOWN);
    }

    public MapEntity getMapMember() {
        if (getMember() != null) {
            return new MapEntity(getMember().getId().toString(), getMember().getName());
        } else {
            return null;
        }
    }

}
