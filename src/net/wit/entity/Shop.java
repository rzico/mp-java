package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 实体店
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_shop")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_shop_sequence")
public class Shop extends BaseEntity {

    private static final long serialVersionUID = 53L;

    /** 收款码 */
    @Column(length = 100,columnDefinition="varchar(100) comment '收款码'")
    private String code;

    /** 名称 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name;

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

    /** 地区 null 代表没有区域限制 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '地区'")
    private Area area;

    /** 地址 */
    @Column(columnDefinition="varchar(255) comment '地址'")
    private String address;

    /** 联系人 */
    @Column(columnDefinition="varchar(255) comment '联系人'")
    private String linkman;

    /** 联系电话 */
    @Column(columnDefinition="varchar(255) comment '联系电话'")
    private String telephone;

    /** 店主 */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Member owner;

    /** 定位 */
    @Embedded
    @JsonIgnore
    private Location location;

    /** 企业 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '企业'")
    private Enterprise enterprise;

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit comment '是否删除'")
    @JsonIgnore
    private Boolean deleted;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public MapEntity getMapArea() {
        if (getArea() != null) {
            return new MapEntity(getArea().getId().toString(), getArea().getName());
        } else {
            return null;
        }
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
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

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
