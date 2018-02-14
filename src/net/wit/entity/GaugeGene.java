package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entity -  分析因子
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_gauge_gene")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_gauge_gene_sequence")
public class GaugeGene extends OrderEntity {

    private static final long serialVersionUID = 24L;

    /** 因子 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '因子'")
    private String name;

    /** 对应题目 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ky_gauge_gene_questions")
    @OrderBy("orders asc")
    @JsonIgnore
    private List<GaugeQuestion> questions = new ArrayList<GaugeQuestion>();

    /** 对应题目 */
    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("orders asc")
    @JsonIgnore
    private List<GaugeResult> results = new ArrayList<GaugeResult>();

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private  Gauge gauge;

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GaugeQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<GaugeQuestion> questions) {
        this.questions = questions;
    }

    public List<GaugeResult> getResults() {
        return results;
    }

    public void setResults(List<GaugeResult> results) {
        this.results = results;
    }
}
