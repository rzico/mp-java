package net.wit.controller.applet;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.controller.model.NavigationModel;
import net.wit.controller.model.TopicListModel;
import net.wit.controller.model.TopicViewModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @ClassName: TopicController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletTopicController")
@RequestMapping("applet/topic")
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

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "navigationServiceImpl")
    private NavigationService navigationService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    /**
     * 专栏信息
     * id 会员
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.find(id);
        if (member==null) {
            if ("3".equals(bundle.getString("weex")) ) {
                if (id==null) {
                    id = Long.parseLong(bundle.getString("platform"));
                }
                member = memberService.find(id);
            } else {
                return Message.error("无效会员编号");
            }
        }
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
        return Message.bind(model,request);
   }

    /**
     * 导航信息
     * id 会员
     */
    @RequestMapping(value = "/navigation", method = RequestMethod.GET)
    @ResponseBody
    public Message navigation(Long id,String template,HttpServletRequest request){
        Member member =memberService.find(id);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("owner", Filter.Operator.eq, member));

        List<Navigation> navigations = navigationService.findList(null,null,filters,null);
        if (navigations.size()>0) {
            return Message.bind(NavigationModel.bindList(navigations),request);
        }

        if (member!=null && member.getTopic()!=null && member.getTopic().getTemplate()!=null) {
            template = "c" + member.getTopic().getTemplate();
        } else {
            template = "c1001";
        }

        List<NavigationModel> data = new ArrayList<>();
        if ("c1001".equals(template)) {
            NavigationModel news = new NavigationModel();
            news.setType(Navigation.Type.news);
            news.setName("新品");
            news.setLogo("http://cdnx.rzico.com/images/news.png");
            data.add(news);
            NavigationModel promotions = new NavigationModel();
            promotions.setType(Navigation.Type.promotion);
            promotions.setName("抢购");
            promotions.setLogo("http://cdnx.rzico.com/images/promotion.png");
            data.add(promotions);
            NavigationModel dragon = new NavigationModel();
            dragon.setType(Navigation.Type.dragon);
            dragon.setName("拼团");
            dragon.setLogo("http://cdnx.rzico.com/images/dragon.png");
            data.add(dragon);
            filters = new ArrayList<>();
            filters.add(new Filter("member", Filter.Operator.eq,member));
            List<ProductCategory> categories = productCategoryService.findList(null,null,filters,null);
            for (ProductCategory productCategory:categories) {
                NavigationModel pc = new NavigationModel();
                pc.setType(Navigation.Type.product);
                pc.setName(productCategory.getName());
                pc.setLogo(productCategory.getThumbnail());
                pc.setProductCategoryId(productCategory.getId());
                data.add(pc);
            }
        } else
        if ("c1002".equals(template)) {
            NavigationModel news = new NavigationModel();
            news.setType(Navigation.Type.news);
            news.setName("新品");
            news.setLogo("http://cdnx.rzico.com/images/news.png");
            data.add(news);
            NavigationModel dragon = new NavigationModel();
            dragon.setType(Navigation.Type.dragon);
            dragon.setName("拼团");
            dragon.setLogo("http://cdnx.rzico.com/images/dragon.png");
            data.add(dragon);
            NavigationModel videos = new NavigationModel();
            videos.setType(Navigation.Type.video);
            videos.setName("视频");
            videos.setLogo("http://cdnx.rzico.com/images/video.png");
            data.add(videos);
            NavigationModel promotions = new NavigationModel();
            promotions.setType(Navigation.Type.promotion);
            promotions.setName("抢购");
            promotions.setLogo("http://cdnx.rzico.com/images/promotion.png");
            data.add(promotions);
            NavigationModel products = new NavigationModel();
            products.setType(Navigation.Type.mall);
            products.setName("商城");
            products.setLogo("http://cdnx.rzico.com/images/mall.png");
            data.add(products);
        } else
        if ("c1004".equals(template)) {
            NavigationModel videos = new NavigationModel();
            videos.setType(Navigation.Type.video);
            videos.setName("视频");
            videos.setLogo("http://cdnx.rzico.com/images/video.png");
            data.add(videos);
            NavigationModel promotions = new NavigationModel();
            promotions.setType(Navigation.Type.promotion);
            promotions.setName("抢购");
            promotions.setLogo("http://cdnx.rzico.com/images/promotion.png");
            data.add(promotions);
            NavigationModel products = new NavigationModel();
            products.setType(Navigation.Type.product);
            products.setName("宝贝");
            products.setLogo("http://cdnx.rzico.com/images/mall.png");
            products.setProductCategoryId(0L);
            data.add(products);
        } else {
            NavigationModel videos = new NavigationModel();
            videos.setType(Navigation.Type.video);
            videos.setName("视频");
            videos.setLogo("http://cdnx.rzico.com/images/video.png");
            data.add(videos);
            NavigationModel  images = new NavigationModel();
            images.setType(Navigation.Type.images);
            images.setName("图集");
            images.setLogo("http://cdnx.rzico.com/images/promotion.png");
            data.add(images);
            NavigationModel products = new NavigationModel();
            products.setType(Navigation.Type.product);
            products.setName("宝贝");
            products.setLogo("http://cdnx.rzico.com/images/mall.png");
            products.setProductCategoryId(0L);
            data.add(products);
        }
        return Message.bind(data,request);
    }

    /**
     *  专栏搜索
     *  keyword
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Message search(String keyword, Pageable pageable, HttpServletRequest request){
        if (keyword==null) {
            return Message.error("请输入关键词");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.like("name","%"+keyword+"%"));
        pageable.setFilters(filters);
        Page<Topic> page = topicService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        List<TopicListModel> data = TopicListModel.bindList(page.getContent());
        model.setData(data);
        Member self = memberService.getCurrent();
        if (self!=null) {
            for (TopicListModel m:data) {
                Member follow = memberService.find(m.getId());
                if (self.equals(follow)) {
                    m.setFollow(true);
                } else {
                    MemberFollow memberFollow = memberFollowService.find(self, follow);
                    m.setFollow(memberFollow!=null);
                }
                MemberFollow memberFollow = memberFollowService.find(follow, self);
                m.setFollowed(memberFollow!=null);
            }
        }
        return Message.bind(model,request);
    }

}