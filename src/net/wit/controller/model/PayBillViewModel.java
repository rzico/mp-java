package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.PayBill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayBillViewModel implements Serializable {

    private Long id;
    /** 交易金额 */
    private BigDecimal amount;
    /** 优惠折扣 */
    private BigDecimal couponDiscount;
    /**  摘要 */
    private String memo;
    /**  时间 */
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void bind(PayBill payBill) {
        this.id = payBill.getId();
        this.amount = payBill.getAmount().subtract(payBill.getCouponDiscount());
        this.couponDiscount = payBill.getCouponDiscount();
        if (payBill.getCardDiscount().compareTo(BigDecimal.ZERO)==0) {
            this.memo = "会员卡支付";
        } else {
            if (payBill.getMethod().equals(PayBill.Method.online)) {
                this.memo = payBill.getPayment().getPaymentMethod();
            } else {
                this.memo = "现金收银";
            }
        }
    }


    public static List<PayBillViewModel> bindList(List<PayBill> payBills) {
        List<PayBillViewModel> ms = new ArrayList<PayBillViewModel>();
        for (PayBill payBill:payBills) {
            PayBillViewModel m = new PayBillViewModel();
            m.bind(payBill);
            ms.add(m);
        }
        return ms;
    }

}