package net.wit.controller.model;
import net.wit.entity.Distribution;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DistributionModel extends BaseModel implements Serializable {
    private Long id;
    private String name;
    private BigDecimal percent1;
    private BigDecimal percent2;
    private BigDecimal percent3;
    private Long point;

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

    public BigDecimal getPercent1() {
        return percent1;
    }

    public void setPercent1(BigDecimal percent1) {
        this.percent1 = percent1;
    }

    public BigDecimal getPercent2() {
        return percent2;
    }

    public void setPercent2(BigDecimal percent2) {
        this.percent2 = percent2;
    }

    public BigDecimal getPercent3() {
        return percent3;
    }

    public void setPercent3(BigDecimal percent3) {
        this.percent3 = percent3;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public void bind(Distribution distribution) {
        this.id = distribution.getId();
        this.name = distribution.getName();
        this.percent1 = distribution.getPercent1();
        this.percent2 = distribution.getPercent2();
        this.percent3 = distribution.getPercent3();
        this.point = distribution.getPoint();
    }

    public static List<DistributionModel> bindList(List<Distribution> distributions) {
        List<DistributionModel> ms = new ArrayList<DistributionModel>();
        for (Distribution distribution:distributions) {
          DistributionModel m = new DistributionModel();
          m.bind(distribution);
          ms.add(m);
        }
        return ms;
    }

}