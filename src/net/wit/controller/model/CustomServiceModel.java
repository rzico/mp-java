package net.wit.controller.model;

import net.wit.Setting;
import net.wit.entity.*;
import net.wit.util.SettingUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

//文章展示输出模板 H5等

public class CustomServiceModel extends BaseModel implements Serializable {
    /** 头像 */
    private String logo;
    /** 昵称 */
    private String name;
    /** 微信 */
    private String wechat;
    /**  qq */
    private String qq;

    private String userId;

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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void bind(CustomService customService) {
        this.logo = customService.getLogo();
        this.name = customService.getName();
        this.wechat = customService.getWechat();
        this.qq = customService.getQq();
        if (customService.getMember()!=null) {
            this.userId = customService.getMember().userId();
        }
    }


    public static List<CustomServiceModel> bindList(List<CustomService> customServices) {
        List<CustomServiceModel> ms = new ArrayList<CustomServiceModel>();
        for (CustomService customService:customServices) {
            CustomServiceModel m = new CustomServiceModel();
            m.bind(customService);
            ms.add(m);
        }
        return ms;
    }

}