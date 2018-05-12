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
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: ShippingController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */


@Controller("appletMemberShippingontroller")
@RequestMapping("/applet/member/shipping")
public class ShippingController extends BaseController {

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


}