package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;

//文章列表图

public class GaugeGeneAttributeModel extends BaseModel implements Serializable {
    /** 水平 */
    private String sname;
    /**  最小值 */
    private BigDecimal smin;
    /**  最大值 */
    private BigDecimal smax;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public BigDecimal getSmin() {
        return smin;
    }

    public void setSmin(BigDecimal smin) {
        this.smin = smin;
    }

    public BigDecimal getSmax() {
        return smax;
    }

    public void setSmax(BigDecimal smax) {
        this.smax = smax;
    }
}