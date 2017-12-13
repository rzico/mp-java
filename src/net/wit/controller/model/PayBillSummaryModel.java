package net.wit.controller.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.wit.entity.ArticleVote;
import net.wit.entity.PayBill;
import net.wit.entity.summary.PayBillShopSummary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PayBillSummaryModel extends BaseModel implements Serializable {
    private Long shopId;
    private String name;
    private String logo;
    //消费类型
    private PayBill.Type type;
    //结算方式
    private String method;
    //消费金额
    private BigDecimal amount;
    //手续费
    private BigDecimal fee;
    //入账金额
    private BigDecimal account;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public PayBill.Type getType() {
        return type;
    }

    public void setType(PayBill.Type type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public void bind(PayBillShopSummary summary) {
        this.shopId = summary.getShop().getId();
        this.name = summary.getShop().getName();
        this.amount = summary.getAmount().subtract(summary.getCouponDiscount());
        this.fee = summary.getFee();
        this.logo = summary.getShop().getThedoor();
        this.type = summary.getType();
        this.method = summary.getPaymentPluginId();
        if ("bankPayPlugin".equals(this.method) || "cardPayPlugin".equals(this.method) || "cashPayPlugin".equals(this.method)) {
            this.account = BigDecimal.ZERO;
        } else {
            this.account = this.amount.subtract(this.fee);
        }
    }


    public static List<PayBillSummaryModel> bindList(List<PayBillShopSummary> sums) {
        List<PayBillSummaryModel> ms = new ArrayList<PayBillSummaryModel>();
        for (PayBillShopSummary payBillShopSummary:sums) {
            PayBillSummaryModel m = new PayBillSummaryModel();
            m.bind(payBillShopSummary);
            ms.add(m);
        }
        return ms;
    }

}