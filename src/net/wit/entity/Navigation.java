package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.util.JsonUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Entity-navigation
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_navigation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_navigation_sequence")
public class Navigation extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /**
     * 状态 orderType=priceAsc  priceDesc  dateDesc, hitsDesc, default
     */

    public enum Type {
        /**  文章 */
        article,
        /**  商品 */
        product,
        /**  新品 */
        news,
        /**  视频 */
        video,
        /**  促销 */
        promotion,
        /**  推荐 */
        recommend,
        /**  商城 */
        mall,
        /**  拼团 */
        dragon,
        /**  图集 */
        images
    }

    /**  类型 */
    @Column(columnDefinition="int(11) comment '类型 { article:等待支付,product:开通成功,failure:支付失败}'")
    private Type type;

    /*名称*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name ;

    /*图标*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '图标'")
    private String logo ;

    /*参数 {tag:0,category:23} */
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '参数'")
    private String extend;

    /** 业主,入驻商家时不能为空 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member owner;

    /*删除前处理*/
    @PreRemove
    public void preRemove(){
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @JsonIgnore
    public Long getArticleCategoryId() {
        if (getExtend()!=null) {
            Map<String, Long> data = JsonUtils.toObject(getExtend(),Map.class);
            return data.get("articleCategory");
        } else {
            return 0L;
        }
    }

    @JsonIgnore
    public Long getArticleCatalogId() {
        if (getExtend()!=null) {
            Map<String, Long> data = JsonUtils.toObject(getExtend(),Map.class);
            return data.get("articleCatalog");
        } else {
            return 0L;
        }
    }

}
