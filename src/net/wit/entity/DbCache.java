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
@Table(name = "wx_db_cache")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_db_cache_sequence")
public class DbCache extends OrderEntity{

    private static final long serialVersionUID = 125L;

    /*key*/
    @Column(nullable = false, updatable = false, unique = true)
    private String key;

    /*value*/
    @Lob
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
