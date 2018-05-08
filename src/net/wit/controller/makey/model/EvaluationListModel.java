package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Evaluation;
import net.wit.entity.Gauge;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章列表图

public class EvaluationListModel extends BaseModel implements Serializable {
    
    private Long id;
    /**  头像 */
    private String logo;
    /** 昵称 */
    private String nickName;
    /** 标题 */
    private String title;
    /**  子标题 */
    private String subTitle;
    /** 缩列图 */
    private String thumbnail;
    /** 现价 */
    private BigDecimal price;
    /** 奖金 */
    private BigDecimal rebate;
    /** 时间 */
    private Date createDate;
    /** 订单 */
    private String sn;
    /** 量表 */
    private Long gaugeId;
    /** 完成 */
    private Long eval;
    /** 题目 */
    private Long total;
    /** 秒数 */
    private Long seconds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getEval() {
        return eval;
    }

    public void setEval(Long eval) {
        this.eval = eval;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Long getGaugeId() {
        return gaugeId;
    }

    public void setGaugeId(Long gaugeId) {
        this.gaugeId = gaugeId;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
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

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public void bind(Evaluation evaluation) {
        this.id = evaluation.getId();
        this.logo = evaluation.getMember().getLogo();
        this.nickName = evaluation.getMember().displayName();
        this.title = evaluation.getTitle();
        this.subTitle = evaluation.getSubTitle();
        this.thumbnail = evaluation.getThumbnail();
        this.eval = evaluation.getEval();
        this.price = evaluation.getPrice();
        this.rebate = evaluation.getRebate();
        this.total = evaluation.getTotal();

        this.gaugeId = evaluation.getGauge().getId();

        this.sn = evaluation.getSn();
        this.createDate = evaluation.getCreateDate();
        this.seconds = evaluation.getSeconds();
    }

    public static List<EvaluationListModel> bindList(List<Evaluation> evaluations) {
        List<EvaluationListModel> ms = new ArrayList<EvaluationListModel>();
        for (Evaluation evaluation:evaluations) {
            EvaluationListModel m = new EvaluationListModel();
            m.bind(evaluation);
            ms.add(m);
        }
        return ms;
    }

}