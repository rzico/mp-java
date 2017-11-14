package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.TopicIndexModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberTopicController")
@RequestMapping("/weex/member/topic")
public class TopicController extends BaseController {

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

     /**
     *  开通专栏
     */
    @RequestMapping(value = "/submit")
    @ResponseBody
    public Message submit(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Topic topic = new Topic();
        topic.setName(member.getNickName());
        topic.setBrokerage(new BigDecimal("0.6"));
        topic.setStatus(Topic.Status.waiting);
        topic.setHits(0L);
        topic.setMember(member);
        topic.setFee(new BigDecimal("588"));
        topic.setLogo(member.getLogo());
        topic.setType(Topic.Type.personal);
        TopicConfig config = topic.getConfig();
        if (config==null) {
            config = new TopicConfig();
            config.setUseCard(false);
            config.setUseCashier(false);
            config.setUseCoupon(false);
        }
        topic.setConfig(config);
        Calendar calendar   =   new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.WEEK_OF_MONTH, 1);
        topic.setExpire(calendar.getTime());
        topic.setTemplate(templateService.findDefault(Template.Type.topic));
        topicService.create(topic);
        return Message.success("发布成功");

    }

    /**
     *  修改专栏
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Message submit(String name,String logo,Long categoryId,Long areaId,String address,Long templateId,Boolean useCoupon,Boolean useCard,Boolean useCashier,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Topic topic = member.getTopic();
        if (topic==null) {
            return Message.error("请先开通专栏");
        }
        if (name!=null) {
            topic.setName(member.getNickName());
        }
        if (logo!=null) {
            topic.setLogo(member.getLogo());
        }
        if (areaId!=null) {
            topic.setArea(areaService.find(areaId));
        }
        if (categoryId!=null) {
            topic.setCategory(categoryService.find(categoryId));
        }
        if (address!=null) {
            topic.setAddress(address);
        }
        if (templateId!=null) {
            topic.setTemplate(templateService.find(templateId));
        }
        TopicConfig config = topic.getConfig();
        if (config==null) {
            config = new TopicConfig();
            config.setUseCard(false);
            config.setUseCashier(false);
            config.setUseCoupon(false);
        }
        if (useCard!=null) {
            if (member.getMobile()==null) {
                return Message.error("绑定手机才能开通");
            }
            if (member.getName()==null) {
                return Message.error("银行卡实名才能开通");
            }
            config.setUseCard(useCard);
            if (useCard) {
                enterpriseService.create(topic);
                topicCardService.create(topic);
            }
        }
        if (useCoupon!=null) {
            if (member.getMobile()==null) {
                return Message.error("绑定手机才能开通");
            }
            if (member.getName()==null) {
                return Message.error("银行卡实名才能开通");
            }
            config.setUseCoupon(useCoupon);
            if (useCoupon) {
                enterpriseService.create(topic);
            }
        }
        if (useCashier!=null) {
            if (member.getMobile()==null) {
                return Message.error("绑定手机才能开通");
            }
            if (member.getName()==null) {
                return Message.error("银行卡实名才能开通");
            }
            config.setUseCashier(useCashier);
            if (useCashier) {
               enterpriseService.create(topic);
            }
        }
        topic.setConfig(config);
        topicService.update(topic);
        return Message.success("修改成功");

    }

    /**
     *  获取专栏信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Topic topic = member.getTopic();
        if (topic==null) {
            topic = new Topic();
            topic.setName(member.getNickName());
            topic.setBrokerage(new BigDecimal("0.6"));
            topic.setStatus(Topic.Status.waiting);
            topic.setHits(0L);
            topic.setMember(member);
            topic.setFee(new BigDecimal("388"));
            topic.setLogo(member.getLogo());
            topic.setType(Topic.Type.individual);
        }
        TopicIndexModel model = new TopicIndexModel();
        model.bind(topic);
        return Message.success(model,"发布成功");

    }


}