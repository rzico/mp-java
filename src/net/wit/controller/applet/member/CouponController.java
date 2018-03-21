package net.wit.controller.applet.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: CouponController
 * @author 降魔战队  优惠券
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberCouponController")
@RequestMapping("/applet/member/coupon")
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
    public Message activate(Long id,Long couponCodeId,Long xuid, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        CouponCode couponCode = null;
        if (couponCodeId!=null) {
            couponCode = couponCodeService.find(couponCodeId);
            if (couponCode==null) {
                return Message.error("无效优惠券id");
            }
            if (xuid==null) {
                return Message.error("请传入推荐人");
            }
            Member x = memberService.find(xuid);
            if (!couponCode.getMember().equals(x)) {
                return Message.error("已经被领取");
            }
            couponCode.setMember(member);
            couponCodeService.update(couponCode);

        } else {
            Coupon coupon = couponService.find(id);
            if (coupon == null) {
                return Message.error("无效优惠券id");
            }
            try {
                couponCode = couponCodeService.build(coupon, member);
            } catch (Exception e) {
                return Message.error(e.getMessage());
            }
        }
        CouponCodeModel model = new CouponCodeModel();
        model.bind(couponCode);
        return Message.success(model,"领取成功,已放入卡包");

    }

}