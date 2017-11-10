package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity-标签
 * Created by wuxiran on 2017/7/10.
 */

@Entity
@Table(name = "wx_tag")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_tag_sequence")
public class Tag extends OrderEntity{

    private static final long serialVersionUID = 56L;

    /*类型*/
    public enum Type{
        /* 文章 */
        article,
        /* 会员 */
        member,
        /* 模板 */
        template,
        /* 商品 */
        product,
        /* 专栏 */
        topic
    }

    /*名称*/
    @NotNull
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) not null comment '名称'")
    private String name ;

    /*类型*/
    @NotNull
    @Column(columnDefinition="int(11) not null comment '类型 {article:文章,member:会员,template:模板,product:商品}'")
    private Type type;

    /*图标*/
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '图标'")
    private String icon;

    /*备注*/
    @Length(max = 200)
    @Column(columnDefinition="varchar(255) comment '图标'")
    private String memo;

    /*文章列表*/
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<Article>();

    /*会员列表*/
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<Member>();

    /*模板列表*/
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Template> templates = new ArrayList<Template>();

    /*模板列表*/
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Topic> topics = new ArrayList<Topic>();

    /*模板列表*/
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<Product>();

    /*删除前处理*/
    @PreRemove
    public void preRemove(){
        List<Article> articles = getArticles();
        if(articles!=null){
            for(Article article:articles){
                article.getTags().remove(this);
            }

        }
        List<Member> members = getMembers();
        if(members!=null){
            for(Member member:members){
                member.getTags().remove(this);
            }

        }
        List<Template> templates = getTemplates();
        if(templates!=null){
            for(Template template:templates){
                template.getTags().remove(this);
            }

        }
        List<Topic> topics = getTopics();
        if(topics!=null){
            for(Topic topic:topics){
                topic.getTags().remove(this);
            }

        }
        List<Product> products = getProducts();
        if(products!=null){
            for(Product product:products){
                product.getTags().remove(this);
            }

        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void setType(net.wit.entity.Tag.Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


}
