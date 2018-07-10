package net.wit.controller.model;

import net.wit.entity.Article;
import net.wit.entity.ArticleRedPackage;

import java.math.BigDecimal;

public class ArticleRedPackageModel {


    /**  红包类型 */
    private ArticleRedPackage.RedPackageType redPackageType;
    /** 红包数量 */
    private Long remainSize;
    /** 红包总金额 */
    private BigDecimal remainMoney;

    public ArticleRedPackage.RedPackageType getRedPackageType() {
        return redPackageType;
    }

    public void setRedPackageType(ArticleRedPackage.RedPackageType redPackageType) {
        this.redPackageType = redPackageType;
    }

    public Long getRemainSize() {
        return remainSize;
    }

    public void setRemainSize(Long remainSize) {
        this.remainSize = remainSize;
    }

    public BigDecimal getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }

    public void bind(Article article){
        ArticleRedPackage articleRedPackage = article.getArticleRedPackage();
        if(articleRedPackage != null){
            this.redPackageType = articleRedPackage.getRedPackageType();
            this.remainSize = articleRedPackage.getRemainSize();
            this.remainMoney = articleRedPackage.getAmount();
        }
    }
}
