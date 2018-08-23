package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Random;

@Embeddable
public class ArticleRedPackage {

    /**  红包类型 */
    public static enum  RedPackageType{
        AVE,//平均分配红包
        RAN//随机分配红包
    }

    /**  红包类型 */
    @Column(columnDefinition="int(11) comment '红包类型'")
    private RedPackageType redPackageType;
    /** 红包数量 */
    @Min(0)
    @Column(columnDefinition="bigint(20) comment '红包数量'")
    private Long remainSize;


    /** 红包总金额 */
    @Min(0)
    @Column(columnDefinition="decimal(21,6) comment '金额'")
    private BigDecimal amount;


    @Column(columnDefinition="bit comment '是否支付'")
    @JsonIgnore
    private Boolean isPay;

    public RedPackageType getRedPackageType() {
        return redPackageType;
    }

    public void setRedPackageType(RedPackageType redPackageType) {
        this.redPackageType = redPackageType;
    }

    public Long getRemainSize() {
        return remainSize;
    }

    public void setRemainSize(Long remainSize) {
        this.remainSize = remainSize;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean pay) {
        isPay = pay;
    }
}
