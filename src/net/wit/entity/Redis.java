package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity-缓存 为了省钱以后要改 redis
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_redis")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_redis_sequence")
public class Redis extends BaseEntity{

    private static final long serialVersionUID = 45L;

    /*key*/
    @Column(name = "redis_key",columnDefinition="varchar(80) not null unique comment '缓存'")
    private String key;

    /*value*/
    @Lob
    @Column(name = "redis_value",columnDefinition="longtext not null comment '内容'")
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
}
