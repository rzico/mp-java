package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ManagerModel;
import net.wit.entity.Admin;
import net.wit.entity.Enterprise;
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
import java.util.List;


/**
 * @ClassName: MemberController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberManagerController")
@RequestMapping("/weex/member/manager")
public class ManagerController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "occupationServiceImpl")
    private OccupationService occupationService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    /**
     * 获取管理界面首页状态
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        ManagerModel model =new ManagerModel();
        model.bind(member);

        Admin admin = adminService.findByMember(member);
        //有企业，以所在企主为
        if (admin!=null && admin.getEnterprise()!=null) {
            Enterprise ent = admin.getEnterprise();
            Topic topic = ent.getMember().getTopic();
            model.setIsShop(true);
            if (topic.getStatus().equals(Topic.Status.success)) {
                model.setActivated(true);
            } else {
                model.setActivated(false);
            }

            model.setHasTopic(ent.getMember().getTopic()!=null);

            if (topic!=null) {
                if (topic == null) {
                    model.setTopic("未开通");
                } else {
                    if (topic.getStatus().equals(Topic.Status.waiting)) {
                        model.setTopic("待点亮");
                    }
                    if (topic.getStatus().equals(Topic.Status.success)) {
                        model.setTopic("已认证");
                    }
                    if (topic.getStatus().equals(Topic.Status.failure)) {
                        model.setTopic("已过期");
                    }
                }
            }

            if (!ent.getType().equals(Enterprise.Type.shop) && ent.getStatus().equals(Enterprise.Status.success)) {
                model.setIsAgent(true);
            }
            Long shops = shopService.count(new Filter("enterprise", Filter.Operator.eq,admin.getEnterprise()));
            model.setHasShop(shops>0L);

        }

        return Message.bind(model,request);
   }

}