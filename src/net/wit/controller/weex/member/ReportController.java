package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.Page;
import net.wit.PageBlock;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.BarrelModel;
import net.wit.controller.model.CouponModel;
import net.wit.entity.*;
import net.wit.entity.summary.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: ReportController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberReportController")
@RequestMapping("/weex/member/report")
public class ReportController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "barrelServiceImpl")
    private BarrelService barrelService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "shippingBarrelServiceImpl")
    private ShippingBarrelService shippingBarrelService;

    @Resource(name = "shippingItemServiceImpl")
    private ShippingItemService shippingItemService;

    @Resource(name = "shippingServiceImpl")
    private ShippingService shippingService;

    /**
     *  订单统计表
     */
    @RequestMapping(value = "/order_summary", method = RequestMethod.GET)
    @ResponseBody
    public Message orderSummary(Date beginDate, Date endDate, Pageable pageable, HttpServletRequest request){

        Member member = memberService.getCurrent();

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
         }

        List<OrderSummary> header = orderService.summary(member,beginDate,endDate,pageable);
        List<OrderItemSummary> body = orderItemService.summary(member,beginDate,endDate,pageable);

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("summary",header);
        data.put("data",body);
        PageBlock model = PageBlock.bind(new Page());
        model.setData(data);
        return Message.bind(model,request);
    }


    /**
     *  收款计表
     */
    @RequestMapping(value = "/payment_summary", method = RequestMethod.GET)
    @ResponseBody
    public Message depositSummary(Date beginDate, Date endDate, Pageable pageable, HttpServletRequest request){

        Member member = memberService.getCurrent();

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
        }

        List<PaymentSummary> header = paymentService.summary_method(member,beginDate,endDate,pageable);
        List<PaymentSummary> body = paymentService.summary(member,beginDate,endDate,pageable);

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("summary",header);
        data.put("data",body);
        PageBlock model = PageBlock.bind(new Page());
        model.setData(data);
        return Message.bind(model,request);
    }


    /**
     *  桶结算表
     */
    @RequestMapping(value = "/barrel_summary", method = RequestMethod.GET)
    @ResponseBody
    public Message barrelSummary(Date beginDate, Date endDate, Pageable pageable, HttpServletRequest request){

        Member member = memberService.getCurrent();

        Enterprise enterprise = null;
        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            enterprise = admin.getEnterprise();
        }

        if (enterprise==null) {
            return Message.error("没有开通店铺");
        }

        List<BarrelSummary> header = shippingBarrelService.summary_barrel(enterprise,beginDate,endDate,pageable);
        List<BarrelSummary> body = shippingBarrelService.summary(enterprise,beginDate,endDate,pageable);

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("summary",header);
        data.put("data",body);
        PageBlock model = PageBlock.bind(new Page());
        model.setData(data);
        return Message.bind(model,request);
    }

    /**
     *  物流结算
     */
    @RequestMapping(value = "/shipping_summary", method = RequestMethod.GET)
    @ResponseBody
    public Message shippingSummary(Date beginDate, Date endDate, Pageable pageable, HttpServletRequest request){

        Member member = memberService.getCurrent();

        Enterprise enterprise = null;
        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            enterprise = admin.getEnterprise();
        }

        if (enterprise==null) {
            return Message.error("没有开通店铺");
        }

        List<ShippingSummary> header = shippingService.summary(enterprise,beginDate,endDate,pageable);
        List<ShippingItemSummary> body = shippingItemService.summary(enterprise,beginDate,endDate,pageable);

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("summary",header);
        data.put("data",body);
        PageBlock model = PageBlock.bind(new Page());
        model.setData(data);
        return Message.bind(model,request);
    }


}