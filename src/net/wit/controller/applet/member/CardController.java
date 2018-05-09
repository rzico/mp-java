package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CardBillModel;
import net.wit.controller.model.CardModel;
import net.wit.controller.model.CardPointBillModel;
import net.wit.entity.*;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.Sha1Util;
import net.wit.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;


/**
 * @ClassName: CardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberCardController")
@RequestMapping("/applet/member/card")
public class CardController extends BaseController {

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

    @Resource(name = "topicCardServiceImpl")
    private TopicCardService topicCardService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "cardBillServiceImpl")
    private CardBillService cardBillService;

    @Resource(name = "cardPointBillServiceImpl")
    private CardPointBillService cardPointBillService;

    /**
     * 发送验证码
     * mobile 手机号
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(String mobile,HttpServletRequest request) {
        String m = null;
        if (mobile!=null) {
            try {
                m = new String(org.apache.commons.codec.binary.Base64.decodeBase64(mobile), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(m);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_BIND_CAPTCHA, JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(m);
        smsSend.setContent("验证码:" + securityCode + ",只用于激活会员卡。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");

    }

    /**
     *  获取付款码
     */
    @RequestMapping(value = "/codepay", method = RequestMethod.POST)
    @ResponseBody
    public Message codepay(Long shopId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效门店id");
        }
        Member payee = shop.getOwner();
        Card card = null;
        for (Card c:member.getCards()) {
            if (c.getOwner().equals(payee)) {
                card = c;
                break;
            }
        }

        if (card==null) {
            return Message.error("没有会员卡");
        }

        int challege = StringUtils.Random6Code();
        card.setSign(String.valueOf(challege));
        cardService.update(card);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String payCode = "http://"+bundle.getString("weixin.url")+"/q/818802"+card.getCode()+String.valueOf(challege)+".jhtml";


        return Message.success((Object)payCode,"获取成功");
    }

     /**
     *  激活会员卡
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String mobile,String captcha,Long authorId,Long xuid,HttpServletRequest request){

        Redis redis = redisService.findKey(Member.MOBILE_BIND_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }

        redisService.remove(Member.MOBILE_BIND_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);

        try {
            captcha = new String(org.apache.commons.codec.binary.Base64.decodeBase64(captcha),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (captcha==null) {
            return Message.error("无效验证码");
        }
        if (safeKey.hasExpired()) {
            return Message.error("验证码已过期");
        }
        if (!captcha.equals(safeKey.getValue())) {
            return Message.error("无效验证码");
        }

        if (!mobile.equals(safeKey.getKey())) {
            return Message.error("手机验证无效");
        }

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        if (authorId==null) {
            authorId = Long.parseLong(bundle.getString("platform"));
        }


        Member promoter = null;
        if (xuid!=null) {
            promoter = memberService.find(xuid);
        }

        Card card = null;
        Member owner = null;

        if ("3".equals(bundle.getString("weex"))) {
            if (member.getCards().size()>0) {
               card = member.getCards().get(0);
            } else {
               Admin admin = adminService.findByMember(promoter);
               if (admin!=null && admin.getEnterprise()!=null) {
                   owner = admin.getEnterprise().getMember();
               } else {
                   return Message.error("暂不支持");
               }
            }
        } else {
            owner = memberService.find(authorId);
            card = member.card(owner);
        }

        if (card==null) {
            card = cardService.createAndActivate(member, owner, promoter, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        String name = member.getName();
        if (name==null) {
            name = member.displayName();
        }
        if (mobile!=null) {
            card.setMobile(mobile);
        } else {
            card.setMobile(member.getMobile());
        }
        if (name!=null && !"".equals(name)) {
            card.setName(name);
        } else {
            card.setName(member.getName());
        }
        card.setName(name);
        cardService.update(card);

        Map<String,Object> data = new HashMap<String,Object>();

        data.put("status", card.getStatus());
        CardModel model = new CardModel();
        model.bind(card);
        data.put("card", model);

        int challege = StringUtils.Random6Code();
        card.setSign(String.valueOf(challege));
        cardService.update(card);
        data.put("payCode", "http://" + bundle.getString("weixin.url") + "/q/818802" + card.getCode() + String.valueOf(challege) + ".jhtml");

        return Message.success(data,"激活成功");

    }

    /**
     *   获取会员卡
     */
    @RequestMapping(value = "view")
    @ResponseBody
    public Message view(Long authorId,Long id,HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card =  null;
        Member owner = null;
        if (id!=null) {
            card = cardService.find(id);
        } else
        if (authorId!=null) {
            owner = memberService.find(authorId);
            if (owner == null) {
                return Message.error("无效商家id");
            }
            card = member.card(owner);
        } else {
            if (authorId==null) {
                authorId = Long.parseLong(bundle.getString("platform"));
            }
            owner = memberService.find(authorId);
            if ("3".equals(bundle.getString("weex")) && member.getCards().size()>0) {
                card = member.getCards().get(0);
            }
        }
        Map<String,Object> data = new HashMap<String,Object>();
        if (card==null) {
            data.put("status", "none");
            if (owner!=null) {
                data.put("name", owner.topicName());
                data.put("logo", owner.getLogo());
            }
        } else {
            data.put("status", card.getStatus());
            CardModel model = new CardModel();
            model.bind(card);
            data.put("card", model);

            int challege = StringUtils.Random6Code();
            card.setSign(String.valueOf(challege));
            cardService.update(card);
            data.put("payCode", "http://" + bundle.getString("weixin.url") + "/q/818802" + card.getCode() + String.valueOf(challege) + ".jhtml");
        }
        return Message.bind(data,request);
    }

    /**
     *  账单记录
     */
    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    @ResponseBody
    public Message bill(Long id,Pageable pageable, HttpServletRequest request){
        Card card = cardService.find(id);
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("card", Filter.Operator.eq,card));
        pageable.setFilters(filters);
        Page<CardBill> page = cardBillService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CardBillModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  账单记录
     */
    @RequestMapping(value = "/point_bill", method = RequestMethod.GET)
    @ResponseBody
    public Message pointBill(Long id,Pageable pageable, HttpServletRequest request){
        Card card = cardService.find(id);
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("card", Filter.Operator.eq,card));
        pageable.setFilters(filters);
        Page<CardPointBill> page = cardPointBillService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CardPointBillModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}