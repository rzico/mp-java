package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.*;
import net.wit.entity.*;
import net.wit.plat.weixin.pojo.Ticket;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.JsonUtils;
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

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

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
        data.put("payCode","http://"+bundle.getString("weixin.url")+"/q/818802"+card.getCode()+String.valueOf(challege)+".jhtml");
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
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getOwner().equals(admin.getEnterprise().getMember())) {
            return Message.error("不是本店会员卡");
        }
        CardViewModel model = new CardViewModel();
        model.bind(card);

        Member cardMember = card.getMembers().get(0);
        Bankcard bankcard = bankcardService.findDefault(cardMember);
        if (bankcard!=null) {
            model.setName(bankcard.getName());
            model.setBindName(true);
        }

        if (cardMember.getMobile()!=null) {
            model.setBindMobile(true);
            model.setMobile(cardMember.getMobile());
        }

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        TopicCard topicCard = card.getTopicCard();
        data.put("description",topicCard.getDescription());
        data.put("prerogative",topicCard.getPrerogative());
        return Message.bind(data,request);
    }

    /**
     *    通过卡号获取会员卡信息
     */

    @RequestMapping(value = "/infobycode")
    @ResponseBody
    public Message info(String code,HttpServletRequest request){
        if (code==null) {
            return Message.error("无效卡号");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Card card = cardService.find(code);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getOwner().equals(admin.getEnterprise().getMember())) {
            return Message.error("不是本店会员卡");
        }
        CardViewModel model = new CardViewModel();
        model.bind(card);


        Member cardMember = card.getMembers().get(0);
        Bankcard bankcard = bankcardService.findDefault(cardMember);
        if (bankcard!=null) {
            model.setName(bankcard.getName());
            model.setBindName(true);
        }

        if (cardMember.getMobile()!=null) {
            model.setBindMobile(true);
            model.setMobile(cardMember.getMobile());
        }

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("card",model);
        TopicCard topicCard = card.getTopicCard();
        data.put("description",topicCard.getDescription());
        data.put("prerogative",topicCard.getPrerogative());
        return Message.bind(data,request);
    }

    /**
     *   设置会员卡属性
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Message update(Long id,Card.VIP vip,String name,String mobile,Long shopId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getOwner().equals(admin.getEnterprise().getMember())) {
            return Message.error("不是本店会员卡");
        }
        if (vip!=null) {
            card.setVip(vip);
        }
        if (name!=null) {
            card.setName(name);
        }
        if (mobile!=null) {
            card.setMobile(mobile);
        }
        if (shopId!=null) {
            Shop shop = shopService.find(shopId);
            if (shop==null) {
                return Message.error("无效店铺 id");
            }
            card.setShop(shop);
        }

        cardService.update(card);
        return Message.success("更新成功");
    }

    // 自定义比较器：按书的价格排序
    static class AmountComparator implements Comparator {
        public int compare(Object object1, Object object2) {// 实现接口中的方法
            Map<String, Object> p1 = (Map<String, Object>) object1; // 强制转换
            Map<String, Object> p2 = (Map<String, Object>) object2;
            return new BigDecimal(p1.get("amount").toString()).compareTo(new BigDecimal(p2.get("amount").toString()));
        }
    }

    private BigDecimal calculate(Shop shop,BigDecimal amount) {
        Member owner = shop.getOwner();
        Topic topic = owner.getTopic();

        if (topic==null) {
            return BigDecimal.ZERO;
        }

        if (topic.getTopicCard()==null) {
            return BigDecimal.ZERO;
        }

        if (topic.getTopicCard().getActivity()==null) {
            return BigDecimal.ZERO;
        }

        List<Map<String, Object>> activitys = JsonUtils.toObject(topic.getTopicCard().getActivity(),List.class);
        Collections.sort(activitys, new AmountComparator());

        Map<String, Object> curr = null;
        for (Map<String, Object> model:activitys) {
            if (new BigDecimal(model.get("amount").toString()).compareTo(amount)>0) {
                break;
            } else {
                curr = model;
            }
        }

        if (curr!=null) {
            return new BigDecimal(curr.get("present").toString());
        } else {
            return BigDecimal.ZERO;
        }

    }

    /**
     * 计算手续费
     */
    @RequestMapping(value = "calculate")
    @ResponseBody
    public Message calculateFee(Long shopId,BigDecimal amount,HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        return Message.success(calculate(shop,amount),"success");
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
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
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
        BigDecimal present = calculate(shop,amount);
        payBill.setCardAmount(amount.add(present));
        payBill.setNoDiscount(BigDecimal.ZERO);
        payBill.setCouponCode(null);
        payBill.setCouponDiscount(BigDecimal.ZERO);
        payBill.setCardDiscount(BigDecimal.ZERO);
        payBill.setMethod(PayBill.Method.online);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setAdmin(admin);
        payBill.setCard(card);
        payBill.setEnterprise(shop.getEnterprise());
        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(shop.getEnterprise().calcFee(effective));
        try {
            if (amount.compareTo(BigDecimal.ZERO)<=0) {
                return Message.error("请输入充值金额");
            }
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
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
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
        payBill.setMethod(PayBill.Method.offline);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setAdmin(admin);
        payBill.setCard(card);
        payBill.setEnterprise(shop.getEnterprise());
        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(shop.getEnterprise().calcFee(effective));
        try {
            if (amount.compareTo(BigDecimal.ZERO)<=0) {
                return Message.error("请输入退款金额");
            }
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
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Shop shop = admin.getShop();
        if (shop==null) {
            return Message.error("没有分配门店");
        }
        Enterprise enterprise = admin.getEnterprise();
        Member owner = enterprise.getMember();
        if (owner.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        if (owner.getTopic().getTopicCard()==null) {
            return Message.error("没有开通会员卡");
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> data = new HashMap<String,String>();
        data.put("logo",owner.getTopic().getLogo());
        data.put("name",owner.getTopic().getName());
        data.put("prerogative",owner.getTopic().getTopicCard().getPrerogative());
        data.put("description",owner.getTopic().getTopicCard().getDescription());
        Long c = 100000000+shop.getId();
        String qr = "http://"+bundle.getString("weixin.url")+"/q/818801"+"86"+String.valueOf(c)+".jhtml";
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
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return Message.error("没有开通店铺");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Member owner = enterprise.getMember();
        if (owner.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        if (owner.getTopic().getTopicCard()==null) {
            return Message.error("没有开通会员卡");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("owner", Filter.Operator.eq,owner));
        if (!admin.getRoles().contains(roleService.find(1L))) {
            Shop shop = admin.getShop();
            if (shop!=null) {
                filters.add(new Filter("shop", Filter.Operator.eq,shop));
            } else {
                Page<Card> page = new Page();
                PageBlock model = PageBlock.bind(page);
                model.setData(CardViewModel.bindList(page.getContent()));
                return Message.bind(model,request);
            }
        }
        filters.add(new Filter("status", Filter.Operator.ne,Card.Status.none));
        pageable.setFilters(filters);
        Page<Card> page = cardService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        List<CardViewModel> cardList = CardViewModel.bindList(page.getContent());
        for (CardViewModel c:cardList) {
            Card card = cardService.find(c.getId());
            Member cardMember = card.getMembers().get(0);
            Bankcard bankcard = bankcardService.findDefault(cardMember);
            if (bankcard!=null) {
                c.setName(bankcard.getName());
                c.setBindName(true);
            }

            if (cardMember.getMobile()!=null) {
                c.setMobile(cardMember.getMobile());
                c.setBindMobile(true);
            }
        }

        model.setData(cardList);
        return Message.bind(model,request);
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

}