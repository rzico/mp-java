package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CashierModel;
import net.wit.controller.model.WalletModel;
import net.wit.entity.*;
import net.wit.entity.summary.PayBillShopSummary;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: CashierController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberCashierController")
@RequestMapping("/weex/member/cashier")
public class CashierController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "transferServiceImpl")
    private TransferService transferService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    /**
     * 收银台
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Date d = DateUtils.truncate(new Date(), Calendar.DATE);
        Date y = DateUtils.addDays(d,-1);
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
            return Message.error("没分配门店");
        }
        List<PayBillShopSummary> dsum = payBillService.sumPage(shop,d,d);
        List<PayBillShopSummary> ysum = payBillService.sumPage(shop,y,y);
        CashierModel model = new CashierModel();
        model.setShopId(shop.getId());
        model.setToday(BigDecimal.ZERO);
        model.setYesterday(BigDecimal.ZERO);
        for (PayBillShopSummary s:dsum) {
            model.setToday(model.getToday().add(s.getAmount().subtract(s.getCouponDiscount())) );
        }
        for (PayBillShopSummary s:ysum) {
            model.setYesterday(s.getAmount().subtract(s.getCouponDiscount()) );
        }
        return Message.bind(model,request);
    }

    /**
     *  提交收款
     *  shopid
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long shopId,BigDecimal amount,HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效店铺");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通收银台");
        }
        PayBill payBill = new PayBill();
        payBill.setType(PayBill.Type.cashier);
        payBill.setAmount(amount);
        payBill.setCardAmount(amount);
        payBill.setNoDiscount(BigDecimal.ZERO);
        payBill.setCouponCode(null);
        payBill.setCouponDiscount(BigDecimal.ZERO);
        payBill.setCard(null);
        payBill.setCardDiscount(BigDecimal.ZERO);
        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(effective.multiply(shop.getEnterprise().getBrokerage()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
        payBill.setMethod(PayBill.Method.online);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setAdmin(admin);
        payBill.setEnterprise(shop.getEnterprise());
        try {
            Payment payment = payBillService.submit(payBill);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id",payBill.getId());
            data.put("sn",payment.getSn());
            return Message.success(data,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }


 }