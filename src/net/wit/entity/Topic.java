package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 专栏
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_topic")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_topic_sequence")
public class Topic extends BaseEntity {

    private static final long serialVersionUID = 127L;

    public static enum Type{
        /** 企业 */
         company,
        /** 个体工商户 */
         individual,
        /** 个人 */
         personal,
        /** 学生 */
         student

    };

    public static enum Status{
        /** 等待 */
         waiting,
        /** 通过 */
         success,
        /** 驳回 */
         failure
    };

    /** 全称 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '全称'")
    @NotNull
    private String name;

    /** 简称 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '简称'")
    @NotNull
    private String shortName;

    /** 营业执照/证件号  个人时用证件号 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '上传证件'")
    private String license;

    /** 营业执照号/证件号 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '证件号'")
    private String licenseCode;

    /** 上传门头 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '上传门头'")
    private String thedoor;

    /** 经营场所 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '经营场所'")
    private String scene;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '会员'")
    @JsonIgnore
    @NotNull
    private Member member;

    /** 状态 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '状态 {waiting:等待,success:通过,failure:驳回}'")
    private Status status;

    /** 到期日 */
    @NotNull
    @Column(columnDefinition="datetime not null comment '到期日'")
    private Date expire;

    /** 类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '类型 {company:公司/企业,individual:个体工商户,personal:个人,student:学生}'")
    private Type type;

    /** 地区 null 代表没有区域限制 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '地区'")
    @JsonIgnore
    private Area area;

    /** 地址 */
    @Column(columnDefinition="varchar(255) comment '地址'")
    private String address;

    /** 行业 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '行业'")
    @JsonIgnore
    private Category category;

    /** 交易佣金 百分比 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '交易佣金'")
    private BigDecimal brokerage;

    /** 模板 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) not null comment '模板'")
    @JsonIgnore
    private Template template;

    /** 模板标签*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "wx_topic_tag")
    @OrderBy("orders asc")
    @JsonIgnore
    private List<Tag> tags = new ArrayList<Tag>();

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

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public MapEntity getMapTemplate() {
        if (getTemplate() != null) {
            return new MapEntity(getTemplate().getId().toString(), getTemplate().getName());
        } else {
            return null;
        }
    }

    public MapEntity getMapMember() {
        if (getMember() != null) {
            return new MapEntity(getMember().getId().toString(), getMember().getNickName()+"("+getMember().getName()+")");
        } else {
            return null;
        }
    }

    public MapEntity getMapArea() {
        if (getArea() != null) {
            return new MapEntity(getArea().getId().toString(), getArea().getName());
        } else {
            return null;
        }
    }


    public MapEntity getMapCategory() {
        if (getCategory() != null) {
            return new MapEntity(getCategory().getId().toString(), getCategory().getName());
        } else {
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

}
