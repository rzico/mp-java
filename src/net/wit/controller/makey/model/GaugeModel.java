package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Gauge;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
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

    /** 用户类型 */
    private Gauge.UserType userType;

    /** 常模类型 */
    private Gauge.Type type;

    /** 测评须知 */
    private String notice;

    /** 常模修订说明 */
    private String revisionNote;

    /** 测评人数 */
    private Long evaluation;

    /** 测评简介 */
    private String content;

    /** 测评亮点 */
    private List<String> spots;

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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getRevisionNote() {
        return revisionNote;
    }

    public void setRevisionNote(String revisionNote) {
        this.revisionNote = revisionNote;
    }

    public Long getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Long evaluation) {
        this.evaluation = evaluation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getSpots() {
        return spots;
    }

    public void setSpots(List<String> spots) {
        this.spots = spots;
    }

    public Gauge.UserType getUserType() {
        return userType;
    }

    public void setUserType(Gauge.UserType userType) {
        this.userType = userType;
    }

    public Gauge.Type getType() {
        return type;
    }

    public void setType(Gauge.Type type) {
        this.type = type;
    }

    public void bind(Gauge gauge) {
        this.id = gauge.getId();
        this.title = gauge.getTitle();
        this.type = gauge.getType();
        this.userType = gauge.getUserType();
        this.subTitle = gauge.getSubTitle();
        this.thumbnail = gauge.getThumbnail();
        this.evaluation = gauge.getEvaluation();
        this.price = gauge.getPrice();
        this.marketPrice = gauge.getMarketPrice();
        this.content = gauge.getContent();
        this.revisionNote = gauge.getRevisionNote();
        this.notice = gauge.getNotice();
        this.spots = new ArrayList<>();
        this.spots.addAll(gauge.getSpots());
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