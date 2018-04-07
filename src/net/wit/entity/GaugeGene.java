package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.sf.json.JSONObject;
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


    public enum ScoreType{
        /**  得分总和  */
        total,
        /**  最大得分 */
        smax
    };

    public enum Rank{
        /**  常规因子  */
        rank1,
        /**  二阶因子 */
        rank2
    };

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

    /** 计算方式 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '计算方式 {total:得分总和,smax:最大得分}'")
    private ScoreType scoreType;

    /** 因子类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null default 0 comment '因子类型 {rank1:常规因子,rank2:二阶因子}'")
    private Rank rank;

    /** 水平 数值为百份号
     *  [{name:"隐性",smin:0,smax:25,expr:‘’}] */

    @JsonIgnore
    @Lob
    @Column(columnDefinition="longtext comment '水平'")
    private String attribute;

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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @JsonIgnore
    public List<String> Attributes() {
        List<String> data = new ArrayList<>();
        if (getAttribute()!=null) {
            net.sf.json.JSONArray attrs = net.sf.json.JSONArray.fromObject(getAttribute());
            for (int i=0;i<attrs.size();i++) {
                JSONObject attr = attrs.getJSONObject(i);
                data.add(attr.getString("sname"));
            }
        }
        return data;
    }
}