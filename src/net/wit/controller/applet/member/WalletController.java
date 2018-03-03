package net.wit.controller.applet.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.WalletModel;
import net.wit.entity.Bankcard;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: WalletController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */


@Controller("makeyMemberWalletController")
@RequestMapping("/makey/member/wallet")
public class WalletController extends BaseController {

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
        WalletModel model = new WalletModel();
        model.bind(member);
        Bankcard card = bankcardService.findDefault(member);
        if (card!=null) {
            model.setBinded(true);
            model.setBankinfo(card.getBankname() + "(" + card.getCardno().substring(card.getCardno().length() - 4, card.getCardno().length()) + ")");
        } else {
            model.setBankinfo("未绑定");
            model.setBinded(false);
        }
        return Message.bind(model,request);
    }

}