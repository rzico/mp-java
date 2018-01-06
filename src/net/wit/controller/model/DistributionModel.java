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

    public void bind(Distribution distribution) {
        this.id = distribution.getId();
        this.name = distribution.getName();
        this.percent1 = distribution.getPercent1();
        this.percent2 = distribution.getPercent2();
        this.percent3 = distribution.getPercent3();
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