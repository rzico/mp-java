
package net.wit.controller;

import net.wit.Message;
import net.wit.Page;
import net.wit.entity.Payment;
import net.wit.entity.Tag;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.weixin.message.resp.Article;
import net.wit.weixin.message.resp.NewsMessage;
import net.wit.weixin.util.MessageUtil;
import net.wit.weixin.util.SignUtil;
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

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

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
        out.print("echostr");
        out.flush();
        return true;

    }

    /**
     * 处理微信服务器发来的消息
     */

    @RequestMapping(value = "/notify",method = RequestMethod.POST)
    public Boolean notify_post(HttpServletRequest request, HttpServletResponse response, ModelMap model) {		// 调用核心业务类接收消息、处理消息
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
                    try {
                        List<Tag> tags = new ArrayList<Tag>();
                        tags.add(tagService.find(1L));
                        Page<net.wit.entity.Article> arts = articleService.findPage(null,null,tags,null);

                        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
                        String eventKey = requestMap.get("EventKey");
                        List<Article> articles = new ArrayList<Article>();
                        for (net.wit.entity.Article art:arts.getContent()) {
                            Article article = new Article();
                            article.setDescription(art.delHTMLTag());
                            article.setPicUrl(art.getThumbnail());
                            article.setTitle(art.getTitle());
                            article.setUrl("http://"+bundle.getString("weixin.url")+"/website/article/view.jhtml?id="+art.getId());
                            articles.add(article);
                        }
                        newsMessage.setArticles(articles);
                        newsMessage.setArticleCount(articles.size());
                        response.setContentType("application/octet-stream");
                        PrintWriter out = response.getWriter();
                        out.print(MessageUtil.newsMessageToXml(newsMessage));
                        out.flush();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }

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
                    try {
                        List<Tag> tags = new ArrayList<Tag>();
                        tags.add(tagService.find(1L));
                        Page<net.wit.entity.Article> arts = articleService.findPage(null,null,tags,null);

                        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
                        String eventKey = requestMap.get("EventKey");
                        List<Article> articles = new ArrayList<Article>();
                        for (net.wit.entity.Article art:arts.getContent()) {
                            Article article = new Article();
                            article.setDescription(art.delHTMLTag());
                            article.setPicUrl(art.getThumbnail());
                            article.setTitle(art.getTitle());
                            article.setUrl("http://"+bundle.getString("weixin.url")+"/website/article/view.jhtml?id="+art.getId());
                            articles.add(article);
                        }
                        newsMessage.setArticles(articles);
                        newsMessage.setArticleCount(articles.size());
                        response.setContentType("application/octet-stream");
                        PrintWriter out = response.getWriter();
                        out.print(MessageUtil.newsMessageToXml(newsMessage));
                        out.flush();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }

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

}