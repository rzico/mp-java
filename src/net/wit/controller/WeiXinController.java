
package net.wit.controller;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Page;
import net.wit.Principal;
import net.wit.entity.*;
import net.wit.plat.im.User;
import net.wit.plat.weixin.aes.AesException;
import net.wit.plat.weixin.aes.WXBizMsgCrypt;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.plat.weixin.pojo.AuthAccessToken;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.propa.ArticlePropa;
import net.wit.plat.weixin.util.SignUtil;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.plat.weixin.message.resp.Article;
import net.wit.plat.weixin.message.resp.NewsMessage;
import net.wit.plat.weixin.util.MessageUtil;
import net.wit.util.*;

import java.util.*;

import org.apache.commons.lang.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Controller - 微信
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinController")
@RequestMapping("/weixin")
public class WeiXinController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;


    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    //消息校验TOKEN
//    private static final String COMPONENT_TOKEN="witpay";
//
//    //消息加解密KEY
//    private static final String COMPONENT_ENCODINGAESKEY="DDHsgFE7U5AoNPgPlkG0uO8Wmhc8cu9pOuXDWtIA57w";
//
//    //第三方公众平台APPID
//    private static final String COMPONENT_APPID="wxfae4ebf43607d851";
//
//    //第三方公众平台Secret
//    private static final String COMPONENT_SECRET="58a0a681beee1bf5e9f7cd49f7658736";

    /**
     * 付款页
     *
     * @param sn 支付单号
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String sn, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        String userAgent = request.getHeader("user-agent");
        String type = "weixin";
        if (BrowseUtil.isAlipay(userAgent)) {
            type = "alipay";
        } else {
            type = "weixin";
        }
        if (payment.getPaymentPluginId() != null) {
            if ("cardPayPlugin".equals(payment.getPaymentPluginId())) {
                type = "cardPayPlugin";
            } else if ("balancePayPlugin".equals(payment.getPaymentPluginId())) {
                type = "balancePayPlugin";
            }
        }
        return "forward:/weixin/payment/view.html?psn=" + sn + "&amount=" + payment.getAmount() + "&type=" + type;
    }

    public String getSha1Sign(HashMap<String, Object> params) {
        try {
            String str1 = WeiXinUtils.FormatBizQueryParaMap(params, false);
            return Sha1Util.encode(str1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取微信配置参数
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/get_config")
    @ResponseBody
    public Message getConfig(String url, HttpServletRequest request) {
        String noncestr = WeiXinUtils.CreateNoncestr();
        String timeStamp = WeiXinUtils.getTimeStamp();
        HashMap<String, Object> map = new HashMap<>();
        map.put("noncestr", noncestr);
        Ticket ticket = WeixinApi.getTicket();
        if (ticket == null) {
            return Message.error("ticket 获取失败");
        }
        map.put("jsapi_ticket", ticket.getTicket());
        map.put("timestamp", timeStamp);
        map.put("url", url);
        String sha1Sign = getSha1Sign(map);
        HashMap<String, Object> config = new HashMap<>();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        config.put("appId", bundle.getString("weixin.appid"));
        config.put("timestamp", timeStamp);
        config.put("nonceStr", noncestr);
        config.put("signature", sha1Sign);
        return Message.success(config, "执行成功");
    }

    @RequestMapping(value = "/notify")
    public void acceptAuthorizeEvent(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException, DocumentException {
        System.out.println("微信第三方平台---------微信推送Ticket消息10分钟一次-----------");
        processAuthorizeEvent(request);
        output(response, "success"); // 输出响应的内容。
        System.out.println("微信第三方平台---------响应成功-----------");
    }

    /**
     * 处理微信服务器发来的消息
     */

    @RequestMapping(value = "/{appId}/notify")
    public void notify_appid_post(@PathVariable String appId, HttpServletRequest request, HttpServletResponse response, ModelMap model) {        // 调用核心业务类接收消息、处理消息

        try {
            System.out.println(appId + "进入/{appId}/notify  POST+++++++++++++++++++++++++++++++++" + System.currentTimeMillis());
            Core(request, response, appId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //老版本
    @RequestMapping(value = "/qrcode/go", method = RequestMethod.GET)
    public String go(String type, String no, HttpServletRequest request) {        // 调用核心业务类接收消息、处理消息
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member == null) {
            String userAgent = request.getHeader("user-agent");
            if (BrowseUtil.isWeixin(userAgent)) {
                String url = "http://" + bundle.getString("weixin.url") + "/weixin/qrcode/go.jhtml?type=" + type + "&no=" + no;
                String redirectUrl = "http://" + bundle.getString("weixin.url") + "/website/login/weixin.jhtml?redirectURL=" + net.wit.util.StringUtils.base64Encode(url.getBytes());
                redirectUrl = URLEncoder.encode(redirectUrl);
                return "redirect:" + MenuManager.codeUrlO2(redirectUrl);
            }
            if (BrowseUtil.isAlipay(userAgent)) {
                String url = "http://" + bundle.getString("weixin.url") + "/weixin/qrcode/go.jhtml?type=" + type + "&no=" + no;
                String redirectUrl = "http://" + bundle.getString("weixin.url") + "/website/login/alipay.jhtml?redirectURL=" + net.wit.util.StringUtils.base64Encode(url.getBytes());
                redirectUrl = URLEncoder.encode(redirectUrl);
                String alipay = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + bundle.getString("alipay.appid") + "&scope=auth_base&state=state&redirect_uri=" + redirectUrl;
                return "redirect:" + alipay;
            }
        }
        if ("paybill".equals(type)) {
            Shop shop = shopService.find(no);
            if (shop != null) {
                return "redirect:/paybill/#/?code=" + shop.getCode();
            } else {
                return "redirect:/#/";
            }
        } else if ("card_active".equals(type)) {
            return "redirect:/#/card?code=" + no;
        }
        return "redirect:/#/";
    }

    /**
     * 确认请求来自微信服务器
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
//            output(response,"success");
        }
    }

    /**
     * 回复微信服务器"文本消息"
     *
     * @param response
     * @param returnvaleue
     */
    public void output(HttpServletResponse response, String returnvaleue) {
        try {
            System.out.println("--------------------response开始返回数据给微信端-----------------" + System.currentTimeMillis());
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
            pw.flush();
            System.out.println("--------------------response返回数据完成,真~~~~~结束-----------------" + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 核心
     *
     * @param request
     * @param response
     */
    public void Core(HttpServletRequest request, HttpServletResponse response, String appid) throws Exception {

        System.out.println("--------------------------我是头分界线------------------------------");
        System.out.println("--------------------------确认请求来自微信--------------------------");
        System.out.println("--------------------------我是尾分界线------------------------------");
        doGet(request, response);

        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String msgSignature = request.getParameter("msg_signature");
        String encrypt_type = request.getParameter("encrypt_type");

        if (!org.apache.commons.lang.StringUtils.isNotBlank(msgSignature)) {
            return;
        }

        System.out.println("--------------------------我是头分界线------------------------------");
        System.out.println("timestamp:" + timestamp);
        System.out.println("encrypt_type:" + encrypt_type);
        System.out.println("nonce:" + nonce);
        System.out.println("msg_signature:" + msgSignature);
        System.out.println("--------------------------我是尾分界线------------------------------");

        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();

        String xml = sb.toString();

        System.out.println("--------------------------我是头分界线------------------------------");
        System.out.println("微信推送的原生：" + xml);
        System.out.println("--------------------------我是尾分界线------------------------------");


        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        WXBizMsgCrypt pc = new WXBizMsgCrypt(bundle.getString("weixin.component.token"), bundle.getString("weixin.component.encodingaeskey"), bundle.getString("weixin.component.appid"));
        xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);

        System.out.println("--------------------------我是头分界线------------------------------");
        System.out.println("解密后的XML：" + xml);
        System.out.println("--------------------------我是尾分界线------------------------------");

        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement();
        String msgType = rootElt.elementText("MsgType");
        String toUserName = rootElt.elementText("ToUserName");
        String fromUserName = rootElt.elementText("FromUserName");

        System.out.println("--------------------------我是头分界线------------------------------");
        System.out.println("msgType:" + msgType);
        System.out.println("toUserName:" + toUserName);
        System.out.println("fromUserName:" + fromUserName);
        System.out.println("--------------------------我是尾分界线------------------------------");

        if ("event".equals(msgType)) {
            System.out.println("--------------------------我是EVENT事件消息分界线------------------------------");
            System.out.println("--------------------------开始(START)------------------------------");
            String event = rootElt.elementText("Event");
            replyEventMessage(request, response, event, toUserName, fromUserName, appid);
        } else if ("text".equals(msgType)) {
            System.out.println("--------------------------我是文本类事件消息分界线------------------------------");
            System.out.println("--------------------------开始(START)------------------------------");
            String content = rootElt.elementText("Content");
            processTextMessage(request, response, content, toUserName, fromUserName, appid);
        }

    }

    public void replyEventMessage(HttpServletRequest request, HttpServletResponse response, String event, String toUserName, String fromUserName, String appId) throws AesException {
        String content = event + "from_callback";
        System.out.println("--------------EVENT事件回复消息  content=" + content + "   toUserName=" + toUserName + "   fromUserName=" + fromUserName + "     appid=" + appId);
        replyTextMessage(request, response, content, toUserName, fromUserName, appId);
    }

    public void processTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName, String appid) throws AesException {
        if ("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)) {
            System.out.println("--------------文本事件回复消息  content=" + content + "   toUserName=" + toUserName + "   fromUserName=" + fromUserName);
            String returnContent = content + "_callback";
            replyTextMessage(request, response, returnContent, toUserName, fromUserName, appid);
        } else if (org.apache.commons.lang.StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE")) {
            System.out.println("--------------文本事件回复消息:空字符串-------------------------------");
            output(response, "");
            System.out.println("--------------API文本事件回复消息  content=" + content + "   toUserName=" + toUserName + "   fromUserName=" + fromUserName);
            //接下来客服API再回复一次消息
            replyApiTextMessage(request, response, content.split(":")[1], fromUserName, appid);
        }
    }

    public void replyApiTextMessage(HttpServletRequest request, HttpServletResponse response, String auth_code, String fromUserName, String appid) {
        // 得到微信授权成功的消息后，应该立刻进行处理！！相关信息只会在首次授权的时候推送过来
        System.out.println("------step.1----使用客服消息接口回复粉丝----逻辑开始-------------------------");
        try {
            System.out.println("------step.2----获取第三方TOKEN----逻辑开始-------------------------auth_code: " + auth_code);
//            net.wit.entity.Article article=articleService.find(476l);
//            System.out.println(article.getContent());

            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//            String conToken = ArticlePropa.getComponentToken(bundle.getString("weixin.component.appid"),bundle.getString("weixin.component.secret"),article.getContent());
            PluginConfig pluginConfig = pluginConfigService.findByPluginId("verifyTicket");
            String verifyTicket = pluginConfig.getAttribute("verify_ticket");
            String conToken = WeixinApi.getComponentToken(verifyTicket, bundle.getString("weixin.component.appid"), bundle.getString("weixin.component.secret")).getComponent_access_token();
            System.out.println("------step.2.5----获取第三方TOKEN----逻辑开始-------------------------token:" + conToken);
//            String authcode=ArticlePropa.getAuthCode(conToken,COMPONENT_APPID);
//            String authCode=ArticlePropa.getAuthorizationCode(conToken,bundle.getString("weixin.component.appid"),auth_code);
            AuthAccessToken authAccessToken = WeixinApi.getAuthorizationCode(conToken, bundle.getString("weixin.component.appid"), auth_code);
            System.out.println("------step.3----使用客服消息接口回复粉丝----逻辑开始---------------------authCode:" + auth_code);
            String authToken = authAccessToken.getAuthorizer_access_token();

//            System.out.println("------step.4----使用客服消息接口回复粉丝----逻辑开始---------------------jsonRes.authorization_info:"+authCode);
//            JSONObject jsonObject1=JSONObject.fromObject(authCode);
//            String authToken=jsonObject1.get("authorizer_access_token").toString();
            System.out.println("------step.4.5----使用客服消息接口回复粉丝----逻辑开始---------------------代理TOKEN:" + authToken);
            String msg = auth_code + "_from_api";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", fromUserName);
            jsonObject.put("msgtype", "text");
            JSONObject text = new JSONObject();
            text.put("content", msg);
            jsonObject.put("text", text);
            System.out.println("------step.5----使用客服消息接口回复粉丝----逻辑开始---------------------jsonRes.authorization_info:" + jsonObject.toString());
            WeixinApi.httpRequest("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + authToken, "POST", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------结束线-------------------");
    }


    public void replyTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName, String appId) throws AesException {

        System.out.println("--------------开始回复文本消息-------------------------------");
        // 回复文本消息
        Long creatTime = System.currentTimeMillis() / 1000;
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");
        sb.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");
        sb.append("<CreateTime>" + creatTime + "</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA[" + content + "]]></Content>");
        sb.append("</xml>");
        String replyMsg = sb.toString();
        System.out.println("--------------组装好的XML-------------------------------");
        System.out.println(replyMsg);
        //加密

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        WXBizMsgCrypt pc = new WXBizMsgCrypt(bundle.getString("weixin.component.token"), bundle.getString("weixin.component.encodingaeskey"), bundle.getString("weixin.component.appid"));
        String returnvaleue = pc.encryptMsg(replyMsg, creatTime.toString(), request.getParameter("nonce"));
        System.out.println("------------------加密后的返回内容 returnvaleue----------------------");
        System.out.println(returnvaleue);

        //发送
        output(response, returnvaleue);
        System.out.println("----------------------结束线-------------------");
    }

    /**
     * 处理授权事件的推送
     *
     * @param request
     * @throws IOException
     * @throws AesException
     * @throws DocumentException
     */
    public void processAuthorizeEvent(HttpServletRequest request) throws IOException, DocumentException, AesException {
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String signature = request.getParameter("signature");
        String msgSignature = request.getParameter("msg_signature");

        if (!StringUtils.isNotBlank(msgSignature))
            return;// 微信推送给第三方开放平台的消息一定是加过密的，无消息加密无法解密消息
        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        String xml = sb.toString();
        System.out.println("第三方平台全网发布-----------------------原始 Xml=" + xml);
//        xml=xml.replace("<AppId>","<ToUserName>").replace("</AppId>","</ToUserName>");
//        System.out.println("第三方平台全网发布-----------------------替换后 Xml=" + xml);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        WXBizMsgCrypt pc = new WXBizMsgCrypt(bundle.getString("weixin.component.token"), bundle.getString("weixin.component.encodingaeskey"), bundle.getString("weixin.component.appid"));
        xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml.replace("<AppId>", "<ToUserName>").replace("</AppId>", "</ToUserName>"));
        System.out.println("第三方平台全网发布-----------------------解密后 Xml=" + xml);
        xml = xml.replace("<ToUserName>", "<AppId>").replace("</ToUserName>", "</AppId>");
        System.out.println("第三方平台全网发布-----------------------替换回来解密后 Xml=" + xml);
        processAuthorizationEvent(xml);
    }

    /**
     * 保存Ticket
     *
     * @param xml
     */
    void processAuthorizationEvent(String xml) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            String ticket = rootElt.elementText("ComponentVerifyTicket");
            String createTime = rootElt.elementText("CreateTime");
            String appId = rootElt.elementText("AppId");
            String infoType = rootElt.elementText("InfoType");
            String AuthorizerAppid = rootElt.elementText("AuthorizerAppid");
            String AuthorizationCode = rootElt.elementText("AuthorizationCode");
            String PreAuthCode = rootElt.elementText("PreAuthCode");
            System.out.println("7、推送component_verify_ticket协议-----------ComponentVerifyTicket = " + ticket);
            System.out.println("推送component_verify_ticket协议-----------CreateTime = " + createTime);
            System.out.println("推送component_verify_ticket协议-----------AppId = " + appId);
            System.out.println("推送component_verify_ticket协议-----------InfoType = " + infoType);
            System.out.println("推送component_verify_ticket协议-----------AuthorizerAppid = " + AuthorizerAppid);
            System.out.println("推送component_verify_ticket协议-----------AuthorizationCode = " + AuthorizationCode);
            System.out.println("推送component_verify_ticket协议-----------PreAuthCode = " + PreAuthCode);
            if (ticket != null && !ticket.equals("")) {
                net.wit.entity.PluginConfig pluginConfig = pluginConfigService.findByPluginId("verifyTicket");
                pluginConfig.setAttribute("verify_ticket", ticket);
                pluginConfigService.update(pluginConfig);
            }
            if (infoType.equalsIgnoreCase("authorized")) {
                System.out.println("=================================：authorized");
//                Member current = memberService.getCurrent();
//                if (current != null) {
//                    System.out.println("------authorized----保存topic---------------------appid:" + AuthorizerAppid);
//                    TopicConfig topicConfig = current.getTopic().getConfig();
//                    topicConfig.setAppetAppId(AuthorizerAppid);
//                    Topic topic = current.getTopic();
//                    topic.setConfig(topicConfig);
//                    topicService.save(topic);
//                    System.out.println("------authorized----id:" + current.getId() + "---------------------==========>>>>>>>>>:");
//                }

            } else if (infoType.equalsIgnoreCase("updateauthorized")) {
                System.out.println("=================================：updateauthorized");

            } else if (infoType.equalsIgnoreCase("unauthorized")) {
                System.out.println("=================================：unauthorized");

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("----------------------结束线-------------------");
    }

}
