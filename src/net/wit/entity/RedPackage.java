package net.wit.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wx_red_package")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_red_package_sequence")
public class RedPackage extends BaseEntity{

    private static final long serialVersionUID = 68L;

    /**
     * 状态
     */
    public enum Status {

        /** 等待支付 */
        wait,

        /** 支付成功 */
        success,

        /** 支付失败 */
        failure,

        /** 红包领取 */
        get,
    }

    /** 文章 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '文章'")
    private Article article;


    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '会员'")
    private Member member;


    /** 金额 */
    @Column(columnDefinition="decimal(21,6) not null comment '金额'")
    private BigDecimal amount;


    /** 付款单 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false,columnDefinition="bigint(20) not null comment '付款单'")
    private Payment payment;

    /** 交易状态 */
    @Column(columnDefinition="int(11) not null comment '交易状态 {wait:等待支付,success:支付成功,failure:支付失败}'")
    private Status status;


    /** IP */
    @Column(nullable = false, updatable = false,columnDefinition="varchar(255) comment 'IP'")
    private String ip;


    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
