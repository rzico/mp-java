package net.wit.controller.website;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.controller.model.PayBillModel;
import net.wit.controller.model.ShopModel;
import net.wit.controller.model.TopicViewModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: PayBillController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websitePayBillController")
@RequestMapping("website/paybill")
public class PayBillController extends BaseController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    /**
     * 获取商家信息
     * id shop id
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(String code,HttpServletRequest request){
        return "redirect:/paybill/#/?code="+code;
    }

     /**
     * 获取商家信息
     * id shop id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(String code,HttpServletRequest request){
        if (code==null) {
            return Message.error("无效收钱码");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("code", Filter.Operator.eq,code));
        List<Shop> sps = shopService.findList(null,filters,null);
        if (sps.size()==0) {
            return Message.error("无效收钱码");
        }
        if (sps.size()>1) {
            return Message.error("重复绑定，无效码");
        }
        ShopModel model = new ShopModel();
        model.bind(sps.get(0));
        return Message.bind(model,request);
   }

    /**
     * 计算费用
     * id shop id
     */
    private PayBill calculate(Member member,Shop shop,BigDecimal amount,BigDecimal noDiscount){
        if (amount==null) {
            amount = BigDecimal.ZERO;
        }
        if (noDiscount==null) {
            noDiscount = BigDecimal.ZERO;
        }
        PayBill payBill = new PayBill();
        CouponCode couponCode = null;
        List<CouponCode> couponCodes = member.getCouponCodes();
        BigDecimal discount = BigDecimal.ZERO;
        for (CouponCode code:couponCodes) {
            if (code.getCoupon().getDistributor().equals(shop.getOwner()) && code.getEnabled()) {
                BigDecimal d = couponCode.calculate(amount.subtract(noDiscount));
                if (d.compareTo(discount) > 0) {
                    couponCode = couponCode;
                    discount = d;
                }
            }
        }
        Member owner = shop.getOwner();
        Map<String,Object> data =new HashMap<String,Object>();
        payBill.setAmount(amount);
        payBill.setNoDiscount(noDiscount);
        payBill.setCouponCode(couponCode);
        payBill.setCouponDiscount(discount);
        Card card = null;
        BigDecimal cardDiscount = BigDecimal.ZERO;
        for (Card c:member.getCards()) {
            if (c.getOwner().equals(owner)) {
               card = c;
               break;
            }
        }
        if (card!=null) {
            if (card.getBalance().compareTo(amount) > 0) {
                cardDiscount = amount;
                card = null;
            } else {
                cardDiscount = BigDecimal.ZERO;
            }
        }

        payBill.setCouponCode(couponCode);
        payBill.setCouponDiscount(discount);
        payBill.setCard(card);
        payBill.setCardDiscount(cardDiscount);

        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(effective.multiply(shop.getEnterprise().getBrokerage()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
        return payBill;
    }

    /**
     * 获取商家信息
     * id shop id
     */
    @RequestMapping(value = "/calculate")
    @ResponseBody
    public Message calculateF(Long shopId,BigDecimal amount,BigDecimal noDiscount,HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效店铺");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        PayBill payBill = calculate(member,shop,amount,noDiscount);
        PayBillModel model = new PayBillModel();
        model.bind(payBill);
        return Message.success(model,"success");
    }

    /**
     *  提交付款
     *  shopid
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long shopId,BigDecimal amount,BigDecimal noDiscount,HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效店铺");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        PayBill payBill = calculate(member,shop,amount,noDiscount);
        payBill.setCardAmount(amount);
        payBill.setType(PayBill.Type.cashier);
        payBill.setMethod(PayBill.Method.online);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setEnterprise(shop.getEnterprise());
        try {
            Payment payment = payBillService.submit(payBill);
            return Message.success((Object)payment.getSn() ,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }



}