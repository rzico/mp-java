package net.wit.controller.component;

import net.wit.Message;
import net.wit.entity.*;
import net.wit.plat.weixin.pojo.AuthAccessToken;
import net.wit.plat.weixin.pojo.AuthorizerInfo;
import net.wit.plat.weixin.pojo.ComponentAccessToken;
import net.wit.plat.weixin.pojo.SmallInformation;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class CommonController extends BaseController {


    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    @RequestMapping(value = "/getAuthUrl", method = RequestMethod.GET)
    public String getAuthUrl(HttpServletRequest request) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String appId = bundle.getString("weixin.component.appid");
        String secret = bundle.getString("weixin.component.secret");
        PluginConfig pluginConfig = pluginConfigService.findByPluginId("verifyTicket");
        if (pluginConfig != null) {
            String verifyTicket = pluginConfig.getAttribute("verify_ticket");
            ComponentAccessToken componentAccessToken = WeixinApi.getComponentToken(verifyTicket, appId, secret);
            String url = "";
            //http://mopian.1xx.me/weixin/notify.jhtml
            if (componentAccessToken != null && componentAccessToken.getComponent_access_token() != null && !componentAccessToken.getComponent_access_token().equals("")) {
                String preAuthCode = WeixinApi.getPreAuthCode(componentAccessToken.getComponent_access_token(), appId);
                Member member = memberService.getCurrent();
                System.out.println("weixinSouquan===============================:" + (member == null ? "null" : "nonull"));
                if (member == null) return "redirect:http://mopian.1xx.me";
                System.out.println("weixinSouquan===============================" + member.getId());
                //+ memberService.getCurrent().getId()；
//                String reUrl = net.wit.util.StringUtils.base64Encode(("http://" + bundle.getString("weixin.component.url") + "/component/common/weixinCallback.jhtml").getBytes());
                String reUrl = "http://" + bundle.getString("weixin.component.url") + "/component/common/weixinCallback.jhtml?memberId=" + memberService.getCurrent().getId();
                url = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&auth_type=2&no_scan=1&component_appid=" + appId + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + reUrl + "#wechat_redirect";
                return "redirect:" + url;
            }
        }
        return "redirect:http://mopian.1xx.me";
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("url", url);
//        return Message.bind(data,request);

    }

    @RequestMapping(value = "/weixinCallback", method = RequestMethod.GET)
    public String weixinCallback(HttpServletRequest request, String auth_code, Long expires_in, Long memberId) {
        System.out.println("weixinCallback===============================" + auth_code + "|" + expires_in + "|" + memberId);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String serverUrl = bundle.getString("weixin.component.url");
        String appId = bundle.getString("weixin.component.appid");
        String secret = bundle.getString("weixin.component.secret");
        PluginConfig pluginConfig = pluginConfigService.findByPluginId("verifyTicket");
        Member member = memberService.find(memberId);//授权的用户
        if (member != null && pluginConfig != null) {
            //weex/member/topic/submit.jhtml
//            if(member.getTopic() == null)
            Admin admin = adminService.findByMember(member);
            if (admin == null || admin.getEnterprise() == null) {//这里是没开通专栏, 则开通成功
                WeixinApi.httpRequest("http://" + serverUrl + "/weex/member/topic/submit.jhtml", "POST", null);
            }

            String verifyTicket = pluginConfig.getAttribute("verify_ticket");
            ComponentAccessToken componentAccessToken = WeixinApi.getComponentToken(verifyTicket, appId, secret);
            if(componentAccessToken == null){
                return "redirect:http://" + serverUrl + "/#/agreeError";
            }
            AuthAccessToken authAccessToken = WeixinApi.getAuthorizationCode(componentAccessToken.getComponent_access_token(), appId, auth_code);

            Topic topic =  member.getTopic();
            if(authAccessToken!=null){
                //设置小程序
                System.out.println("TopicAppetAppid == null ===============================");
                TopicConfig topicConfig = topic.getConfig();
                topicConfig.setAppetAppId(authAccessToken.getAuthorizer_appid());
                topicConfig.setRefreshToken(authAccessToken.getAuthorizer_refresh_token());
                topicConfig.setTokenExpire(authAccessToken.getExpire());
                System.out.println("TopicUpdateSuccess===============================");

                SmallInformation smallInformation = WeixinApi.getSmallInformation(componentAccessToken.getComponent_access_token(), appId, member.getTopic().getConfig().getAppetAppId());
                Topic topic1 = topicService.findByAppId(topicConfig.getAppetAppId());
                System.out.println("smallInformation====================="+ topic1.getName() + "|" + smallInformation.getAuthorizerInfo().toString());
                if(smallInformation.getAuthorizerInfo()!=null){
                    AuthorizerInfo authorizerInfo = smallInformation.getAuthorizerInfo();
                    //为了防止重复设置
//                    if(authorizerInfo.getUserName()!=null && !authorizerInfo.getUserName().equalsIgnoreCase("") && !authorizerInfo.getUserName().equalsIgnoreCase("user_name")){
                        topicConfig.setUserName(authorizerInfo.getUserName());//原始id
//                    }
//                    if(authorizerInfo.getPrincipalName() != null && !authorizerInfo.getPrincipalName().equalsIgnoreCase("") && !authorizerInfo.getPrincipalName().equalsIgnoreCase("principal_name")){
                        topic.setName(authorizerInfo.getPrincipalName());//这里用专栏信息
//                    }
                    topic.setConfig(topicConfig);

                    topic.setName(authorizerInfo.getNickName());//这个是专栏名称 这里设置成小程序的名称了
                    topicService.update(topic);
                    if(admin==null){
                        return "redirect:http://" + serverUrl + "/#/agreeError";
                    }

                    Enterprise enterprise = admin.getEnterprise();
                    enterprise.setName(authorizerInfo.getPrincipalName());//公司名称
                    //更新企业信息
                    enterpriseService.update(enterprise);

                    //接下来 设置小程序的 域名===================



                }
            }else {
                return "redirect:http://" + serverUrl + "/#/agreeError";
            }

//            member.getTopic().getConfig().setAppetAppId();


        } else {
            return "redirect:http://" + serverUrl + "/#/agreeError";
        }
        return "redirect:http://" + serverUrl + "/#/agreeSuccess";
    }

}
