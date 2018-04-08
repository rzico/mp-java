package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity-全球公排
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_order_ranking")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_order_ranking_sequence")
public class OrderRanking extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /*品名*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name ;

    /*规格*/
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '规格'")
    private String spec ;

    /*奖金*/
    @Min(0)
    @Column(columnDefinition="decimal(21,6) not null default 0 comment '奖金'")
    private BigDecimal amount;

    /*积分*/
    @Min(0)
    @Column(columnDefinition="bigint(20) default 0 comment '积分'")
    private Long point;

    /** 商品 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Goods goods;

    /** 卖家 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member owner;

    /** 买家 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


}
