package net.wit.controller.weex.agent;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.NoticeModel;
import net.wit.controller.model.RebateModel;
import net.wit.controller.weex.agent.model.AgentModel;
import net.wit.entity.*;
import net.wit.entity.summary.RebateSummary;
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

    @Resource(name = "rebateServiceImpl")
    private RebateService rebateService;

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
     *   获取代理明累
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable,HttpServletRequest request) {
        Member member = memberService.getCurrent();

        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("不是代理商");
        }

        Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return Message.error("不是代理商");
        }

        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("enterprise", Filter.Operator.eq, enterprise));

        Page<RebateSummary> page = rebateService.sumPage(null,null,enterprise,pageable);

        return Message.bind(AgentModel.bindList(page.getContent()),request);

    }
        /**
         *   获取代理汇总
         */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message create(Long xuid,HttpServletRequest request) {
        Member member = memberService.getCurrent();

        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null) {
            Enterprise enterprise = admin.getEnterprise();
            if (enterprise!=null) {
                if (enterprise.getParent()!=null) {
                    return Message.error("你已经是代理商");
                }
            }
        }

        Member parent = memberService.find(xuid);
        if (parent==null) {
            return Message.error("无效邀请人");
        }

        Admin adminParent = adminService.findByMember(member);
        if (adminParent==null) {
            return Message.error("无效邀请人");
        }
        Enterprise enterpriseParent = admin.getEnterprise();
        if (enterpriseParent==null) {
            return Message.error("无效邀请人");
        }


        enterpriseService.createAgent(member,enterpriseParent);

        return Message.error("");


    }

}