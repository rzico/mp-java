package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * Entity -  因子得分
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_eval_gene_score")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_eval_gene_score_sequence")
public class EvalGeneScore extends OrderEntity {

    private static final long serialVersionUID = 24L;

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Gauge gauge;

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GaugeGene gaugeGene;

    /** 因子 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '因子'")
    private String name;

    /** 得分 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '现价'")
    private BigDecimal score;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    /**  测评表 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Evaluation evaluation;

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public GaugeGene getGaugeGene() {
        return gaugeGene;
    }

    public void setGaugeGene(GaugeGene gaugeGene) {
        this.gaugeGene = gaugeGene;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
