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
        Shop shop = shopService.find(code);
        ShopModel model = new ShopModel();
        model.bind(shop);
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
            if (code.getCoupon().getDistributor().equals(shop.getOwner()) && code.getEnabled() && !code.getCoupon().getScope().equals(Coupon.Scope.mall)) {
                BigDecimal d = code.calculate(amount.subtract(noDiscount));
                if (d.compareTo(discount) > 0) {
                    couponCode = code;
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
            } else {
                card = null;
                cardDiscount = BigDecimal.ZERO;
            }
        }

        payBill.setCard(card);
        payBill.setCardDiscount(cardDiscount);

        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(shop.getEnterprise().calcFee(effective));
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
            if (amount.compareTo(BigDecimal.ZERO)<=0) {
                return Message.error("请输入付款金额");
            }
            Payment payment = payBillService.submit(payBill);
            Map<String,String> data = new HashMap<String,String>();
            data.put("sn",payment.getSn());
            if (payBill.getCardDiscount().compareTo(BigDecimal.ZERO)>0) {
                data.put("card","true");
            } else {
                data.put("card","false");
            }
            return Message.success(data,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }

}