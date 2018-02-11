package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.MemberViewModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @ClassName: RechargeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberRechargerController")
@RequestMapping("/weex/member/recharge")
public class RechargeController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "rechargeServiceImpl")
    private RechargeService rechargeService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    /**
     * 查找用户
     */
    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseBody
    public Message find(String username,HttpServletRequest request){
        Member member = memberService.findByUsername(username);
        MemberViewModel model = new MemberViewModel();
        model.bind(member);
        return Message.bind(model,request);
    }

    /**
     * 充值申请
     */
    @RequestMapping(value = "submit")
    @ResponseBody
    public Message submit(String username,String voucher,BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getBalance().compareTo(amount) < 0) {
            return Message.error("账户余额不足");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("不是代理商");
        }
        Member user = memberService.findByUsername(username);
        if (user==null) {
            return Message.error("无效用户名");
        }
        Recharge recharge = new Recharge();
        recharge.setStatus(Recharge.Status.waiting);
        recharge.setAmount(amount);
        recharge.setVoucher(voucher);
        recharge.setFee(BigDecimal.ZERO);
        recharge.setMethod(Recharge.Method.offline);
        recharge.setMember(user);
        recharge.setAdmin(admin);
        recharge.setSn(snService.generate(Sn.Type.recharge));
        recharge.setMemo("代理商代充,"+admin.getName());
        try {
            rechargeService.agentSubmit(recharge,member);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
        return Message.success("提交成功");
    }

}