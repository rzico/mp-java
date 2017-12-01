package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CardModel;
import net.wit.controller.model.CardViewModel;
import net.wit.controller.model.CouponModel;
import net.wit.entity.*;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: CardController
 * @author 降魔战队  会员卡
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberCardController")
@RequestMapping("/weex/member/card")
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

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "cardBillServiceImpl")
    private CardBillService cardBillService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    /**
     *   获取会员卡
     */
    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getMembers().contains(member)) {
            return Message.error("不是本人不能打开");
        }
        CardModel model = new CardModel();
        model.bind(card);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        TopicCard topicCard = card.getTopicCard();
        data.put("mobile",member.getMobile());
        data.put("name",member.getName());
        data.put("description",topicCard.getDescription());
        data.put("prerogative",topicCard.getPrerogative());
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = StringUtils.Random6Code();
        card.setSign(String.valueOf(challege));
        cardService.update(card);
        data.put("payCode","http://"+bundle.getString("weixin.url")+"/q/818802"+card.getCode()+String.valueOf(challege));
        return Message.bind(data,request);
    }

    /**
     *   获取会员卡
     */
    @RequestMapping(value = "/info")
    @ResponseBody
    public Message info(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        CardViewModel model = new CardViewModel();
        model.bind(card);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        TopicCard topicCard = card.getTopicCard();
        data.put("description",topicCard.getDescription());
        data.put("prerogative",topicCard.getPrerogative());
        return Message.bind(data,request);
    }


    /**
     *  提交收款
     *  id amount
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST)
    @ResponseBody
    public Message fill(Long id,BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通收银台");
        }
        Shop shop = admin.getShop();
        if (shop==null) {
            return Message.error("没有分配门店");
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getOwner().equals(shop.getOwner())) {
            return Message.error("不是本店会员卡");
        }
        if (!card.getStatus().equals(Card.Status.activate)) {
            return Message.error("会员卡没激活");
        }
        PayBill payBill = new PayBill();
        payBill.setType(PayBill.Type.card);
        payBill.setAmount(amount);
        payBill.setCardAmount(amount);
        payBill.setNoDiscount(BigDecimal.ZERO);
        payBill.setCouponCode(null);
        payBill.setCouponDiscount(BigDecimal.ZERO);
        payBill.setCardDiscount(BigDecimal.ZERO);
        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(effective.multiply(shop.getEnterprise().getBrokerage()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
        payBill.setMethod(PayBill.Method.online);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setAdmin(admin);
        payBill.setCard(card);
        payBill.setEnterprise(shop.getEnterprise());
        try {
            Payment payment = payBillService.cardFill(payBill);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id",payBill.getId());
            data.put("sn",payment.getSn());
            return Message.success(data,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }

    /**
     *  会员卡退款
     */
    @RequestMapping(value = "/refund")
    @ResponseBody
    public Message refund(Long id, BigDecimal amount, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通收银台");
        }
        Shop shop = admin.getShop();
        if (shop==null) {
            return Message.error("没有分配门店");
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getOwner().equals(shop.getOwner())) {
            return Message.error("不是本店会员卡");
        }
        if (!card.getStatus().equals(Card.Status.activate)) {
            return Message.error("会员卡没激活");
        }
        PayBill payBill = new PayBill();
        payBill.setType(PayBill.Type.cardRefund);
        payBill.setAmount(BigDecimal.ZERO.subtract(amount));
        payBill.setCardAmount(BigDecimal.ZERO.subtract(amount));
        payBill.setNoDiscount(BigDecimal.ZERO);
        payBill.setCouponCode(null);
        payBill.setCouponDiscount(BigDecimal.ZERO);
        payBill.setCardDiscount(BigDecimal.ZERO);
        payBill.setFee(BigDecimal.ZERO);
        payBill.setMethod(PayBill.Method.offline);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setAdmin(admin);
        payBill.setCard(card);
        payBill.setEnterprise(shop.getEnterprise());
        try {
            Refunds refunds = payBillService.cardRefund(payBill);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id",payBill.getId());
            data.put("sn",refunds.getSn());
            return Message.success(data,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }

    /**
     *  发卡二维码
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    @ResponseBody
    public Message qrcode(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        Shop shop = admin.getShop();
        if (shop==null) {
            return Message.error("没有分配门店");
        }
        if (member.getTopic().getTopicCard()==null) {
            return Message.error("没有开通会员卡");
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> data = new HashMap<String,String>();
        data.put("logo",member.getTopic().getLogo());
        data.put("name",member.getTopic().getName());
        data.put("prerogative",member.getTopic().getTopicCard().getPrerogative());
        data.put("description",member.getTopic().getTopicCard().getDescription());
        Long c = 100000000+shop.getId();
        String qr = "http://"+bundle.getString("weixin.url")+"/q/818801"+"86"+String.valueOf(c);
        data.put("qrcode",qr);
        return Message.bind(data,request);
    }

    /**
     *  会员卡
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        Enterprise enterprise = admin.getEnterprise();
        Member owner = enterprise.getMember();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("owner", Filter.Operator.eq,owner));
        filters.add(new Filter("status", Filter.Operator.ne,Card.Status.none));
        pageable.setFilters(filters);
        Page<Card> page = cardService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CardViewModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}