package net.wit.controller.component;

import net.wit.Message;
import net.wit.entity.PluginConfig;
import net.wit.plat.weixin.pojo.ComponentAccessToken;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.MemberService;
import net.wit.service.PluginConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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


    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    @RequestMapping(value = "/getAuthUrl" , method = RequestMethod.GET)
    public String getAuthUrl(HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String appId = bundle.getString("weixin.component.appid");
        String secret    = bundle.getString("weixin.component.secret");
        PluginConfig pluginConfig = pluginConfigService.findByPluginId("verifyTicket");
        if(pluginConfig != null){
            String verifyTicket = pluginConfig.getAttribute("verify_ticket");
            ComponentAccessToken componentAccessToken = WeixinApi.getComponentToken(verifyTicket, appId, secret);
            String url = "";
            //http://mopian.1xx.me/weixin/notify.jhtml
            if(componentAccessToken != null && componentAccessToken.getComponent_access_token() !=null && !componentAccessToken.getComponent_access_token().equals("")){
                String preAuthCode = WeixinApi.getPreAuthCode(componentAccessToken.getComponent_access_token(), appId);
                System.out.println("weixinSouquan===============================" + memberService.getCurrent().getId());
                url = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&auth_type=2&no_scan=1&component_appid=" + appId + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + "http://mopian.1xx.me/component/common/weixinCallback.jhtml?" + memberService.getCurrent().getId() + "#wechat_redirect";
                return "redirect:" + url;
            }
        }
        return "redirect:http://mopian.1xx.me";
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("url", url);
//        return Message.bind(data,request);

    }

    @RequestMapping(value = "/weixinCallback" , method = RequestMethod.GET)
    public String weixinCallback(HttpServletRequest request, String auth_code, Long expires_in, Long memberId){
        System.out.println("weixinCallback===============================" + auth_code + "|" + expires_in + "|" + memberId);


        return "redirect:http://mopian.1xx.me";
    }

}
