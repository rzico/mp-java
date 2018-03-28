package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: RechargeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberRechargeController")
@RequestMapping("/weex/member/recharge")
public class RechargeController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "rechargeServiceImpl")
    private RechargeService rechargeService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     *  提交充值
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    @ResponseBody
    public Message payment(BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();

        Recharge recharge = new Recharge();
        recharge.setFee(BigDecimal.ZERO);
        recharge.setAmount(amount);
        recharge.setMember(member);
        recharge.setMemo("钱包充值");
        recharge.setMethod(Recharge.Method.online);
        recharge.setStatus(Recharge.Status.waiting);
        recharge.setSn(snService.generate(Sn.Type.recharge));
        Payment payment = null;
        try {
            payment = rechargeService.recharge(recharge);
        } catch (Exception e) {
            return Message.error("提交失败");
        }
        if (payment==null) {
            return Message.error("提交失败");
        }
        return Message.success((Object) payment.getSn(),"发布成功");

    }

}