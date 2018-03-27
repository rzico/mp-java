package net.wit.Calculator;

import net.wit.entity.Gauge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsr on 2018/3/27.
 */
public class GeneCalculator {
    private Map<String,Object> genes = new HashMap<>();
    private Gauge gauge;

    public Map<String, Object> getGenes() {
        return genes;
    }

    public void setGenes(Map<String, Object> genes) {
        this.genes = genes;
    }

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }
}
