package net.wit.entity.model;

import net.wit.entity.AppletSowingMap;

public class AppletSowingMapModel {
    /** 动作 */
    private AppletSowingMap.ACTION action;

    /** 跳转ID */
    private Long actionId;

    /** 跳转链接 */
    private String url;

    /**  封面  */
    private String frontcover;


    /** 排序 */
    private Integer orders;

    public static AppletSowingMapModel bind(AppletSowingMap appletSowingMap){
        AppletSowingMapModel model = new AppletSowingMapModel();
        model.action = appletSowingMap.getAction();
        model.actionId = appletSowingMap.getActionId();
        model.url = appletSowingMap.getUrl();
        model.frontcover = appletSowingMap.getFrontcover();
        return model;
    }

    public AppletSowingMap.ACTION getAction() {
        return action;
    }

    public void setAction(AppletSowingMap.ACTION action) {
        this.action = action;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }
}
