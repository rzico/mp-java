package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity-vip
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_xdict")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_xdict_sequence")
public class Xdict extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /*名称*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name ;

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
}
