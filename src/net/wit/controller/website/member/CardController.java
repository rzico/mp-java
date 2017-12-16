package net.wit.controller.website.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.CardModel;
import net.wit.entity.*;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.Sha1Util;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @ClassName: CardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberCardController")
@RequestMapping("/website/member/card")
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

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    /**
     *  我的会员卡
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        return Message.bind(CardModel.bindList(member.getCards()),request);
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
    public Message submit(String mobile,String name,Long cardId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = cardService.find(cardId);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (mobile==null) {
            return Message.error("请填写手机号");
        }
        if (name==null) {
            return Message.error("请填写真实姓名");
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
        cardService.activate(card,member);
        CardModel model = new CardModel();
        model.bind(card);

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        if (member.getMobile()==null) {
            data.put("mobile","");
        } else {
            data.put("mobile",member.getMobile());
        }
        if (member.getName()==null) {
            data.put("name", "");
        } else {
            data.put("name", member.getName());
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = StringUtils.Random6Code();
        card.setSign(String.valueOf(challege));
        cardService.update(card);
        data.put("payCode","http://"+bundle.getString("weixin.url")+"/q/818802"+card.getCode()+String.valueOf(challege)+".jhtml");

        Ticket ticket = WeixinApi.getWxCardTicket();
        HashMap<String, Object> params = new HashMap<>();
        params.put("api_ticket", ticket.getTicket());
        params.put("timestamp", WeiXinUtils.getTimeStamp());
        params.put("nonce_str", WeiXinUtils.CreateNoncestr());
        params.put("card_id", card.getTopicCard().getWeixinCardId());
        String sha1Sign1 = getCardSha1Sign(params);
        HashMap<String, Object> cardExt = new HashMap<>();
        cardExt.put("timestamp", params.get("timestamp"));
        cardExt.put("nonce_str", params.get("nonce_str"));
        cardExt.put("signature", sha1Sign1);
        data.put("cardExt",cardExt);
        data.put("cardId",card.getTopicCard().getWeixinCardId());
        //System.out.println(data);
        return Message.success(data,"激活成功");
    }

    /**
     *   获取会员卡
     */
    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(Long id,String code,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = null;
        if (id!=null) {
            card = cardService.find(id);
        } else {
            if (code != null) {
               Long shopId = null;
               if (code.substring(0,2).equals("86")) {
                   shopId = Long.parseLong(code.substring(2,11))-100000000;
                   code = null;
               } else
               if (code.substring(0,2).equals("88")) {
                   shopId = Long.parseLong(code.substring(2,11))-100000000;
                   card = cardService.find(code);
                   if (card!=null && !card.getStatus().equals(Card.Status.none) && !card.getMembers().contains(member)) {
                       return Message.error("不是空卡,不能领取");
                   }
               } else {
                   return Message.error("无效code");
               }
               Shop shop = shopService.find(shopId);
               if (shop==null) {
                   return Message.error("无效店铺 id");
               }
               Member owner = shop.getOwner();
               if (owner.getTopic()==null) {
                   return Message.error("没有开通专栏");
               }
               TopicCard topicCard = owner.getTopic().getTopicCard();
               if (topicCard==null) {
                   return Message.error("没有开通会员卡");
               }
//               if (card==null) {
//                   for (Card c : member.getCards()) {
//                       if (c.getTopicCard().equals(topicCard)) {
//                           card = c;
//                           break;
//                       }
//                   }
//               }
               if (card==null) {
                   card = cardService.create(owner.getTopic().getTopicCard(),shop, code, member);
               }
            } else {
                return Message.error("无效code");
            }
        }
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (card.getStatus().equals(Card.Status.activate)) {
            if (!card.getMembers().contains(member)) {
                return Message.error("不是本人不能打开");
            }
        }
        CardModel model = new CardModel();
        model.bind(card);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        if (member.getMobile()==null) {
            data.put("mobile","");
        } else {
            data.put("mobile",member.getMobile());
        }
        if (member.getName()==null) {
            data.put("name", "");
        } else {
            data.put("name", member.getName());
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = StringUtils.Random6Code();
        card.setSign(String.valueOf(challege));
        cardService.update(card);
        data.put("payCode","http://"+bundle.getString("weixin.url")+"/q/818802"+card.getCode()+String.valueOf(challege)+".jhtml");

        Ticket ticket = WeixinApi.getWxCardTicket();
        HashMap<String, Object> params = new HashMap<>();
        params.put("api_ticket", ticket.getTicket());
        params.put("timestamp", WeiXinUtils.getTimeStamp());
        params.put("nonce_str", WeiXinUtils.CreateNoncestr());
        params.put("card_id", card.getTopicCard().getWeixinCardId());
        String sha1Sign1 = getCardSha1Sign(params);
        HashMap<String, Object> cardExt = new HashMap<>();
        cardExt.put("timestamp", params.get("timestamp"));
        cardExt.put("nonce_str", params.get("nonce_str"));
        cardExt.put("signature", sha1Sign1);
        data.put("cardExt",cardExt);
        data.put("cardId",card.getTopicCard().getWeixinCardId());
        return Message.bind(data,request);
    }

    private String getCardSha1Sign(HashMap<String, Object> params) {
        try {
            String str1 = WeiXinUtils.signMapValue(params);
            //System.out.println(params);
            //System.out.println(str1);
            return Sha1Util.encode(str1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}