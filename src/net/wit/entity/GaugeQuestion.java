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
 * Entity -  量表题目
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_gauge_question")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_gauge_question_sequence")
public class GaugeQuestion extends OrderEntity {

    private static final long serialVersionUID = 24L;

    public enum Type{
        /**  文字 */
        text,
        /**  图片 */
        image
    };

    /** 题型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '题型 {text:文字,image:图片}'")
    private Type type;

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
    private  Gauge gauge;

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


}
