package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entity -  咨询量表
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_gauge")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_gauge_sequence")
public class Gauge extends BaseEntity {

    private static final long serialVersionUID = 24L;

    public enum UserType{
        /**  普通用户 */
        general,
        /**  企业用户 */
        enterprise,
        /**  学校用户 */
        school
    };

    public enum Type{
        /**  单常模 */
        single,
        /**  多常模 */
        complex
    };

    /** 量表分类 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GaugeCategory gaugeCategory;

    /** 用户类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '用户类型 {general:普通用户,enterprise:企业用户,school:学校用户}'")
    private UserType  userType;

    /** 常模类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '常模类型 {single:单常模,complex:多常模}'")
    private Type type;

    /** 主标题 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '主标题'")
    private String title;

    /** 副标题 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '副标题'")
    private String subTitle;

    /** 缩列图 */
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '缩列图'")
    private String thumbnail;

    /** 测评须知 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '测评须知'")
    private String notice;

    /** 用户资料*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ky_gauge_user_attribute")
    @OrderBy("orders asc")
    @JsonIgnore
    private List<MemberAttribute> userAttributes = new ArrayList<MemberAttribute>();

    /** 修订收集*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ky_gauge_revision_attribute")
    @OrderBy("orders asc")
    @JsonIgnore
    private List<MemberAttribute> revisionAttributes = new ArrayList<MemberAttribute>();

    /** 亮点 */
    @ElementCollection
    @CollectionTable(name = "ky_gauge_spot")
    private List<String> spots = new ArrayList<String>();

    /** 常模修订说明 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '常模修订说明'")
    private String revisionNote;

    /** 测评人数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '测评人数'")
    private Long evaluation;

    /** 原价 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '原价'")
    private BigDecimal marketPrice;

    /** 现价 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '现价'")
    private BigDecimal price;

    /** 测评简介 */
    @Lob
    @Column(columnDefinition="longtext comment '测评简介'")
    @JsonIgnore
    private String content;

    /** 推广佣金 % */
    @Min(0)
    @NotNull
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '推广佣金'")
    private BigDecimal brokerage;

    /** 分销佣金 % */
    @Min(0)
    @NotNull
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '分销佣金'")
    private BigDecimal distribution;

    /** 题库*/
    @OneToMany(mappedBy = "gauge",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orders asc")
    @JsonIgnore
    private List<GaugeQuestion> gaugeQuestions = new ArrayList<GaugeQuestion>();

    /** 所属商品 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;

    /** 量表标签*/
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ky_gauge_tag")
    @OrderBy("orders asc")
    @JsonIgnore
    private List<Tag> tags = new ArrayList<Tag>();

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit comment '是否删除'")
    @JsonIgnore
    private Boolean deleted;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


    public String getRevisionNote() {
        return revisionNote;
    }

    public void setRevisionNote(String revisionNote) {
        this.revisionNote = revisionNote;
    }

    public Long getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Long evaluation) {
        this.evaluation = evaluation;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public BigDecimal getDistribution() {
        return distribution;
    }

    public void setDistribution(BigDecimal distribution) {
        this.distribution = distribution;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<MemberAttribute> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(List<MemberAttribute> userAttributes) {
        this.userAttributes = userAttributes;
    }

    public List<MemberAttribute> getRevisionAttributes() {
        return revisionAttributes;
    }

    public void setRevisionAttributes(List<MemberAttribute> revisionAttributes) {
        this.revisionAttributes = revisionAttributes;
    }

    public List<GaugeQuestion> getGaugeQuestions() {
        return gaugeQuestions;
    }

    public void setGaugeQuestions(List<GaugeQuestion> gaugeQuestions) {
        this.gaugeQuestions = gaugeQuestions;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public GaugeCategory getGaugeCategory() {
        return gaugeCategory;
    }

    public void setGaugeCategory(GaugeCategory gaugeCategory) {
        this.gaugeCategory = gaugeCategory;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<String> getSpots() {
        return spots;
    }

    public void setSpots(List<String> spots) {
        this.spots = spots;
    }
}
