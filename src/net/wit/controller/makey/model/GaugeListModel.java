package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleFavorite;
import net.wit.entity.Gauge;
import net.wit.entity.GaugeRelation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

//文章列表图

public class GaugeListModel extends BaseModel implements Serializable {
    
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
    /** 标签名 */
    private List<TagModel> tags = new ArrayList<TagModel>();

    /** 人数 */
    private Long evaluation;

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

    public Long getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Long evaluation) {
        this.evaluation = evaluation;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public void bind(Gauge gauge) {
        this.id = gauge.getId();
        this.title = gauge.getTitle();
        this.subTitle = gauge.getSubTitle();
        this.thumbnail = gauge.getThumbnail();
        this.evaluation = gauge.getEvaluation();
        this.price = gauge.getPrice();
        this.marketPrice = gauge.getMarketPrice();
        this.tags = TagModel.bindList(gauge.getTags());
    }

    public static List<GaugeListModel> bindList(List<Gauge> gauges) {
        List<GaugeListModel> ms = new ArrayList<GaugeListModel>();
        for (Gauge gauge:gauges) {
            GaugeListModel m = new GaugeListModel();
            m.bind(gauge);
            ms.add(m);
        }
        return ms;
    }


    public static List<GaugeListModel> bindRelation(List<GaugeRelation> relations) {
        List<GaugeListModel> ms = new ArrayList<GaugeListModel>();
        for (GaugeRelation gaugeRelation:relations) {
            GaugeListModel m = new GaugeListModel();
            m.bind(gaugeRelation.getRelation());
            ms.add(m);
        }
        return ms;
    }

}