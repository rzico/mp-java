package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.WalletModel;
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
 * @ClassName: TransferController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberTransferController")
@RequestMapping("/weex/member/transfer")
public class TransferController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "transferServiceImpl")
    private TransferService transferService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "configServiceImpl")
    private ConfigService configService;

    /**
     *
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Member member = memberService.getCurrent();
        Bankcard card = bankcardService.findDefault(member);
        Map<String,String> data = new HashMap<String,String>();
        if (card!=null) {
            data.put("cardNo","尾号"+card.getCardno().substring(card.getCardno().length() - 4, card.getCardno().length())+card.getCardtype());
            data.put("bankName",card.getBankname());
        } else {
            return Message.error("没有绑定银行卡");
        }
        data.put("nickName",member.getNickName());
        return Message.bind(data,request);
    }

    private BigDecimal calculate(Member member,BigDecimal amount) {
        Config config = configService.find("transfer.fee.type");
        if (config==null) {
            if (member!=null) {
                Admin admin = adminService.findByMember(member);
                if (admin != null && admin.getEnterprise() != null) {
                    return admin.getEnterprise().getTransfer();
                }
            }
            return BigDecimal.ONE;
        } else {
            Config fee = configService.find("transfer.fee");
            return amount.multiply(fee.getBigDecimal().multiply(new BigDecimal("0.01")))
                   .setScale(2,BigDecimal.ROUND_HALF_DOWN);
        }
    }

    /**
     * 计算手续费
     */
    @RequestMapping(value = "calculate", method = RequestMethod.POST)
    @ResponseBody
    public Message calculateFee(BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();
        return Message.success(calculate(member,amount),"success");
    }

    /**
     * 获取可提现金额
     */
    @RequestMapping(value = "effectiveBalance", method = RequestMethod.POST)
    @ResponseBody
    public Message effectiveBalance(BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        BigDecimal effective = member.effectiveBalance();
        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            effective = effective.subtract(admin.getEnterprise().getCreditLine());
        }
        return Message.success(effective,"success");
    }

    /**
     * 提交提现
     */
    @RequestMapping(value = "submit")
    @ResponseBody
    public Message submit(Transfer.Type type,BigDecimal amount,HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Config config = configService.find("transfer.min");
        if (config!=null) {
            if (amount.compareTo(config.getBigDecimal())<0) {
                return Message.error("不能低于"+config.getValue()+"元");
            }
        }

        BigDecimal effective = member.effectiveBalance();
        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            effective = effective.subtract(admin.getEnterprise().getCreditLine());
        }

        if (effective.compareTo(amount) < 0) {
            return Message.error("可提现余额不足");
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Transfer transfer = new Transfer();
        Bankcard card = bankcardService.findDefault(member);
        if (Transfer.Type.bankcard.equals(type)) {
            if (card==null) {
                return Message.error("请绑定银行卡");
            }
            transfer.setBankname(card.getBankname());
            transfer.setCardno(card.getCardno());
            transfer.setCity(card.getCity());
            transfer.setName(card.getName());
            transfer.setMemo("提现到银行卡");
        } else
        if (Transfer.Type.weixin.equals(type)) {
            transfer.setBankname("微信钱包");
            BindUser bindUser = bindUserService.findMember(member,bundle.getString("app.appid"), BindUser.Type.weixin);
            if (bindUser==null) {
                return Message.error("请绑定微信号");
            }
            transfer.setCardno(bindUser.getOpenId());
            transfer.setCity("全国");
            transfer.setName(member.getNickName());
            transfer.setMemo("提现到微信钱包");
        } else {
            return Message.error("不支持的类型");
        }
        transfer.setMember(member);
        transfer.setStatus(Transfer.Status.waiting);
        transfer.setAmount(amount);
        transfer.setFee(calculate(member,amount));
        transfer.setType(type);
        transfer.setSn(snService.generate(Sn.Type.transfer));
        try {
            transferService.submit(transfer);
            return Message.success("提交成功");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }

}