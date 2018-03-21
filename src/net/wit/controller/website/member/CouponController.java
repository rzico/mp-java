package net.wit.controller.website.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.controller.model.CouponModel;
import net.wit.entity.*;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private String getCardSha1Sign(HashMap<String, Object> params) {
        try {
            String str1 = WeiXinUtils.signMapValue(params);
            return Sha1Util.encode(str1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

        Map<String,Object> data = new HashMap<String,Object>();
        Member owner = couponCode.getCoupon().getDistributor();

        if (owner.getTopic()!=null && owner.getTopic().getTopicCard()!=null) {
            Ticket ticket = WeixinApi.getWxCardTicket();
            HashMap<String, Object> params = new HashMap<>();
            params.put("api_ticket", ticket.getTicket());
            params.put("timestamp", WeiXinUtils.getTimeStamp());
            params.put("nonce_str", WeiXinUtils.CreateNoncestr());
            params.put("card_id", owner.getTopic().getTopicCard().getWeixinCardId());
            String sha1Sign1 = getCardSha1Sign(params);
            HashMap<String, Object> cardExt = new HashMap<>();
            cardExt.put("timestamp", params.get("timestamp"));
            cardExt.put("nonce_str", params.get("nonce_str"));
            cardExt.put("signature", sha1Sign1);
            data.put("cardExt", cardExt);
            data.put("cardId", owner.getTopic().getTopicCard().getWeixinCardId());
        }

        CouponCodeModel model = new CouponCodeModel();
        model.bind(couponCode);
        data.put("data", model);
        return Message.success(data,"领取成功,已放入卡包");
    }

}