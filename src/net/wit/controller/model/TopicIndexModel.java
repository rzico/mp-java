package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.entity.TopicConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TopicIndexModel implements Serializable {

    private Long id;
    /** 名称 */
    private String name;
    /** 状态 */
    private Topic.Status status;
    /** 年费 */
    private BigDecimal fee;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 是否业主 */
    private Boolean isOwner;
    /** 待就业 */
    private Boolean noJob;
    /** 开通会员卡 */
    private Boolean useCard;
    /** 开通优惠券 */
    private Boolean useCoupon;
    /** 开通收银台 */
    private Boolean useCashier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Topic.Status getStatus() {
        return status;
    }

    public void setStatus(Topic.Status status) {
        this.status = status;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Boolean getUseCard() {
        return useCard;
    }

    public void setUseCard(Boolean useCard) {
        this.useCard = useCard;
    }

    public Boolean getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(Boolean useCoupon) {
        this.useCoupon = useCoupon;
    }

    public Boolean getUseCashier() {
        return useCashier;
    }

    public void setUseCashier(Boolean useCashier) {
        this.useCashier = useCashier;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean owner) {
        isOwner = owner;
    }

    public Boolean getNoJob() {
        return noJob;
    }

    public void setNoJob(Boolean noJob) {
        this.noJob = noJob;
    }

    public void bind(Topic topic) {
        this.id = topic.getId();
        this.autograph = topic.getMember().getAutograph();
        this.name = topic.getName();
        this.logo = topic.getLogo();
        this.fee = topic.getFee();
        this.status = topic.getStatus();
        TopicConfig config = topic.getConfig();
        if (config!=null) {
            this.useCard = config.getUseCard();
            this.useCoupon = config.getUseCoupon();
            this.useCashier = config.getUseCashier();
        } else {
            this.useCard = false;
            this.useCoupon = false;
            this.useCashier = false;
        }

        this.noJob = false;
        this.isOwner = false;
    }
}
