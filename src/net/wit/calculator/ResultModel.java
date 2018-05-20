package net.wit.calculator;

import net.wit.controller.model.BaseModel;
import net.wit.entity.GaugeResult;

import java.io.Serializable;

//文章列表图

public class ResultModel extends BaseModel implements Serializable {

    private String title;
    private GaugeResult.Type type;
    private GaugeResult.ChartType chartType;


    /**  结果 */
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public GaugeResult.Type getType() {
        return type;
    }

    public void setType(GaugeResult.Type type) {
        this.type = type;
    }

    public GaugeResult.ChartType getChartType() {
        return chartType;
    }

    public void setChartType(GaugeResult.ChartType chartType) {
        this.chartType = chartType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}