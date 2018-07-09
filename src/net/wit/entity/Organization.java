package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity-标签
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_organization")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_organization_sequence")
public class Organization extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /*类型*/
    public enum Type{
        /* 学校 */
        school,
        /* 企业 */
        enterprise
    }

    /*名称*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name ;

    /*类型*/
    @NotNull
    @Column(columnDefinition="int(11) not null comment '类型 {school:学校,enterprise:企业}'")
    private Type type;

    /*删除前处理*/
    @PreRemove
    public void preRemove(){
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
