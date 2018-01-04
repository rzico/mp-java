package net.wit.controller.website;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.controller.model.TopicViewModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleCatalog;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteTopicController")
@RequestMapping("website/topic")
public class TopicController extends BaseController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    /**
     * 打开首页
     * id 会员
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Long id,HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            member = memberService.getCurrent();
            if (member==null) {
                return "redirect:/c1001?id=";
            }
        }
        String template="1001";
        Topic topic = topicService.find(member);
        if (topic!=null) {
            template = topic.getTemplate().getSn();
        }

        return "redirect:/c"+template+"?id="+id;
    }

    /**
     * 专栏信息
     * id 会员
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            member = memberService.getCurrent();
        }
        if (member==null) {
            return Message.error("无效会员编号");
        }
        Topic topic = topicService.find(member);
        TopicViewModel model =new TopicViewModel();
        model.bind(member);
        Long at = articleService.count(new Filter("member", Filter.Operator.eq,member));
        model.setArticle(at.intValue());
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));

        List<ArticleCatalog> catalogs = articleCatalogService.findList(null,filters,null);
        model.setCatalogs(ArticleCatalogModel.bindList(catalogs));
        return Message.bind(model,request);
   }

}