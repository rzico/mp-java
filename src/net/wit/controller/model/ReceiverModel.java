package net.wit.controller.model;
import net.wit.entity.Receiver;
import net.wit.entity.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReceiverModel extends BaseModel implements Serializable {
    private Long id;
    private String consignee;
    private String areaName;
    private String address;
    private String phone;
    private Long areaId;
    private Integer level;
    private double lat;
    private double lng;
    private Boolean isDefault;
    private Long shopId;
    private String shopName;
    private Long adminId;
    private String adminName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }



    //
//    public Long getRoadId() {
//        return roadId;
//    }
//

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void bind(Receiver receiver) {
        this.id = receiver.getId();
        this.consignee = receiver.getConsignee();
        this.address = receiver.getAddress();
        this.areaId = receiver.getArea().getId();
        this.areaName = receiver.getAreaName();
        this.phone = receiver.getPhone();
        this.isDefault = receiver.getIsDefault();
        this.lat = 0;
        this.lng = 0;
        if (receiver.getLocation()!=null) {
            this.lng = receiver.getLocation().getLng();
            this.lat = receiver.getLocation().getLat();
        }
        this.level = receiver.getLevel();

        if (receiver.getShop()!=null) {
            this.shopId = receiver.getShop().getId();
            this.shopName = receiver.getShop().getName();
        } else {
            this.shopId = 0L;
        }

        if (receiver.getAdmin()!=null) {
            this.adminId = receiver.getAdmin().getId();
            this.adminName = receiver.getAdmin().getName();
        } else {
            this.adminId = 0L;
        }

    }

    public static List<ReceiverModel> bindList(List<Receiver> receivers) {
        List<ReceiverModel> ms = new ArrayList<ReceiverModel>();
        for (Receiver receiver:receivers) {
          ReceiverModel m = new ReceiverModel();
          m.bind(receiver);
          ms.add(m);
        }
        return ms;
    }

}