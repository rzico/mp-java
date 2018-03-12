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

    /** 水平设置
     *  [{gene:"因子",attribute:"显性"}] */
    @Lob
    @Column(columnDefinition="longtext comment '水平设置'")
    private String attribute;

    /** 因子关系
     *  [{gene:"因子"}] */
    @Lob
    @Column(columnDefinition="longtext comment '因子关系'")
    private String scompare;

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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getScompare() {
        return scompare;
    }

    public void setScompare(String scompare) {
        this.scompare = scompare;
    }
}
