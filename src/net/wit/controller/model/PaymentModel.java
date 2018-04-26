package net.wit.controller.model;

import net.wit.Setting;
import net.wit.entity.Payment;
import net.wit.util.SettingUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//文章编辑模板

public class PaymentModel extends BaseModel implements Serializable {

    /** 收款单号 */
    private String sn;
    /** 收款方 */
    private String nickName;
    /** 收款方头像 */
    private String logo;
    /** 摘要 */
    private String memo;
    /** 付款方式 */
    private String paymentPluginId;
    /** 金额 */
    private BigDecimal amount;
    /** 交易时间 */
    private Date createDate;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPaymentPluginId() {
        return paymentPluginId;
    }

    public void setPaymentPluginId(String paymentPluginId) {
        this.paymentPluginId = paymentPluginId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void bind(Payment payment) {
        this.sn = payment.getSn();
        this.amount = payment.getAmount();
        this.createDate = payment.getCreateDate();
        this.memo = payment.getMemo();
        if (payment.getPayee()!=null) {
            this.logo = payment.getPayee().getLogo();
            this.nickName = payment.getPayee().displayName();
        } else {
            Setting setting = SettingUtils.get();
            this.nickName = setting.getSiteName();
            this.logo = setting.getLogo();
        }
        this.paymentPluginId = payment.getPaymentPluginId();
    }
}