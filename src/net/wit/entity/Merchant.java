package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity -  商户资料
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_merchant")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_merchant_sequence")
public class Merchant extends BaseEntity {

    private static final long serialVersionUID = 53L;

    /** 商户名称 */
    @Column(length = 100,columnDefinition="varchar(100) comment '商户名称'")
    private String scompany;

    /** 商户姓名 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '商户姓名'")
    private String merchantName;

    /** 手机号 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '手机号'")
    private String phone;

    /** 开户银行 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '开户银行'")
    private String bankName;

    /** 银行城市 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '银行城市'")
    private String cardCity;

    /** 银行省份 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '银行省份'")
    private String cardProvince;

    /** 支行名称 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '支行名称'")
    private String branchBankName;

    /** 经营地址 */
    @Column(columnDefinition="varchar(255) comment '经营地址'")
    private String address;

    /** 经营城市 */
    @Column(columnDefinition="varchar(255) comment '经营城市'")
    private String city;

    /** 经营省份 */
    @Column(columnDefinition="varchar(255) comment '经营省份'")
    private String province;

    /** 营业执照 */
    @Column(columnDefinition="varchar(255) comment '营业执照'")
    private String licenseNo;

    /** 行业类型 */
    @Column(columnDefinition="varchar(255) comment '行业类型'")
    private String industryType;

    /** 邮箱 */
    @Column(columnDefinition="varchar(255) comment '邮箱'")
    private String email;

    /** 身份证 */
    @Column(columnDefinition="varchar(255) comment '身份证'")
    private String idCard;

    /** 银行卡号 */
    @Column(columnDefinition="varchar(255) comment '银行卡号'")
    private String cardNo;

    /** 结算费率 */
    @Column(columnDefinition="decimal(21,6) not null default 0.38 comment '结算费率'")
    private BigDecimal brokerage;

    /** 唯一标识 */
    @Column(columnDefinition="varchar(255) comment '唯一标识'")
    private String userId;

    /** 商户编号 */
    @Column(columnDefinition="varchar(255) comment '商户编号'")
    private String merchantNo;

    /** 店主 */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Member owner;

    /** 企业 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="bigint(20) comment '企业'")
    private Enterprise enterprise;

    public String getScompany() {
        return scompany;
    }

    public void setScompany(String scompany) {
        this.scompany = scompany;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCity() {
        return cardCity;
    }

    public void setCardCity(String cardCity) {
        this.cardCity = cardCity;
    }

    public String getCardProvince() {
        return cardProvince;
    }

    public void setCardProvince(String cardProvince) {
        this.cardProvince = cardProvince;
    }

    public String getBranchBankName() {
        return branchBankName;
    }

    public void setBranchBankName(String branchBankName) {
        this.branchBankName = branchBankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
}
