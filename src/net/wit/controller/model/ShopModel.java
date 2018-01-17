package net.wit.controller.model;
import net.wit.entity.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShopModel extends BaseModel implements Serializable {

    private Long id;
    /** 收款码 */
    private String code;

    /** 名称 */
    private String name;

    /** 营业执照/证件号  个人时用证件号 */
    private String license;

    /** 上传门头 */
    private String thedoor;

    /** 经营场所 */
    private String scene;

    /** 地区 ID null 代表没有区域限制 */
    private Long areaId;

    /** 地区名 */
    private String areaName;

    /** 行业 id */
    private Long categoryId;

    /** 行业名称 */
    private String categoryName;

    /** 地址 */
    private String address;

    /** 联系人 */
    private String linkman;

    /** 联系电话 */
    private String telephone;

    /** lat */
    private double lat;

    /** lng */
    private double lng;

    /** 距离 */
    private double distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getThedoor() {
        return thedoor;
    }

    public void setThedoor(String thedoor) {
        this.thedoor = thedoor;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void bind(Shop shop) {
        this.id = shop.getId();
        this.address = shop.getAddress();
        this.license = shop.getLicense();
        if (shop.getArea()!=null) {
            this.areaId = shop.getArea().getId();
            this.areaName = shop.getArea().getFullName();
        }
        if (shop.getCategory()!=null) {
            this.categoryId = shop.getCategory().getId();
            this.categoryName = shop.getCategory().getName();
        }
        this.code = shop.getCode();
        this.scene = shop.getScene();
        this.thedoor = shop.getThedoor();
        this.telephone = shop.getTelephone();
        this.linkman = shop.getLinkman();
        this.name = shop.getName();
        if (shop.getLocation()!=null) {
            this.lat = shop.getLocation().getLat();
            this.lng = shop.getLocation().getLng();
        }
    }

    public static List<ShopModel> bindList(List<Shop> shops,double lat,double lng) {
        List<ShopModel> ms = new ArrayList<ShopModel>();
        for (Shop shop:shops) {
            ShopModel m = new ShopModel();
            m.bind(shop);

            if (lat>0 && lng>0) {
                Location location = shop.getLocation();
                if (location.getLat()>0 && location.getLng()>0) {
                    m.setDistance(shop.getLocation().calcDistance(lat, lng));
                }
            }

            ms.add(m);
        }
        return ms;
    }
}