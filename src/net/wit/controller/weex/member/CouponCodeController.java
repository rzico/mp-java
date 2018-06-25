package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.BarrelStockModel;
import net.wit.controller.model.CouponCodeModel;
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

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

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
        if (couponCode.getStock()>0) {
            couponCode.setStock(couponCode.getStock()-1L);
        }
        if (couponCode.getStock()==0) {
            couponCode.setIsUsed(true);
        }
        couponCodeService.update(couponCode);
        return Message.success("核销成功");
    }

    /**
     *  获取列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long cardId,HttpServletRequest request){
        Member owner = memberService.getCurrent();

        Admin admin = adminService.findByMember(owner);
        if (admin!=null && admin.getEnterprise()!=null) {
            owner = admin.getEnterprise().getMember();
        }

        Card card = cardService.find(cardId);
        if (card==null) {
            return Message.error("cardId 无效");
        }

        Member member = card.getMember();
        if (member==null) {
            return Message.error("没找到卡主");
        }

        List<CouponCode> models = new ArrayList<>();
        for (CouponCode c:member.getCouponCodes()) {
            if (c.getEnabled()) {
                if (c.getCoupon().getDistributor().equals(owner)) {
                    models.add(c);
                }
            }
        }
        return Message.bind(CouponCodeModel.bindList(models),request);
    }


    /**
     *  赠送
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Long couponCodeId,Integer quantity,HttpServletRequest request){
        Member owner = memberService.getCurrent();

        Admin admin = adminService.findByMember(owner);
        if (admin!=null && admin.getEnterprise()!=null) {
            owner = admin.getEnterprise().getMember();
        }

        CouponCode  couponCode = couponCodeService.find(couponCodeId);
        if (couponCode==null) {
            return Message.error("couponCodeId 无效");
        }

        if (!couponCode.getCoupon().getType().equals(Coupon.Type.exchange)) {
            return Message.error("优惠券不能赠送");
        }

        if (!couponCode.getCoupon().getDistributor().equals(owner)) {
            return Message.error("不是本店电子券");
        }


        couponCode.setStock(couponCode.getStock()+quantity);

        couponCodeService.update(couponCode);
        return Message.success("执行成功");
    }

}