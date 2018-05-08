package net.wit.plat.weixin.pojo;

import java.util.Date;

/**
 * Created by Jinlesoft on 2018/5/8.
 */
public class ComponentAccessToken {
    private String component_access_token;
    private int expires_in;

    /**到期时间*/
    private Date expire;
    public String getComponent_access_token() {
        return component_access_token;
    }

    public void setComponent_access_token(String component_access_token) {
        this.component_access_token = component_access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}
