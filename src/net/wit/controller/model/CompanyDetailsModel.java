package net.wit.controller.model;

import net.wit.entity.CompanyLabel;
import net.wit.entity.Enterprise;

import java.io.Serializable;
import java.util.ArrayList;
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
     * 企业签名
     */
    private String autograph;

    /**
     * 企业介绍图1
     */
    private List<String> image;

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

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public void bind(Enterprise enterprise) {
        this.name = enterprise.getName();
        if(enterprise.getArea() == null || enterprise.getArea().getFullName() == null || enterprise.getAddress() == null){
            this.address = "";
        }else {
            this.address=enterprise.getArea().getFullName()+enterprise.getAddress();
        }
        this.logo=enterprise.getLogo();
        this.phone=enterprise.getPhone();
        List<String> images=new ArrayList<>();
        if(enterprise.getImage1()!=null){
            images.add(enterprise.getImage1());
        }
        if(enterprise.getImage2()!=null){
            images.add(enterprise.getImage2());
        }
        if(enterprise.getImage3()!=null){
            images.add(enterprise.getImage3());
        }
        if(enterprise.getImage4()!=null){
            images.add(enterprise.getImage4());
        }
        if(enterprise.getImage5()!=null){
            images.add(enterprise.getImage5());
        }
        if(enterprise.getImage6()!=null){
            images.add(enterprise.getImage6());
        }
        this.image=images;
        String s=" ";
        List<CompanyLabel> list =enterprise.getLabel();
        for(CompanyLabel c: list){
            s=s+c.getName()+"·";
        }
        this.label=s.substring(0,s.length()-1);
        this.time=enterprise.getStartTime()+"-"+enterprise.getEndTime();
        this.autograph=enterprise.getAutograph();
    }
}
