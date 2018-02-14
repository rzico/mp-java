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
 * Entity -  分析结果
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_gauge_result")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_gauge_result_sequence")
public class GaugeResult extends OrderEntity {

    private static final long serialVersionUID = 24L;

    /**  最小得分 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '最小得分'")
    private BigDecimal minscore;

    /**  最大得分 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(precision = 21, scale = 6,columnDefinition="decimal(21,6) not null default 0 comment '最大得分'")
    private BigDecimal maxscore;

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private  Gauge gauge;

    /** 对应因子 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private  GaugeGene gaugeGene;

    /** 结果模板 */
    @Lob
    @Column(columnDefinition="longtext comment '结果模板'")
    @JsonIgnore
    private String content;

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public BigDecimal getMinscore() {
        return minscore;
    }

    public void setMinscore(BigDecimal minscore) {
        this.minscore = minscore;
    }

    public BigDecimal getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(BigDecimal maxscore) {
        this.maxscore = maxscore;
    }

    public GaugeGene getGaugeGene() {
        return gaugeGene;
    }

    public void setGaugeGene(GaugeGene gaugeGene) {
        this.gaugeGene = gaugeGene;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
