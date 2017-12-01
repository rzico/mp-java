package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.ArticleModel;
import net.wit.controller.model.CardActivityModel;
import net.wit.controller.model.TopicIndexModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: TopicCardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberTopicCardController")
@RequestMapping("/weex/member/topiccard")
public class TopicCardController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "categoryServiceImpl")
    private CategoryService categoryService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    @Resource(name = "topicCardServiceImpl")
    private TopicCardService topicCardService;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    @Resource(name = "topicBillServiceImpl")
    private TopicBillService topicBillService;

     /**
     *  获取设置活动
     */
    @RequestMapping(value = "/activity",method = RequestMethod.GET)
    @ResponseBody
    public Message activity(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Topic topic = member.getTopic();
        if (topic==null) {
            return Message.error("没有开通专栏");
        }
        TopicCard topicCard = topic.getTopicCard();
        if (topicCard==null) {
            return Message.error("没有开通会员卡");
        }

        List<CardActivityModel> activitys = new ArrayList<CardActivityModel>();
        if (topicCard.getActivity()!=null) {
            activitys = JsonUtils.toObject(topicCard.getActivity(), List.class);
        }

        return Message.success(activitys,"获取成功");

    }

    /**
     *  设置活动
     */
    @RequestMapping(value = "/activity",method = RequestMethod.POST)
    @ResponseBody
    public Message activity(String body,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Topic topic = member.getTopic();
        if (topic==null) {
            return Message.error("没有开通专栏");
        }
        TopicCard topicCard = topic.getTopicCard();
        if (topicCard==null) {
            return Message.error("没有开通会员卡");
        }

        List<CardActivityModel> activitys = JsonUtils.toObject(body,List.class);

        topicCard.setActivity(JsonUtils.toJson(activitys));
        topicCardService.update(topicCard);
        return Message.success(activitys,"获取成功");

    }

}