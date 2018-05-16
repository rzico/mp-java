package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.GuideModel;
import net.wit.controller.model.RoleModel;
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
 * @ClassName: GuideController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberGuideController")
@RequestMapping("/weex/member/guide")
public class GuideController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;


    /**
     *  开通专栏
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Member member = memberService.getCurrent();
        GuideModel model = new GuideModel();
        Admin admin = adminService.findByMember(member);
        model.setSteped1(admin!=null && admin.getEnterprise()!=null);

        if (model.getSteped1()) {
            member = admin.getEnterprise().getMember();
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("owner", Filter.Operator.eq,member));

        Bankcard card = bankcardService.findDefault(member);

        model.setSteped2(card!=null);

        //去掉第3部店铺信息
//        List<Shop> shops = shopService.findList(null,null,filters,null);
//        model.setSteped3(shops.size()>0);

        Topic topic = member.getTopic();
        model.setSteped3(topic!=null && topic.getStatus().equals(Topic.Status.success));

        model.setSteped4(topic!=null && topic.getConfig() !=null && topic.getConfig().getAppetAppId()!=null);
        return Message.bind(model,request);
    }

}