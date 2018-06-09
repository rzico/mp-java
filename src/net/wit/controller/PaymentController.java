
package net.wit.controller;

import net.wit.Message;
import net.wit.controller.model.PaymentModel;
import net.wit.entity.*;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.BrowseUtil;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 支付
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("paymentController")
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "transferServiceImpl")
    private TransferService transferService;

    @Resource(name = "refundsServiceImpl")
    private RefundsService refundsService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

//    /**
//     * 付款页
//     *
//     * @param sn              支付单号
//     *
//     */
//    @RequestMapping(value = "/index", method = RequestMethod.GET)
//    public String index(String sn,HttpServletRequest request) {
//        Payment payment = paymentService.findBySn(sn);
//        String userAgent = request.getHeader("user-agent");
//        String type="weixin";
//        System.out.println(userAgent);
//        if (BrowseUtil.isAlipay(userAgent)) {
//            type="alipay";
//        } else {
//            type="weixin";
//        }
//        if (payment.getPaymentPluginId()!=null) {
//            if ("cardPayPlugin".equals(payment.getPaymentPluginId())) {
//                type = "cardPayPlugin";
//            } else if ("balancePayPlugin".equals(payment.getPaymentPluginId())) {
//                type = "balancePayPlugin";
//            }
//        }
//        System.out.println(type);
//        return "redirect:/weixin/payment/view.html?psn="+sn+"&amount="+payment.getAmount()+"&type="+type;
//    }

    /**
     * 付款单信
     *
     * @param sn              支付单号
     *
     */

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(String sn, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        if (payment==null) {
            Message.error("无效付款单");
        }
        PaymentModel model = new PaymentModel();
        model.bind(payment);
        return Message.bind(model,request);
    }

    /**
     * 支付提交
     *
     * @param paymentPluginId
     * @param sn              支付单号
     *
     */

    @RequestMapping(value = "/submit")
    @ResponseBody
    public Message submit(String paymentPluginId, String sn,String safeKey,Long xmid, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        if (payment==null) {
            return Message.error("无效付款单");
        }

        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
            return Message.error("支付插件无效");
        }

        payment.setMethod(Method.online);
        payment.setPaymentPluginId(paymentPluginId);
        payment.setPaymentMethod(paymentPlugin.getName());
        paymentService.update(payment);

//        if (xmid!=null) {
//            Member agent = memberService.find(xmid);
//            if (agent.getTopic()==null) {
//                return Message.error("没有开通");
//            }
//            if (agent.getTopic().getConfig()==null)  {
//                return Message.error("没有设置");
//            }
//            if (agent.getTopic().getConfig().getAppetAppId()==null)  {
//                return Message.error("没有设置");
//            }
////            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
////
////            String appid = agent.getTopic().getConfig().getAppetAppId();
////
//            payment.setWay(Payment.Way.yundian);
//            payment.setMerchant(agent);
//        }

        Map<String, Object> parameters = null;
        if (safeKey==null) {
            parameters = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request);
        } else {
            if ("free".equals(safeKey)) {
                Member member = memberService.getCurrent();
                if (member==null) {
                    return Message.error("不能免密支付");
                }

                Member seller = payment.getPayee();
                if ("cardPayPlugin".equals(paymentPluginId)) {
                    Card card = null;
                    for (Card c:member.getCards()) {
                       if (c.getOwner().equals(seller)) {
                           card = c;
                           break;
                       }
                    }
                    int challege = StringUtils.Random6Code();
                    card.setSign(String.valueOf(challege));
                    cardService.update(card);
                    safeKey = "http://free/q/818802"+card.getCode()+String.valueOf(challege)+".jhtml";
                }
                if ("balancePayPlugin".equals(paymentPluginId)) {
                    int challege = StringUtils.Random6Code();
                    member.setSign(String.valueOf(challege));
                    memberService.update(member);
                    safeKey = "http://free/q/818805"+String.valueOf(member.getId()+10200L)+String.valueOf(challege)+".jhtml";
                }
            }
            parameters = paymentPlugin.submit(payment,safeKey,request);
        }
        if ("SUCCESS".equals(parameters.get("return_code"))) {
            if ("balancePayPlugin".equals(paymentPluginId) || "cardPayPlugin".equals(paymentPluginId) || "bankPayPlugin".equals(paymentPluginId) || "cashPayPlugin".equals(paymentPluginId) || "monthPayPlugin".equals(paymentPluginId)) {
                try {
                    paymentService.handle(payment);
                } catch (Exception e) {
                    e.printStackTrace();
                    //模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
                }
            }
            return Message.success(parameters, "success");
        } else {
            return Message.error(parameters.get("result_msg").toString());
        }

    }

    /**
     * 支付结果通知
     */
    @RequestMapping("/notify/{notifyMethod}/{sn}")
    public void notify(@PathVariable PaymentPlugin.NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request,HttpServletResponse response) throws Exception {
        Payment payment = paymentService.findBySn(sn);
        if (payment != null) {
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
            if (paymentPlugin != null && notifyMethod.equals(PaymentPlugin.NotifyMethod.async)) {
                if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
                    try {
                        paymentService.handle(payment);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
                out.flush();
                return ;
            }
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("error");
        out.flush();

    }

    /**
     * 转账结果通知
     */
    @RequestMapping("/transfer/{sn}")
    public void transfer(@PathVariable String sn, HttpServletRequest request,HttpServletResponse response) throws Exception {
        Transfer transfer = transferService.findBySn(sn);
//        System.out.println("transfer");
        if (transfer != null) {
             String resp = UnsPay.verifyNotify(sn, request);
                if ("00".equals (resp)) {
                    transferService.handle(transfer);
                }
                if ("20".equals (resp)) {
                    transferService.refunds(transfer);
                }
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print("success");
                out.flush();
                return;
         }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("error");
        out.flush();
    }

//    /**
//     * 退款结果通知
//     */
//    @RequestMapping("/weixin/refunds")
//    public void refunds(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin("weixinH5Plugin");
//        if (paymentPlugin != null) {
//            String resp = paymentPlugin.refundsVerify(request);
//            if (!"".equals(resp)) {
//                Refunds refunds = refundsService.findBySn(resp);
//                refundsService.handle(refunds);
//            }
//            response.setContentType("application/json");
//            PrintWriter out = response.getWriter();
//            out.print("success");
//            out.flush();
//            return;
//        }
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        out.print("error");
//        out.flush();
//
//    }
//

    /**
     * 查询支付状态
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Message query(String sn, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        if (payment == null) {
            return Message.error("无效支付单号");
        }
        if (payment.getStatus().equals(Status.success)) {
            return Message.success((Object) "0000","支付成功");
        } else
        if (payment.getStatus().equals(Status.failure)) {
            return Message.success((Object) "0001","支付失败");
        }
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
                return Message.success((Object) resultCode,"支付成功");
            case "0001":
                try {
                    paymentService.close(payment);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return Message.success((Object) resultCode,"支付失败");
            default:
                return Message.success((Object) resultCode,"支付中");
        }
    }


}