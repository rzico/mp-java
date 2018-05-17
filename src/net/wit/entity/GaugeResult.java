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

    public enum Type{
        /**   富文本 */
        html,
        /**   纯文本 */
        text,
        /**   图表 */
        echart,
        /**   图片 */
        image

    };

    public enum ChartType{
        /**  折线图 */
        line
    };

    /** 类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null default 0 comment '结果类型 {html:富文本,text:纯文本,echart:图表}'")
    private Type type;

    /** 图表类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null default 0 comment '图表类型 {html:富文本,text:纯文本,echart:图表}'")
    private ChartType chartType;

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private  Gauge gauge;

    /** 标题  */
    @Column(columnDefinition="varchar(255) not null comment '标题'")
    private String title;

    /** 水平设置
     *  [{gene:"因子",attribute:"显性"}] */
    @Lob
    @Column(columnDefinition="longtext comment '水平设置'")
    private String attribute;

    /** 因子关系
     *  [] */
    @Lob
    @Column(columnDefinition="longtext comment '因子关系'")
    private String scompare;

    /** 结果模板 */
    @Lob
    @Column(columnDefinition="longtext comment '结果模板'")
    @JsonIgnore
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

}
