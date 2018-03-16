
package net.wit.controller;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Page;
import net.wit.Principal;
import net.wit.entity.*;
import net.wit.plat.im.User;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.plat.weixin.pojo.AccessToken;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.plat.weixin.message.resp.Article;
import net.wit.plat.weixin.message.resp.NewsMessage;
import net.wit.plat.weixin.util.MessageUtil;
import net.wit.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

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

    /**
     * 付款页
     *
     * @param sn              支付单号
     *
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String sn,HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        String userAgent = request.getHeader("user-agent");
        String type="weixin";
        if (BrowseUtil.isAlipay(userAgent)) {
            type="alipay";
        } else {
            type="weixin";
        }
        if (payment.getPaymentPluginId()!=null) {
            if ("cardPayPlugin".equals(payment.getPaymentPluginId())) {
                type = "cardPayPlugin";
            } else if ("balancePayPlugin".equals(payment.getPaymentPluginId())) {
                type = "balancePayPlugin";
            }
        }
        return "forward:/weixin/payment/view.html?psn="+sn+"&amount="+payment.getAmount()+"&type="+type;
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
    public Message getConfig(String url,HttpServletRequest request) {
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

    /**
     * 确认请求来自微信服务器
     */
    @RequestMapping(value = "/notify",method = RequestMethod.GET)
    public Boolean notify_get(HttpServletRequest request, HttpServletResponse response, ModelMap model)  throws Exception {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(echostr);
        out.flush();
        return true;

    }

    /**
     * 处理微信服务器发来的消息
     */

    @RequestMapping(value = "/notify",method = RequestMethod.POST)
    public Boolean notify_post(HttpServletRequest request, HttpServletResponse response, ModelMap model) {		// 调用核心业务类接收消息、处理消息
        try {
//            System.out.println("weixin.notify");
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 事件推送
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                NewsMessage newsMessage = new NewsMessage();
                newsMessage.setToUserName(fromUserName);
                newsMessage.setFromUserName(toUserName);
                newsMessage.setCreateTime(new Date().getTime());
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                newsMessage.setFuncFlag(0);
                // 事件类型
                String eventType = requestMap.get("Event").toLowerCase();
                // 关注订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//                    try {
//                        List<Tag> tags = new ArrayList<Tag>();
//                        tags.add(tagService.find(1L));
//                        Page<net.wit.entity.Article> arts = articleService.findPage(null,null,tags,null);
//
//                        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//                        String eventKey = requestMap.get("EventKey");
//                        List<Article> articles = new ArrayList<Article>();
//                        for (net.wit.entity.Article art:arts.getContent()) {
//                            Article article = new Article();
//                            article.setDescription(art.delHTMLTag());
//                            article.setPicUrl(art.getThumbnail());
//                            article.setTitle(art.getTitle());
//                            article.setUrl("http://"+bundle.getString("weixin.url")+"/website/article/view.jhtml?id="+art.getId());
//                            articles.add(article);
//                        }
//                        newsMessage.setArticles(articles);
//                        newsMessage.setArticleCount(articles.size());
//                        response.setContentType("application/octet-stream");
//                        PrintWriter out = response.getWriter();
//                        out.print(MessageUtil.newsMessageToXml(newsMessage));
//                        out.flush();
//                    } catch (Exception e) {
//                        logger.error(e.getMessage());
//                    }
//
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {

                }
                // 扫描二维码
                else if (eventType.equals(MessageUtil.SCAN)) {

                }
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 确认请求来自微信服务器
     */
    @RequestMapping(value = "/{appId}/notify",method = RequestMethod.GET)
    public Boolean notify_appid_get(@PathVariable String appId,HttpServletRequest request, HttpServletResponse response, ModelMap model)  throws Exception {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("echostr");
        out.flush();
        return true;

    }

    /**
     * 处理微信服务器发来的消息
     */

    @RequestMapping(value = "/{appId}/notify",method = RequestMethod.POST)
    public Boolean notify_appid_post(@PathVariable String appId,HttpServletRequest request, HttpServletResponse response, ModelMap model) {		// 调用核心业务类接收消息、处理消息
        try {
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 事件推送
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                NewsMessage newsMessage = new NewsMessage();
                newsMessage.setToUserName(fromUserName);
                newsMessage.setFromUserName(toUserName);
                newsMessage.setCreateTime(new Date().getTime());
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                newsMessage.setFuncFlag(0);
                // 事件类型
                String eventType = requestMap.get("Event").toLowerCase();
                // 关注订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//                    try {
//                        List<Tag> tags = new ArrayList<Tag>();
//                        tags.add(tagService.find(1L));
//                        Page<net.wit.entity.Article> arts = articleService.findPage(null,null,tags,null);
//
//                        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//                        String eventKey = requestMap.get("EventKey");
//                        List<Article> articles = new ArrayList<Article>();
//                        for (net.wit.entity.Article art:arts.getContent()) {
//                            Article article = new Article();
//                            article.setDescription(art.delHTMLTag());
//                            article.setPicUrl(art.getThumbnail());
//                            article.setTitle(art.getTitle());
//                            article.setUrl("http://"+bundle.getString("weixin.url")+"/website/article/view.jhtml?id="+art.getId());
//                            articles.add(article);
//                        }
//                        newsMessage.setArticles(articles);
//                        newsMessage.setArticleCount(articles.size());
//                        response.setContentType("application/octet-stream");
//                        PrintWriter out = response.getWriter();
//                        out.print(MessageUtil.newsMessageToXml(newsMessage));
//                        out.flush();
//                    } catch (Exception e) {
//                        logger.error(e.getMessage());
//                    }
//
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {

                }
                // 扫描二维码
                else if (eventType.equals(MessageUtil.SCAN)) {

                }
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }


    //老版本
    @RequestMapping(value = "/qrcode/go", method = RequestMethod.GET)
    public String go(String type, String no,HttpServletRequest request) {        // 调用核心业务类接收消息、处理消息
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member==null) {
            String userAgent = request.getHeader("user-agent");
            if (BrowseUtil.isWeixin(userAgent)) {
                String url = "http://" + bundle.getString("weixin.url") + "/weixin/qrcode/go.jhtml?type=" + type + "&no=" + no;
                String redirectUrl = "http://" + bundle.getString("weixin.url") + "/website/login/weixin.jhtml?redirectURL=" + StringUtils.base64Encode(url.getBytes());
                redirectUrl = URLEncoder.encode(redirectUrl);
                return "redirect:" + MenuManager.codeUrlO2(redirectUrl);
            }
            if (BrowseUtil.isAlipay(userAgent)) {
                String url = "http://" + bundle.getString("weixin.url") + "/weixin/qrcode/go.jhtml?type=" + type + "&no=" + no;
                String redirectUrl = "http://" + bundle.getString("weixin.url") + "/website/login/alipay.jhtml?redirectURL=" + StringUtils.base64Encode(url.getBytes());
                redirectUrl = URLEncoder.encode(redirectUrl);
                String alipay = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + bundle.getString("alipay.appid")+"&scope=auth_base&state=state&redirect_uri=" + redirectUrl;
                return "redirect:" + alipay;
            }
        }
        if ("paybill".equals(type)) {
            Shop shop = shopService.find(no);
            if (shop!=null) {
                return "redirect:/paybill/#/?code="+shop.getCode();
            } else {
                return "redirect:/#/";
            }
        } else
        if ("card_active".equals(type)) {
            return "redirect:/#/card?code=" + no;
        }
        return "redirect:/#/";
    }

}
