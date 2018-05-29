package net.wit.entity.weixin;

/**
 * Created by Jinlesoft on 2018/5/9.
 * 小程序设置合法域名
 */
public class Domain extends WeiXinCallBack{
    private String[] requestdomain;
    private String[] wsrequestdomain;
    private String[] uploaddomain;
    private String[] downloaddomain;

    public String[] getRequestdomain() {
        return requestdomain;
    }

    public void setRequestdomain(String[] requestdomain) {
        this.requestdomain = requestdomain;
    }

    public String[] getWsrequestdomain() {
        return wsrequestdomain;
    }

    public void setWsrequestdomain(String[] wsrequestdomain) {
        this.wsrequestdomain = wsrequestdomain;
    }

    public String[] getUploaddomain() {
        return uploaddomain;
    }

    public void setUploaddomain(String[] uploaddomain) {
        this.uploaddomain = uploaddomain;
    }

    public String[] getDownloaddomain() {
        return downloaddomain;
    }

    public void setDownloaddomain(String[] downloaddomain) {
        this.downloaddomain = downloaddomain;
    }
}
