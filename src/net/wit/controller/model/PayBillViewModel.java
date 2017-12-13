package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.PayBill;
import net.wit.entity.Payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayBillViewModel extends BaseModel implements Serializable {

    private Long id;
    /** 交易金额 */
    private BigDecimal amount;
    /** 优惠折扣 */
    private BigDecimal couponDiscount;
    /**  摘要 */
    private String memo;
    /**  LOGO */
    private String logo;
    /**  昵称 */
    private String nickName;
    /**  时间 */
    private Date createDate;
    /**  状态 */
    private PayBill.Status status;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PayBill.Status getStatus() {
        return status;
    }

    public void setStatus(PayBill.Status status) {
        this.status = status;
    }

    public void bind(PayBill payBill) {
        this.id = payBill.getId();
        this.amount = payBill.getAmount().subtract(payBill.getCouponDiscount());
        this.couponDiscount = payBill.getCouponDiscount();
        String s = "";
        if (payBill.getType().equals(PayBill.Type.cashierRefund)) {
            this.memo = "退款";
            if (payBill.getRefunds().getPaymentMethod()!=null) {
                s = ","+payBill.getRefunds().getPaymentMethod();
            }
        } else
        if (payBill.getType().equals(PayBill.Type.cardRefund)){
            this.memo = "退款(会员卡)";
            if (payBill.getRefunds().getPaymentMethod()!=null) {
                s = ","+payBill.getRefunds().getPaymentMethod();
            }
        } else
        if (payBill.getType().equals(PayBill.Type.card)) {
            this.memo = "消费(会员卡)";
            if (payBill.getPayment().getPaymentMethod()!=null) {
                s = ","+payBill.getPayment().getPaymentMethod();
            }
        } else {
            this.memo = "消费";
            if (payBill.getPayment().getPaymentMethod()!=null) {
                s = ","+payBill.getPayment().getPaymentMethod();
            }
        }
        this.memo = this.memo + s;
        this.logo = payBill.getMember().getLogo();
        this.createDate = payBill.getCreateDate();
        this.status = payBill.getStatus();
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