package net.wit.controller.model;
import net.wit.entity.OrderRanking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShippingTrackModel extends BaseModel implements Serializable {

    /*送货员姓名*/
    private String name ;
    /*送货方式*/
    private String method ;
    /*送货状态*/
    private String status ;
    /*经度*/
    private double lat ;
    /*纬度*/
    private double lng ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}