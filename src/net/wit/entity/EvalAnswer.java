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
 * Entity -  测评表
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_eval_answer")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_eval_answer_sequence")
public class EvalAnswer extends OrderEntity {

    private static final long serialVersionUID = 24L;

    /** 题型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '题型 {text:文字,image:图片}'")
    private GaugeQuestion.Type type;

    /** 题目 */
    @Length(max = 200)
    @NotNull
    @Column(columnDefinition="varchar(255) not null comment '题目'")
    private String title;

    /** 选项
     *  {id:序号,name:"你几岁了",image:"图片地址",score:分数} */
    @Lob
    @Column(columnDefinition="longtext comment '选项'")
    @JsonIgnore
    private String content;

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Gauge gauge;

    /** 答案id */
    @Min(0)
    @NotNull
    @Column(columnDefinition="bigint(20) not null default 0 comment '答案'")
    private Long answer;

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

    public GaugeQuestion.Type getType() {
        return type;
    }

    public void setType(GaugeQuestion.Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public Long getAnswer() {
        return answer;
    }

    public void setAnswer(Long answer) {
        this.answer = answer;
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
