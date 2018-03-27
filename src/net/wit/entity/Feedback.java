package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Eric on 2018/3/26.
 */
@Entity
@Table(name = "wx_feedback")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_feedback_sequence")
public class Feedback extends BaseEntity {

    /** 是否解决 */
    @NotNull
    @Column(columnDefinition="bit not null default 0 comment '是否解决'")
    @JsonIgnore
    private Boolean isSolve;

    /** 问题内容 */
    @Lob
    @Column(columnDefinition="longtext comment '问题内容'")
    @JsonIgnore
    private String content;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    /** 回复内容 */
    @Lob
    @Column(columnDefinition="longtext comment '回复内容'")
    @JsonIgnore
    private String recontent;

    /** 问题反馈图1 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '问题反馈图1'")
    @JsonIgnore
    private String problemPictrue1;

    /** 问题反馈图2 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '问题反馈图2'")
    @JsonIgnore
    private String problemPictrue2;

    /** 问题反馈图3 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '问题反馈图3'")
    @JsonIgnore
    private String problemPictrue3;

    /** 问题反馈图4 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '问题反馈图4'")
    @JsonIgnore
    private String problemPictrue4;

    /** 问题反馈图5 */
    @Length(max = 255)
    @Column(columnDefinition="varchar(255) comment '问题反馈图5'")
    @JsonIgnore
    private String problemPictrue5;

    public Boolean getSolve() {
        return isSolve;
    }

    public void setSolve(Boolean solve) {
        isSolve = solve;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getProblemPictrue1() {
        return problemPictrue1;
    }

    public void setProblemPictrue1(String problemPictrue1) {
        this.problemPictrue1 = problemPictrue1;
    }

    public String getProblemPictrue2() {
        return problemPictrue2;
    }

    public void setProblemPictrue2(String problemPictrue2) {
        this.problemPictrue2 = problemPictrue2;
    }

    public String getProblemPictrue3() {
        return problemPictrue3;
    }

    public void setProblemPictrue3(String problemPictrue3) {
        this.problemPictrue3 = problemPictrue3;
    }

    public String getProblemPictrue4() {
        return problemPictrue4;
    }

    public void setProblemPictrue4(String problemPictrue4) {
        this.problemPictrue4 = problemPictrue4;
    }

    public String getProblemPictrue5() {
        return problemPictrue5;
    }

    public void setProblemPictrue5(String problemPictrue5) {
        this.problemPictrue5 = problemPictrue5;
    }

    public String getRecontent() {
        return recontent;
    }

    public void setRecontent(String recontent) {
        this.recontent = recontent;
    }

    public MapEntity getMapMember() {
        if (getMember() != null) {
            return new MapEntity(getMember().getId().toString(), getMember().getNickName()+(getMember().getName()==null?"":"("+getMember().getName()+")") );
        } else {
            return null;
        }
    }
}
