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

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

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

    private BigDecimal calculate(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal(5000))>=0) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.ONE;
        }
    }

    /**
     * 计算手续费
     */
    @RequestMapping(value = "calculate", method = RequestMethod.POST)
    @ResponseBody
    public Message calculateFee(BigDecimal amount,HttpServletRequest request){
        return Message.success(calculate(amount),"success");
    }

    /**
     *
     */
    @RequestMapping(value = "submit")
    @ResponseBody
    public Message submit(Transfer.Type type,BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getBalance().compareTo(amount) < 0) {
            return Message.error("账户余额不足");
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
        transfer.setFee(calculate(amount));
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