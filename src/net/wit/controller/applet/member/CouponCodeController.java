package net.wit.controller.applet.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.Sha1Util;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;


/**
 * @ClassName: CouponCodeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberCouponCodeController")
@RequestMapping("/applet/member/couponCode")
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

    /**
     * 我的优惠券
     * id 会员
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Long id,HttpServletRequest request,HttpServletResponse response){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member==null) {
            String url = "http://"+bundle.getString("weixin.url")+"/website/member/couponCode/index.jhtml?id="+id;
            String redirectUrl = "http://"+bundle.getString("weixin.url")+"/website/login/weixin.jhtml?redirectURL="+ StringUtils.base64Encode(url.getBytes());
            redirectUrl = URLEncoder.encode(redirectUrl);
            return "redirect:"+ MenuManager.codeUrlO2(redirectUrl);
        }

        return "redirect:/member/coupon?id="+id;
    }

    /**
     *  文章列表,带分页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Member owner = null;
        if (authorId!=null) {
            owner = memberService.find(authorId);
        }

        List<CouponCode> models = new ArrayList<>();
        for (CouponCode c:member.getCouponCodes()) {
            if (c.getEnabled()) {
                if (owner==null) {
                    models.add(c);
                } else
                if (c.getCoupon().getDistributor().equals(owner)) {
                    models.add(c);
                }
            }
        }
        return Message.bind(CouponCodeModel.bindList(models),request);
    }

    /**
     *   获取优惠券
     */
    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(Long id,String code,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        CouponCode couponCode = null;
        if (id!=null) {
            couponCode = couponCodeService.find(id);
        } else {
            if (code != null) {
                couponCode = couponCodeService.findByCode(code);
            } else {
                return Message.error("无效code");
            }
        }
        if (couponCode==null) {
            return Message.error("无效卡号");
        }
        CouponCodeModel model = new CouponCodeModel();
        model.bind(couponCode);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("coupon",model);
        data.put("mobile",member.getMobile());
        data.put("name",member.getName());
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        data.put("payCode","http://"+bundle.getString("weixin.url")+"/q/818803"+couponCode.getCode()+".jhtml");

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

        return Message.bind(data,request);
    }

    private String getCardSha1Sign(HashMap<String, Object> params) {
        try {
            String str1 = WeiXinUtils.signMapValue(params);
            return Sha1Util.encode(str1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}