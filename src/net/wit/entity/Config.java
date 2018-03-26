package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 系统设置
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_config")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_config_sequence")
public class Config extends BaseEntity {

    private static final long serialVersionUID = 58L;

    /** key */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment 'key'")
    @NotNull
    private String key;

    /** value */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment 'value'")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getBigDecimal() {
       if (this.value==null) {
           return BigDecimal.ZERO;
       } else {
           return new BigDecimal(this.value);
       }
    }

    public Long getLong() {
        if (this.value==null) {
            return 0L;
        } else {
            return new Long(this.value);
        }
    }

 }
