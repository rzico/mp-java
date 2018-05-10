package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.controller.admin.BaseController;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Eric-Yang on 2018/5/9.
 */
@Entity
@Table(name = "wx_company_label")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_company_label_sequence")
public class CompanyLabel extends BaseEntity{

    /** 标签名称 */
    @Column(columnDefinition="varchar(255) comment '标签名称'")
    private String name;

    /** 标签简介 */
    @Column(columnDefinition="varchar(255) comment '标签简介'")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "wx_enterprise_label")
    @OrderBy("modifyDate desc")
    private List<Enterprise> enterprise;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Enterprise> getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(List<Enterprise> enterprise) {
        this.enterprise = enterprise;
    }
}
