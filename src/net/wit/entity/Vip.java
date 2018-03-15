package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity-vip
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_vip")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_vip_sequence")
public class Vip extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /*等级*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment 'vip'")
    private String vip ;

    /*名称*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name ;

    /*金额*/
    @Min(0)
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '金额'")
    private BigDecimal amount;

    /*扩展*/
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '扩展'")
    private String extend;

    /** 业主,入驻商家时不能为空 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member owner;

    /*删除前处理*/
    @PreRemove
    public void preRemove(){
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

}
