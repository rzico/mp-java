package net.wit.controller.model;

import net.wit.MapEntity;
import net.wit.entity.Area;
import net.wit.entity.CompanyLabel;
import net.wit.entity.Enterprise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric-Yang on 2018/5/11.
 */
public class CompanyViewsModel extends BaseModel implements Serializable {

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
     * 区域地址
     */
    private MapEntity area;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 企业营业开始时间
     */
    private String starTime;

    /**
     * 企业营业开始时间
     */
    private String endTime;

    /**
     * 企业标签
     */
    private List<MapEntity> label;

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

    public MapEntity getArea() {
        return area;
    }

    public void setArea(MapEntity area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<MapEntity> getLabel() {
        return label;
    }

    public void setLabel(List<MapEntity> label) {
        this.label = label;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public void bind(Enterprise enterprise) {
        this.name = enterprise.getName();
        this.address=enterprise.getAddress();
        this.logo=enterprise.getLogo();
        this.phone=enterprise.getPhone();
        List<String> images=new ArrayList<>();
        images.add(enterprise.getImage1());
        images.add(enterprise.getImage2());
        images.add(enterprise.getImage3());
        images.add(enterprise.getImage4());
        images.add(enterprise.getImage5());
        images.add(enterprise.getImage6());
        this.image=images;
        this.starTime=enterprise.getStarTime();
        this.endTime=enterprise.getEndTime();
        this.area=new MapEntity(enterprise.getArea().getId().toString(),enterprise.getArea().getFullName());
        List<MapEntity> list= new ArrayList<>();
        for(CompanyLabel label: enterprise.getLabel()){
            list.add(new MapEntity(label.getId().toString(),label.getName()));
        }
        this.label=list;
    }
}
