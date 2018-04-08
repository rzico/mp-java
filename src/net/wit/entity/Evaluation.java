package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Entity -  测评表
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_evaluation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_evaluation_sequence")
public class Evaluation extends BaseEntity {

    private static final long serialVersionUID = 24L;

    public enum EvalStatus{
        /**  待付款 */
        waiting,
        /**  已付款 */
        paid,
        /** 已完成 */
        completed,
        /** 已取消 */
        cancelled
    };

    /** 订单号 */
    @Column(nullable = false, updatable = false, unique = true, length = 100,columnDefinition="varchar(100) not null unique comment '订单编号'")
    private String sn;

    /** 缩列图 */
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '缩列图'")
    private String thumbnail;

    /** 状态 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '状态 {waiting:待付款,paid:已付款,completed:已完成,cancelled:已取消}'")
    private EvalStatus evalStatus;

    /** 主标题 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '主标题'")
    private String title;

    /** 学校/企业 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '学校/企业'")
    private String attr1;

    /** 班级/部门 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '班级/部门'")
    private String attr2;

    /** 学号/工号 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '学号/工号'")
    private String attr3;

    /**  姓名 */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '姓名'")
    private String attr4;

    /** 副标题 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '副标题'")
    private String subTitle;

    /** 现价 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '现价'")
    private BigDecimal price;

    /** 奖金 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '奖金'")
    private BigDecimal rebate;

    /** 对应量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Gauge gauge;

    /** 完成数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '完成数'")
    private Long eval;

    /** 总题数 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '总题数'")
    private Long total;

    /** 用时 */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '用时'")
    private Long seconds;

    /** 是否删除 */
    @NotNull
    @Column(columnDefinition="bit comment '是否删除'")
    @JsonIgnore
    private Boolean deleted;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    /** 测评记录*/
    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orders asc")
    @JsonIgnore
    private List<EvalAnswer> evalAnswers = new ArrayList<EvalAnswer>();

    /** 因子得分*/
    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orders asc")
    @JsonIgnore
    private List<EvalGeneScore> evalGeneScores = new ArrayList<EvalGeneScore>();

    /** 测评结果*/
    @Lob
    @Column(columnDefinition="longtext comment '测评结果'")
    @JsonIgnore
    private String result;

    /** 测评变量*/
    @Lob
    @Column(columnDefinition="longtext comment '测评变量'")
    @JsonIgnore
    private String evalvar;

    /** 推广 */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Member promoter;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public EvalStatus getEvalStatus() {
        return evalStatus;
    }

    public void setEvalStatus(EvalStatus evalStatus) {
        this.evalStatus = evalStatus;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public Long getEval() {
        return eval;
    }

    public void setEval(Long eval) {
        this.eval = eval;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<EvalAnswer> getEvalAnswers() {
        return evalAnswers;
    }

    public void setEvalAnswers(List<EvalAnswer> evalAnswers) {
        this.evalAnswers = evalAnswers;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Member getPromoter() {
        return promoter;
    }

    public void setPromoter(Member promoter) {
        this.promoter = promoter;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public List<EvalGeneScore> getEvalGeneScores() {
        return evalGeneScores;
    }

    public void setEvalGeneScores(List<EvalGeneScore> evalGeneScores) {
        this.evalGeneScores = evalGeneScores;
    }

    public String getEvalvar() {
        return evalvar;
    }

    public void setEvalvar(String evalvar) {
        this.evalvar = evalvar;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }
}
