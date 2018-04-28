package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.PayBillViewModel;
import net.wit.controller.model.TransferListModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: TransferController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberAgentController")
@RequestMapping("/weex/member/agent")
public class AgentController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "transferServiceImpl")
    private TransferService transferService;

    @Resource(name = "configServiceImpl")
    private ConfigService configService;

    /**
     * 提现记录
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("promoter", Filter.Operator.in, member));
        filters.add(new Filter("status", Filter.Operator.in, Transfer.Status.waiting));
        Page<Transfer> page = transferService.findPage(null, null, pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(TransferListModel.bindList(page.getContent()));
        return Message.bind(model, request);
    }

    /**
     * 提现审核
     */
    @RequestMapping(value = "submit")
    @ResponseBody
    public Message submit(Long id, String voucher, BigDecimal amount, HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin == null) {
            return Message.error("不是代理商");
        }

        Transfer entity = transferService.find(id);
        if (!member.equals(entity.getMember().getPromoter())) {
            return Message.error("只能审核自已客户!");
        }

        if (amount.compareTo(entity.effectiveAmount()) != 0) {
            return Message.error("汇款金额不正确,请重新填写!");
        }

        entity.setVoucher(voucher);
        entity.setOperator(member.displayName());
        try {
            transferService.agentTransfer(entity);
        } catch (Exception e) {
            return Message.error("提交失败");
        }
        return Message.success("success");

    }
}