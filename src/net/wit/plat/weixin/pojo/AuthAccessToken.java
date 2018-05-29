package net.wit.plat.weixin.pojo;

import java.util.Date;
import java.util.List;

/**
 * Created by Jinlesoft on 2018/5/8.
 */
public class AuthAccessToken {
    private String authorizer_appid;
    private String authorizer_access_token;
    private int expires_in;
    private Date expire;
    private String authorizer_refresh_token;
    private List<FuncInfo> func_info;

    public String getAuthorizer_appid() {
        return authorizer_appid;
    }

    public void setAuthorizer_appid(String authorizer_appid) {
        this.authorizer_appid = authorizer_appid;
    }

    public String getAuthorizer_access_token() {
        return authorizer_access_token;
    }

    public void setAuthorizer_access_token(String authorizer_access_token) {
        this.authorizer_access_token = authorizer_access_token;
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

    public void setExpire(Date expires) {
        this.expire = expires;
    }

    public String getAuthorizer_refresh_token() {
        return authorizer_refresh_token;
    }

    public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
        this.authorizer_refresh_token = authorizer_refresh_token;
    }

    public List<FuncInfo> getFunc_info() {
        return func_info;
    }

    public void setFunc_info(List<FuncInfo> func_info) {
        this.func_info = func_info;
    }
}
