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
    private Boolean isDefault;
    private Long roadId;
    private String roadName;

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

    public void bind(Receiver receiver) {
        this.id = receiver.getId();
        this.consignee = receiver.getConsignee();
        this.address = receiver.getAddress();
        this.areaId = receiver.getArea().getId();
        this.areaName = receiver.getAreaName();
        this.phone = receiver.getPhone();
        this.isDefault = receiver.getIsDefault();
        if (receiver.getRoad()!=null) {
            this.roadId = receiver.getRoad().getId();
            this.roadName = receiver.getRoad().getName();
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