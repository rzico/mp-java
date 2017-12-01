
package net.wit.controller;

import net.wit.Message;
import net.wit.controller.model.PaymentModel;
import net.wit.controller.model.RefundsModel;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Refunds;
import net.wit.entity.Transfer;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Ref;
import java.util.Map;

/**
 * Controller -  退款
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("refundsController")
@RequestMapping("/refunds")
public class RefundsController extends BaseController {

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

    /**
     * 付款单信
     *
     * @param sn              支付单号
     *
     */

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(String sn, HttpServletRequest request) {
        Refunds refunds = refundsService.findBySn(sn);
        if (refunds==null) {
            Message.error("无效付款单");
        }
        RefundsModel model = new RefundsModel();
        model.bind(refunds);
        return Message.bind(model,request);
    }

    /**
     * 支付退款
     *
     * @param paymentPluginId
     * @param sn              支付退款
     *
     */

    @RequestMapping(value = "/submit")
    @ResponseBody
    public Message submit(String paymentPluginId, String sn,String safeKey, HttpServletRequest request) {
        Refunds refunds = refundsService.findBySn(sn);
        if (refunds==null) {
            Message.error("无效退款单");
        }

        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
            return Message.error("支付插件无效");
        }

        try {
            refunds.setMethod(Refunds.Method.online);
            refunds.setPaymentPluginId(paymentPluginId);
            refunds.setPaymentMethod(paymentPlugin.getName());
            refundsService.update(refunds);
            refundsService.refunds(refunds,request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Message.error(e.getMessage());
        }

        Map<String, Object> parameters = paymentPlugin.refunds(refunds,request);
        if ("SUCCESS".equals(parameters.get("return_code"))) {
            if ("balancePayPlugin".equals(paymentPluginId) || "cardPayPlugin".equals(paymentPluginId) || "bankPayPlugin".equals(paymentPluginId) || "cashPayPlugin".equals(paymentPluginId)) {
                try {
                    refundsService.handle(refunds);
                } catch (Exception e) {
                    e.printStackTrace();
                    //模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
                }
            }
            return Message.success(parameters, "success");
        } else {
            try {
                refundsService.close(refunds);
            } catch (Exception e) {
                logger.error(e.getMessage());
                parameters.put("return_code","success");
                parameters.put("result_msg","撤消退款失败");
                return Message.success(parameters,"退款已提交，客服会尽快处理");
            }
            return Message.error(parameters.get("result_msg").toString());
        }

    }

//    /**
//     * 退款结果通知
//     */
//    @RequestMapping("/notify.jhtml")
//    public void notify(@PathVariable PaymentPlugin.NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request,HttpServletResponse response) throws Exception {
//        Payment payment = paymentService.findBySn(sn);
//        if (payment != null) {
//            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
//            if (paymentPlugin != null && notifyMethod.equals(PaymentPlugin.NotifyMethod.async)) {
//                if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
//                    try {
//                        paymentService.handle(payment);
//                    } catch (Exception e) {
//                        logger.error(e.getMessage());
//                    }
//                }
//                response.setContentType("application/json");
//                PrintWriter out = response.getWriter();
//                out.print(paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
//                out.flush();
//                return ;
//            }
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
        Refunds refunds = refundsService.findBySn(sn);
        if (refunds == null) {
            return Message.error("无效退款单号");
        }
        if (refunds.getStatus().equals(Status.success)) {
            return Message.success((Object) "0000","退款成功");
        } else
        if (refunds.getStatus().equals(Status.failure)) {
            return Message.success((Object) "0001","退款失败");
        }
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
        String resultCode = null;
        try {
            resultCode = paymentPlugin.refundsQuery(refunds,request);
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
                return Message.success((Object) resultCode,"退款成功");
            case "0001":
                try {
                    refundsService.close(refunds);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return Message.success((Object) resultCode,"退款失败");
            default:
                return Message.success((Object) resultCode,"退款中");
        }
    }


}