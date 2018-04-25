package net.wit.controller.website;

import com.sun.mail.util.BASE64DecoderStream;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.controller.model.TopicViewModel;
import net.wit.entity.*;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.service.*;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


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

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "friendsServiceImpl")
    private FriendsService friendsService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    /**
     * 打开首页
     * id 会员
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Long id,HttpServletRequest request,HttpServletResponse response){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.find(id);
        if (member==null) {
            member = memberService.getCurrent();
            if (member==null) {
                String url = "http://"+bundle.getString("weixin.url")+"/website/topic/index.jhtml?id="+id;
                String redirectUrl = "http://"+bundle.getString("weixin.url")+"/website/login/weixin.jhtml?redirectURL="+ StringUtils.base64Encode(url.getBytes());
                redirectUrl = URLEncoder.encode(redirectUrl);
                return "redirect:"+MenuManager.codeUrlO2(redirectUrl);
            }
        }

        String template="1001";
        Topic topic = topicService.find(member);
        if (topic!=null) {
            template = topic.getTemplate().getSn();
        }

        return "redirect:"+"http://"+bundle.getString("weixin.url")+"/#/c"+template+"?id="+id;
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
        Topic topic = topicService.find(member);
        TopicViewModel model =new TopicViewModel();
        model.bind(member,member);
        Long at = articleService.count(new Filter("member", Filter.Operator.eq,member));
        model.setArticle(at.intValue());
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));


        List<ArticleCatalog> catalogs = articleCatalogService.findList(null,filters,null);
        model.setCatalogs(ArticleCatalogModel.bindList(catalogs));
        Member self = memberService.getCurrent();
        if (self!=null) {
            MemberFollow follow = memberFollowService.find(self,member);
            model.setFollowed(follow!=null);
            Friends friends = friendsService.find(self,member);
            if (friends!=null) {
                model.setFriendStatus(friends.getStatus());
            }
        }

        filters.clear();
        filters.add(new Filter("owner", Filter.Operator.eq,member));
        List<Shop> shop = shopService.findList(0,1,filters,null);
        if (shop.size()>0) {
            model.setThumbnail(shop.get(0).getScene());
        }

        return Message.bind(model,request);
   }

}