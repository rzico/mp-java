package net.wit.controller.website.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.controller.model.CouponModel;
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
 * @ClassName: CouponController
 * @author 降魔战队  优惠券
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberCouponController")
@RequestMapping("/website/member/coupon")
public class CouponController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

     /**
     * 领取优惠券
     */
    @RequestMapping(value = "/activate")
    @ResponseBody
    public Message activate(Long id, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Coupon coupon = couponService.find(id);
        if (coupon==null) {
            return Message.error("无效优惠券id");
        }
        CouponCode couponCode = null;
        try {
            couponCode = couponCodeService.build(coupon,member);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }

        CouponCodeModel model = new CouponCodeModel();
        model.bind(couponCode);
        return Message.success(model,"保存成功");

    }

}