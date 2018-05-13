package net.wit.controller.component;

import net.wit.Message;
import net.wit.plat.weixin.pojo.ComponentAccessToken;
import net.wit.plat.weixin.util.WeixinApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by Jinlesoft on 2018/5/8.
 */
@Controller("componentCommonController")
@RequestMapping("/component/common")
public class CommonController extends BaseController{



    @RequestMapping(value = "/getAuthUrl" , method = RequestMethod.GET)
    public String getAuthUrl(HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String appId = bundle.getString("weixin.component.appid");
        String secret    = bundle.getString("weixin.component.secret");
        ComponentAccessToken componentAccessToken = WeixinApi.getComponentToken(appId, secret);
        String url = "";
        //http://mopian.1xx.me/weixin/notify.jhtml
        if(componentAccessToken != null && componentAccessToken.getComponent_access_token() !=null && !componentAccessToken.getComponent_access_token().equals("")){
           String preAuthCode = WeixinApi.getPreAuthCode(componentAccessToken.getComponent_access_token(), appId);
           url = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&auth_type=2&no_scan=1&component_appid=" + appId + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + "mopian.1xx.me" + "#wechat_redirect";
            return "redirect:" + url;
        }
        return "";
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("url", url);
//        return Message.bind(data,request);

    }
}
