package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Counselor;
import net.wit.entity.CounselorOrder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章列表图

public class CounselorOrderModel extends BaseModel implements Serializable {
    
    private Long id;

    private Date createDate;

    /** 头像 */
    private String logo;

    /** 名称 */
    private String name;

    /** 问题 */
    private String worry ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorry() {
        return worry;
    }

    public void setWorry(String worry) {
        this.worry = worry;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void bind(CounselorOrder counselorOrder) {

        this.id = counselorOrder.getId();
        this.name = counselorOrder.getName();
        this.logo = counselorOrder.getCounselor().getLogo();
        this.worry = counselorOrder.getWorry();
        this.createDate = counselorOrder.getCreateDate();

    }

    public static List<CounselorOrderModel> bindList(List<CounselorOrder> counselors) {
        List<CounselorOrderModel> ms = new ArrayList<CounselorOrderModel>();
        for (CounselorOrder counselor:counselors) {
            CounselorOrderModel m = new CounselorOrderModel();
            m.bind(counselor);
            ms.add(m);
        }
        return ms;
    }

}