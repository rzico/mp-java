package net.wit.controller.model;

import net.wit.entity.CompanyLabel;
import net.wit.entity.Enterprise;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eric-Yang on 2018/5/9.
 */
public class CompanyDetailsModel extends BaseModel implements Serializable {

    /**
     * 企业
     */
    private String name;

    /**
     * logo
     */
    private String logo;

    /**
     * 服务电话
     */
    private String phone;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 企业营业时间
     */
    private String time;

    /**
     * 企业标签
     */
    private String label;

    /**
     * 企业介绍图1
     */
    private String image1;

    /**
     * 企业介绍图1
     */
    private String image2;

    /**
     * 企业介绍图1
     */
    private String image3;

    /**
     * 企业介绍图1
     */
    private String image4;

    /**
     * 企业介绍图1
     */
    private String image5;

    /**
     * 企业介绍图1
     */
    private String image6;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public void bind(Enterprise enterprise) {
        this.name = enterprise.getName();
        this.address=enterprise.getAddress();
        this.logo=enterprise.getLogo();
        this.phone=enterprise.getPhone();
        this.image1=enterprise.getImage1();
        this.image2=enterprise.getImage2();
        this.image3=enterprise.getImage3();
        this.image4=enterprise.getImage4();
        this.image5=enterprise.getImage5();
        this.image6=enterprise.getImage6();
        String s=" ";
        List<CompanyLabel> list =enterprise.getLabel();
        for(CompanyLabel c: list){
            s=s+c.getName()+"·";
        }
        this.label=s.substring(0,s.length()-1);
        this.time=enterprise.getTime();
    }
}
