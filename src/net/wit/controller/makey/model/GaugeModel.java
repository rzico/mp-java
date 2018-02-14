package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Gauge;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class GaugeModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 标题 */
    private String title;
    /**  子标题 */
    private String subTitle;
    /** 缩列图 */
    private String thumbnail;
    /** 原价 */
    private BigDecimal marketPrice;
    /** 现价 */
    private BigDecimal price;
    
    /** 人数 */
    private Long eval;

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

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
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

    public void bind(Gauge gauge) {
        this.id = gauge.getId();
        this.title = gauge.getTitle();
        this.subTitle = gauge.getSubTitle();
        this.thumbnail = gauge.getContent();
        this.eval = gauge.getEvaluation();
        this.price = gauge.getPrice();
        this.marketPrice = gauge.getMarketPrice();
    }

    public static List<GaugeModel> bindList(List<Gauge> gauges) {
        List<GaugeModel> ms = new ArrayList<GaugeModel>();
        for (Gauge gauge:gauges) {
            GaugeModel m = new GaugeModel();
            m.bind(gauge);
            ms.add(m);
        }
        return ms;
    }

}