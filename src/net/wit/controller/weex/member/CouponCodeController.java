package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.entity.Admin;
import net.wit.entity.CouponCode;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.Sha1Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @ClassName: CouponCodeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberCouponCodeController")
@RequestMapping("/weex/member/couponCode")
public class CouponCodeController extends BaseController {

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

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  核销优惠券
     */
    @RequestMapping(value = "/use")
    @ResponseBody
    public Message use(String code,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        CouponCode couponCode = couponCodeService.findByCode(code);
        if (couponCode==null) {
            return Message.error("无效卡号");
        }
        if (couponCode.getIsUsed()) {
            return Message.error("已经使用了");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有绑定门店");
        }
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise.getMember().equals(couponCode.getCoupon().getDistributor())) {
            return Message.error("不是本店优惠券");
        }
        couponCode.setUsedDate(new Date());
        couponCode.setIsUsed(true);
        couponCodeService.update(couponCode);
        return Message.success("核销成功");
    }

}