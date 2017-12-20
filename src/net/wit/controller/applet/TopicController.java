package net.wit.controller.applet;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
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

    /**
     * 专栏信息
     * id 会员
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效会员编号");
        }
        TopicViewModel model =new TopicViewModel();
        model.bind(member);
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