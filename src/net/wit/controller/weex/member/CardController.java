package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CardModel;
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
        CardModel model = new CardModel();
        model.bind(card);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        data.put("mobile",member.getMobile());
        data.put("name",member.getName());
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = StringUtils.Random6Code();
        card.setSign(String.valueOf(challege));
        cardService.update(card);
        data.put("payCode","http://"+bundle.getString("weixin.url")+"/q/818802"+card.getCode()+String.valueOf(challege));
        return Message.bind(data,request);
    }


    /**
     *  会员卡充值
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST)
    @ResponseBody
    public Message fill(Long id, BigDecimal amount, Pageable pageable, HttpServletRequest request){
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
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (card.getStatus().equals(Card.Status.activate)) {
            return Message.error("会员卡没激活");
        }
        Enterprise enterprise = admin.getEnterprise();
        Member owner = enterprise.getMember();

        CardBill cardBill = new CardBill();
        cardBill.setDeleted(false);
        cardBill.setOwner(owner);
        cardBill.setShop(shop);
        cardBill.setCredit(amount);
        cardBill.setDebit(BigDecimal.ZERO);
        cardBill.setMemo("线下充值,操作员:"+admin.getName());
        cardBill.setMethod(CardBill.Method.offline);
        cardBill.setMember(card.getMembers().get(0));
        cardBill.setCard(card);
        cardBill.setOperator(admin.getName());
        try {
            cardBillService.fill(cardBill);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Message.error("保存失败");
        }
        CardModel model = new CardModel();
        model.bind(card);
        return Message.success(model,"充值成功");
    }


    /**
     *  会员卡退款
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    @ResponseBody
    public Message refund(Long id, BigDecimal amount, Pageable pageable, HttpServletRequest request){
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
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (card.getStatus().equals(Card.Status.activate)) {
            return Message.error("会员卡没激活");
        }
        Enterprise enterprise = admin.getEnterprise();
        Member owner = enterprise.getMember();

        CardBill cardBill = new CardBill();
        cardBill.setDeleted(false);
        cardBill.setOwner(owner);
        cardBill.setShop(shop);
        cardBill.setCredit(BigDecimal.ZERO);
        cardBill.setDebit(amount);
        cardBill.setMemo("线下退款,操作员:"+admin.getName());
        cardBill.setMethod(CardBill.Method.offline);
        cardBill.setMember(card.getMembers().get(0));
        cardBill.setCard(card);
        cardBill.setOperator(admin.getName());
        try {
            cardBillService.refund(cardBill);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Message.error("退款失败");
        }
        CardModel model = new CardModel();
        model.bind(card);
        return Message.success(model,"退款成功");
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
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Long c = 100000000+shop.getId();
        String qr = "http://"+bundle.getString("weixin.url")+"/q/818801"+"86"+String.valueOf(c);
        return Message.bind(qr,request);
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
        pageable.setFilters(filters);
        Page<Card> page = cardService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CardModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}