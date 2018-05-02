package net.wit.controller.model;
import net.wit.entity.Live;
import net.wit.entity.OrderRanking;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderRankingModel extends BaseModel implements Serializable {

    private Long id;

    private String thumbnail ;

    private String sn ;

    private String name ;

    /*规格*/
    private String spec ;

    /*奖金*/
    private BigDecimal amount;

    /*积分*/
    private Long point;

    /*排序*/
    private Integer orders;

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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public void bind(OrderRanking orderRanking) {
        this.id = orderRanking.getId();
        this.name = orderRanking.getName();
        this.spec = orderRanking.getSpec();
        this.amount = orderRanking.getAmount();
        this.point = orderRanking.getPoint();
        this.orders = orderRanking.getOrders();
        this.sn = orderRanking.getSn();
        this.thumbnail = orderRanking.getThumbnail();
    }

    public static List<OrderRankingModel> bindList(List<OrderRanking> orderRankings) {
        List<OrderRankingModel> ms = new ArrayList<OrderRankingModel>();
        for (OrderRanking orderRanking:orderRankings) {
            OrderRankingModel m = new OrderRankingModel();
            m.bind(orderRanking);
            ms.add(m);
        }
        return ms;
    }

}