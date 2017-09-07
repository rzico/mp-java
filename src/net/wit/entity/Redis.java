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
public class Redis extends OrderEntity{

    private static final long serialVersionUID = 125L;

    /*key*/
    @Column(nullable = false, updatable = false, unique = true,columnDefinition="varchar(50) not null unique comment '缓存'")
    private String key;

    /*value*/
    @Lob
    @Column(nullable = false,columnDefinition="longtext not null unique comment '内容'")
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
