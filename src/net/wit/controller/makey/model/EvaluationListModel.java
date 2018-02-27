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
    /** 标题 */
    private String title;
    /**  子标题 */
    private String subTitle;
    /** 缩列图 */
    private String thumbnail;
    /** 现价 */
    private BigDecimal price;
    /** 时间 */
    private Date createDate;
    /** 订单 */
    private String sn;

    /** 完成 */
    private Long eval;
    /** 题目 */
    private Long total;

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

    public void bind(Evaluation evaluation) {
        this.id = evaluation.getId();
        this.title = evaluation.getTitle();
        this.subTitle = evaluation.getSubTitle();
        this.thumbnail = evaluation.getThumbnail();
        this.eval = evaluation.getEval();
        this.price = evaluation.getPrice();
        this.total = evaluation.getTotal();

        this.sn = evaluation.getSn();
        this.createDate = evaluation.getCreateDate();
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