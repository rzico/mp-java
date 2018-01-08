package net.wit.controller;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ShareModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleShare;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: ShareController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("shareController")
@RequestMapping("/share")
public class ShareController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    /**
     *    分享文章
     */
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    @ResponseBody
    public Message article(Long articleId,ArticleShare.ShareType shareType, Pageable pageable, HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        Member member = memberService.getCurrent();
        ShareModel model = new ShareModel();
        model.bind(article,shareType,member);
        return Message.bind(model,request);
   }

    /**
     *    分享专栏
     */
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    @ResponseBody
    public Message topic(Long topicId,ArticleShare.ShareType shareType, Pageable pageable, HttpServletRequest request){
        Topic topic = topicService.find(topicId);
        if (topic==null) {
            return Message.error("无效专栏编号");
        }
        Member member = memberService.getCurrent();
        ShareModel model = new ShareModel();
        model.bind(topic,shareType,member);
        return Message.bind(model,request);
    }

}