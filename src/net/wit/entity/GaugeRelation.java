package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * Entity -  因子关联
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 *
 */

@Entity
@Table(name = "ky_gauge_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "ky_gauge_relation_sequence")
public class GaugeRelation extends OrderEntity {

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private  Gauge gauge;

    /** 量表 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private  Gauge relation;

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public Gauge getRelation() {
        return relation;
    }

    public void setRelation(Gauge relation) {
        this.relation = relation;
    }


    public MapEntity getMapGauge() {
        if (getRelation() != null) {
            return new MapEntity(getRelation().getId().toString(), getRelation().getTitle());
        } else {
            return null;
        }
    }

}
