package net.wit.controller.weex.agent;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.NoticeModel;
import net.wit.controller.weex.agent.model.AgentModel;
import net.wit.entity.Admin;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.Notice;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ClassName: AgentController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexAgentController")
@RequestMapping("/weex/agent")
public class AgentController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    /**
     *   获取代理汇总
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Pageable pageable,HttpServletRequest request) {
        Member member = memberService.getCurrent();

        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("不是代理商");
        }

        Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return Message.error("不是代理商");
        }

        List<Filter> filters = pageable.getFilters();
        Filter filter = null;
        if (enterprise.getType().equals(Enterprise.Type.operate)) {
            filter = new Filter("operate", Filter.Operator.eq, enterprise);
        } else
        if (enterprise.getType().equals(Enterprise.Type.agent)) {
            filter = new Filter("agent", Filter.Operator.eq, enterprise);
        } else
        {
            filter = new Filter("personal", Filter.Operator.eq, enterprise);
        }

        Long ms = memberService.count(filter);

        AgentModel model = new AgentModel();
        model.setMembers(ms);
        model.setId(member.getId());
        model.setNickName(member.displayName());
        model.setLogo(member.getLogo());


        return Message.bind(model,request);
    }

    /**
     *   获取代理汇总
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message create(Pageable pageable,HttpServletRequest request) {
        return Message.error("");
    }

}