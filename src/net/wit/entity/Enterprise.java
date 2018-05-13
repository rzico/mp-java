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
import java.util.List;
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

    public enum Status{
        /** 待审核  */
        waiting,
        /** 已审核 */
        success,
        /** 已关闭 */
        failure
    };

    public enum Type{
        /** 代理商 */
        operate,
        /** 合作商 */
        agent,
        /** 推广员 */
        personal,
        /** 商户 */
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

    /** 服务电话 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '服务电话'")
    private String phone;

    /** 联系人 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '联系人'")
    private String linkman;

    /** 类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '类型 {operate:运营商,agent:代理商,personal:个人代理商,shop:入驻商家}'")
    private Type type;

    /** 状态 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '状态 {waiting:待审核,success:已审核,failure:已关闭}'")
    private Status status;

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

    /** 提现手续费   */
    @Min(0)
    @NotNull
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '提现手续费'")
    private BigDecimal transfer;

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit comment '是否删除'")
    @JsonIgnore
    private Boolean deleted;

    /** 业主,入驻商家时不能为空 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    /** 父级企业--代理关系 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Enterprise parent;

    /** ERP主机 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Host host;

    /** 企业标签 */
    @ManyToMany(mappedBy = "enterprise",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompanyLabel> label;

    /** 企业地址 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业地址'")
    private String address;

    /** 企业营业时间 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业营业开始时间'")
    private String startTime;

    /** 企业营业时间 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业营业结束时间'")
    private String endTime;

    /** 企业介绍图1 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业介绍图1'")
    private String image1;

    /** 企业介绍图2 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业介绍图2'")
    private String image2;

    /** 企业介绍图3 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业介绍图3'")
    private String image3;

    /** 企业介绍图4 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业介绍图4'")
    private String image4;

    /** 企业介绍图5 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业介绍图5'")
    private String image5;

    /** 企业介绍图6 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业介绍图6'")
    private String image6;

    /** 企业签名 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '企业签名'")
    private String autograph;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public BigDecimal getTransfer() {
        return transfer;
    }

    public void setTransfer(BigDecimal transfer) {
        this.transfer = transfer;
    }

    public Enterprise getParent() {
        return parent;
    }

    public void setParent(Enterprise parent) {
        this.parent = parent;
    }

    public List<CompanyLabel> getLabel() {
        return label;
    }

    public void setLabel(List<CompanyLabel> label) {
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
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
            return new MapEntity(getMember().getId().toString(), getMember().displayName());
        } else {
            return null;
        }
    }


    public MapEntity getMapParent() {
        if (getParent() != null) {
            return new MapEntity(getParent().getId().toString(), getParent().getName());
        } else {
            return null;
        }
    }

}
