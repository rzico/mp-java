package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entity -  测评表资料
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_evaluation_attribute")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_evaluation_attribute_sequence")
public class EvaluationAttribute extends BaseEntity {

    private static final long serialVersionUID = 24L;

    public enum Type{
        /**  用户信息 */
        user,
        /**  修订资料 */
        revision
    };

    /** 类型 */
    @NotNull
    @Column(columnDefinition="int(11) not null comment '常模类型 {user:用户资料,revision:修订资料}'")
    private Type type;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    /** 测评 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Evaluation evaluation;

    /** 属性 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private MemberAttribute memberAttribute;

    /** 名称 */
    @NotEmpty
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name;

    /** 内容 */
    @Column(columnDefinition="varchar(255) not null comment '内容'")
    private String value;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public MemberAttribute getMemberAttribute() {
        return memberAttribute;
    }

    public void setMemberAttribute(MemberAttribute memberAttribute) {
        this.memberAttribute = memberAttribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
