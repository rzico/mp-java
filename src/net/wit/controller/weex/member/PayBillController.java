package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.*;
import net.wit.entity.*;
import net.wit.entity.summary.PayBillShopSummary;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.ESCUtil;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ClassName: PayBillController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberPayBillController")
@RequestMapping("weex/member/paybill")
public class PayBillController extends BaseController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "refundsServiceImpl")
    private RefundsService refundsService;

    /**
     *  收银明细
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long shopId,Date billDate,Pageable pageable, HttpServletRequest request){
        Shop shop = null;
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有绑定门店");
        }
        if (shopId==null) {
            shop = admin.getShop();
        } else {
            shop = shopService.find(shopId);
        }
        List<Filter> filters = new ArrayList<Filter>();
        if (billDate!=null) {
            filters.add(new Filter("billDate", Filter.Operator.le,billDate));
        }
        if (shop!=null) {
            filters.add(new Filter("shop", Filter.Operator.eq, shop));
        } else {
            if (!admin.isOwner()) {
                return Message.error("没有查询权限");
            }
        }
        filters.add(new Filter("status", Filter.Operator.ne,PayBill.Status.failure));
        pageable.setFilters(filters);
        Page<PayBill> page = payBillService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(PayBillViewModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

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
            return Message.error("没有开通");
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
            model.setYesterday(model.getYesterday().add(s.getAmount().subtract(s.getCouponDiscount())) );
        }
        return Message.bind(model,request);
    }

    /**
     *  收银汇总
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Message summary(Long shopId,Date billDate,Pageable pageable, HttpServletRequest request){
        Shop shop = null;
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (shopId==null) {
            shop = admin.getShop();
        } else {
            shop = shopService.find(shopId);
        }

        if (shop==null) {
            if (!admin.isOwner()) {
                return Message.error("没有查询权限");
            }
        }
        Date d = DateUtils.truncate(billDate, Calendar.DATE);
        List<PayBillShopSummary> dsum = payBillService.sumPage(shop,d,d);
        List<PayBillSummaryModel> models = PayBillSummaryModel.bindList(dsum);
        for (PayBillSummaryModel model:models) {
            if (model.getMethod()!=null) {
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(model.getMethod());
                model.setMethod(paymentPlugin.getName());
            } else {
                model.setMethod("现金（未确定）");
            }
        }
        return Message.bind(models,request);
    }


    /**
     *  退款
     */
    @RequestMapping(value = "/refund")
    @ResponseBody
    public Message refund(Long id,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        PayBill payBill = payBillService.find(id);
        if (payBill==null) {
            return Message.error("无效 id");
        }

        if (payBill.getType().equals(PayBill.Type.cardRefund) || payBill.getType().equals(PayBill.Type.cashierRefund)) {
            Refunds refunds = payBill.getRefunds();
            if (refunds.getStatus().equals(Refunds.Status.waiting)) {
                Map<String,Object> data = new HashMap<String,Object>();
                PayBillViewModel model = new PayBillViewModel();
                model.bind(payBill);
                data.put("data",model);
                data.put("sn",payBill.getRefunds().getSn());
                return Message.success(data,"申请退款");
            } else {
                return Message.error("退款单，不能再退款");
            }
        }

        if (payBill.getStatus().equals(PayBill.Status.none)) {
            Payment payment = payBill.getPayment();
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
            String resultCode = null;
            try {
                if (paymentPlugin==null) {
                    resultCode = "0001";
                } else {
                    resultCode = paymentPlugin.queryOrder(payment,request);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return Message.success(e.getMessage());
            }
            switch (resultCode) {
                case "0000":
                    try {
                        paymentService.handle(payment);
                    } catch (Exception e) {
                        return Message.error("通知失败，请联系客服");
                    }
                case "0001":
                    try {
                        paymentService.close(payment);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    return Message.error("支付失败，不能打印");
                default:
                    return Message.error("支付中，请稍等");
            }
        } else {
            if (payBill.getStatus().equals(PayBill.Status.failure)) {
                return Message.error("付款失败的单不能退款");
            }
        }

        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有绑定门店");
        }

        if (!payBill.getShop().equals(admin.getShop())) {
            return Message.error("只能退本门店的单");
        }

        //if (!admin.equals(payBill.getAdmin())) {
        //    return Message.error("收款人才能退款");
        //}

        if (payBill.getBillDate().compareTo(DateUtils.truncate(new Date(), Calendar.DATE))!=0) {
            return Message.error("只能退当天的收款");
        }

        PayBill bill = null;
        try {
            bill = payBillService.createRefund(payBill,admin);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
        Map<String,Object> data = new HashMap<String,Object>();
        PayBillViewModel model = new PayBillViewModel();
        model.bind(bill);
        data.put("data",model);
        data.put("sn",bill.getRefunds().getSn());
        return Message.success(data,"申请退款");
    }

    /**
     *  打印日报
     */
    @RequestMapping(value = "/summary_print", method = RequestMethod.GET)
    @ResponseBody
    public Message summaryPrint(Long shopId,Date billDate,HttpServletRequest request){
        Shop shop = null;
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (shopId==null) {
            shop = admin.getShop();
        } else {
            shop = shopService.find(shopId);
        }

        if (shop==null) {
            if (!admin.isOwner()) {
                return Message.error("没有查询权限");
            }
        }
        Date d = DateUtils.truncate(billDate, Calendar.DATE);
        List<PayBillShopSummary> dsum = payBillService.sumPage(shop,d,d);
        List<PayBillSummaryModel> models = PayBillSummaryModel.bindList(dsum);

        StringBuilder builder = new StringBuilder();
        try {
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] boldOn = ESCUtil.boldOn();
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] fontSize2Small = ESCUtil.fontSizeSetBig(1);
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(1);
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(1);
            byte[] center = ESCUtil.alignCenter();
            byte[] left = ESCUtil.alignLeft();
            byte[] title = "营业日报表".getBytes("gb2312");
            byte[] Stub = "------------商户存根------------".getBytes("gb2312");


            byte[] shopName = "门店：".concat(shop.getName()).getBytes("gb2312");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            byte[] dateDisplay = "日期：".concat(sdf.format(d)).getBytes("gb2312");

            byte[] line = "-------------------------------".getBytes("gb2312");

            BigDecimal total = BigDecimal.ZERO;
            BigDecimal fee = BigDecimal.ZERO;
            BigDecimal wallet = BigDecimal.ZERO;

            for (PayBillSummaryModel model:models) {
                total = total.add(model.getAmount());
                fee = fee.add(model.getFee());
                wallet = wallet.add(model.getAmount());
                if (model.getMethod()!=null) {
                    PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(model.getMethod());
                    model.setMethod(paymentPlugin.getName());
                } else {
                    model.setMethod("现金（未确定）");
                }
                String s = "";
                if (model.getType().equals(PayBill.Type.cashier)) {
                    s = "消费";
                } else
                if (model.getType().equals(PayBill.Type.cashierRefund)) {
                    s = "退款";
                } else
                if (model.getType().equals(PayBill.Type.card)) {
                    s = "充值(会员卡)";
                } else
                if (model.getType().equals(PayBill.Type.cardRefund)) {
                    s = "退款(会员卡)";
                } else {
                    s = "未知";
                }

                s  = s.concat(",").concat(model.getMethod());

                builder.append(s);
                builder.append(nextLine);
                NumberFormat format = NumberFormat.getInstance();
                format.setMinimumFractionDigits( 2 );
                String amount =  format.format(model.getAmount());
                builder.append(String.format("%-33s",amount));
            }
            byte[] content = builder.toString().getBytes("gb2312");

            NumberFormat format = NumberFormat.getInstance();
            format.setMinimumFractionDigits( 2 );
            byte[] totalDiaplay = "营业额:".concat(format.format(total)).getBytes("gb2312");
            byte[] feeDiaplay = "手续费:".concat(format.format(fee)).getBytes("gb2312");
            byte[] walletDiaplay = "线下结算:".concat(format.format(wallet)).getBytes("gb2312");
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            byte[][] cmdBytes = {center, fontSize1Small, boldOn, title, next2Line, boldOff, Stub, nextLine, left, shopName,
                    nextLine, dateDisplay, nextLine, content, nextLine, boldOn, totalDiaplay, nextLine, feeDiaplay, nextLine,walletDiaplay, nextLine,boldOff,next4Line,
                    breakPartial};


            byte[] data = ESCUtil.byteMerger(cmdBytes);
            return Message.bind(Base64.encodeBase64String(data),request);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return Message.error("打印出错了");
        }
    }

    /**
     *  打印小票
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @ResponseBody
    public Message print(Long id,HttpServletRequest request){
        PayBill payBill = payBillService.find(id);
        if (payBill==null) {
            return Message.error("无效id");
        }
        if (payBill.getStatus().equals(PayBill.Status.none)) {
            if (payBill.getType().equals(PayBill.Type.cashierRefund) || payBill.getType().equals(PayBill.Type.cardRefund)) {
                Refunds refunds = payBill.getRefunds();
                //还没提交，前台发起补提交退款
                if (refunds.getStatus().equals(Refunds.Status.waiting)) {
                    return Message.error("没有提交不能打印");
                }
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
                String resultCode = null;
                try {
                    if (paymentPlugin==null) {
                        resultCode = "0001";
                    } else {
                        resultCode = paymentPlugin.refundsQuery(refunds, request);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return Message.success(e.getMessage());
                }
                switch (resultCode) {
                    case "0000":
                        try {
                            refundsService.handle(refunds);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    case "0001":
                        try {
                            refundsService.close(refunds);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                        return Message.error("支付失败，不能打印");
                }

            } else {
                Payment payment = payBill.getPayment();
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
                String resultCode = null;
                try {
                    if (paymentPlugin==null) {
                        resultCode = "0001";
                    } else {
                        resultCode = paymentPlugin.queryOrder(payment, request);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return Message.success(e.getMessage());
                }
                switch (resultCode) {
                    case "0000":
                        try {
                            paymentService.handle(payment);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    case "0001":
                        try {
                            paymentService.close(payment);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                        return Message.error("支付失败，不能打印");
                    default:
                        return Message.error("支付中，请稍等");
                }

            }
        } else {
            if (payBill.getStatus().equals(PayBill.Status.failure)) {
                return Message.error("支付失败，不能打印");
            }
        }
        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        StringBuilder builder3 = new StringBuilder();
        StringBuilder builder4 = new StringBuilder();
        StringBuilder builder5 = new StringBuilder();
        StringBuilder builder6 = new StringBuilder();
        StringBuilder builder7 = new StringBuilder();
        StringBuilder builder8 = new StringBuilder();
        StringBuilder builder9 = new StringBuilder();
        StringBuilder builder10 = new StringBuilder();
        try {
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] title = "智 能 POS 签 购 单".getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] Stub = "------------商户存根------------".getBytes("gb2312");
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetBig(1);

            byte[] left = ESCUtil.alignLeft();

            byte[] merchantName = builder1.append("商户名：").append(payBill.getShop().getName()).toString().getBytes("gb2312");
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(1);
            byte[] partner = builder2.append("商户号：").append(payBill.getShop().getCode()).toString().getBytes("gb2312");
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(1);

            byte[] device = builder3.append("终端号：").append(payBill.getAdmin()==null?"":payBill.getAdmin().getMember().getUuid()).toString().getBytes("gb2312");

            byte[] cashierName = builder4.append("收银员：").append(payBill.getAdmin()==null?"":payBill.getAdmin().getName()).toString().getBytes("gb2312");

            byte[] line = "-------------------------------".getBytes("gb2312");
            byte[] snNumber = builder5.append("流水号：").append(String.valueOf(10200L+payBill.getId())).toString().getBytes("gb2312");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTime = simpleDateFormat.format(payBill.getCreateDate());
            byte[] paymentDate = builder6.append("日期时间：").append(formattedTime).toString().getBytes("gb2312");
            byte[] type = null;
            if (payBill.getType().equals(PayBill.Type.cardRefund)) {
                type = builder7.append("交易类型：退款[").append(payBill.getRefunds() == null ? "线下支付" : payBill.getRefunds().getPaymentMethod()).append("]").toString().getBytes("gb2312");
            } else
            if (payBill.getType().equals(PayBill.Type.cashierRefund)) {
                type = builder7.append("交易类型：退款[").append(payBill.getRefunds() == null ? "线下支付" : payBill.getRefunds().getPaymentMethod()).append("]").toString().getBytes("gb2312");
            } else
            if (payBill.getType().equals(PayBill.Type.card)) {
                type = builder7.append("交易类型：充值[").append(payBill.getPayment() == null ? "线下支付" : payBill.getPayment().getPaymentMethod()).append("]").toString().getBytes("gb2312");
            } else {
                type = builder7.append("交易类型：消费[").append(payBill.getPayment() == null ? "线下支付" : payBill.getPayment().getPaymentMethod()).append("]").toString().getBytes("gb2312");
            }
            byte[] amount = builder8.append("金额：RMB ").append(payBill.getPayBillAmount()).toString().getBytes("gb2312");
            byte[] signature = "支付人签名：".getBytes("gb2312");
            byte[] agreement = "确认以上交易同意将其记入商家账户".getBytes("gb2312");
            Setting setting = SettingUtils.get();
            byte[] customerPhone = builder9.append("客服热线：").append(setting.getPhone()).toString().getBytes("gb2312");
            byte[] technical = builder10.append("技术支持：").append(setting.getCompany()).toString().getBytes("gb2312");

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            byte[][] cmdBytes = {center, fontSize1Small, boldOn, title, next2Line, boldOff, Stub, nextLine, left, merchantName,
                    nextLine, partner, nextLine, device, nextLine, cashierName, nextLine, line,
                    nextLine, snNumber, nextLine, paymentDate, nextLine, type, nextLine, boldOn, amount, nextLine, boldOff,
                    signature, next2Line, line, nextLine, agreement, nextLine, customerPhone, nextLine, technical, next4Line,
                    breakPartial};

            byte[] data = ESCUtil.byteMerger(cmdBytes);
            return Message.bind(Base64.encodeBase64String(data),request);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return Message.error("打印出错了");
        }
    }

}