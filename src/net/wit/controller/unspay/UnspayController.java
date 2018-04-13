
package net.wit.controller.unspay;

import net.wit.Message;
import net.wit.controller.BaseController;
import net.wit.controller.model.PaymentModel;
import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Transfer;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
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
import java.util.Map;

/**
 * Controller - 代付接口
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("unspayController")
@RequestMapping("/unspay")
public class UnspayController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
//
//    /**
//     * 提交
//     *
//     * @param paymentPluginId
//     * @param sn              支付单号
//     *
//     */
//
//    @RequestMapping(value = "/submit",method = RequestMethod.GET)
//    @ResponseBody
//    public Message submit(String paymentPluginId, String sn,String safeKey, HttpServletRequest request) {
//        Payment payment = paymentService.findBySn(sn);
//        if (payment==null) {
//            return Message.error("无效付款单");
//        }
//
//        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
//        if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
//            return Message.error("支付插件无效");
//        }
//
//        payment.setMethod(Method.online);
//        payment.setPaymentPluginId(paymentPluginId);
//        payment.setPaymentMethod(paymentPlugin.getName());
//        paymentService.update(payment);
//
//        Map<String, Object> parameters = null;
//        if (safeKey==null) {
//            parameters = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request);
//        } else {
//            if ("free".equals(safeKey)) {
//                Member member = memberService.getCurrent();
//                if (member==null) {
//                    return Message.error("不能免密支付");
//                }
//
//                Member seller = payment.getPayee();
//                if ("cardPayPlugin".equals(paymentPluginId)) {
//                    Card card = null;
//                    for (Card c:member.getCards()) {
//                       if (c.getOwner().equals(seller)) {
//                           card = c;
//                           break;
//                       }
//                    }
//                    int challege = StringUtils.Random6Code();
//                    card.setSign(String.valueOf(challege));
//                    cardService.update(card);
//                    safeKey = "http://free/q/818802"+card.getCode()+String.valueOf(challege)+".jhtml";
//                }
//                if ("balancePayPlugin".equals(paymentPluginId)) {
//                    int challege = StringUtils.Random6Code();
//                    member.setSign(String.valueOf(challege));
//                    memberService.update(member);
//                    safeKey = "http://free/q/818805"+String.valueOf(member.getId()+10200L)+String.valueOf(challege)+".jhtml";
//                }
//            }
//            parameters = paymentPlugin.submit(payment,safeKey,request);
//        }
//        if ("SUCCESS".equals(parameters.get("return_code"))) {
//            if ("balancePayPlugin".equals(paymentPluginId) || "cardPayPlugin".equals(paymentPluginId) || "bankPayPlugin".equals(paymentPluginId) || "cashPayPlugin".equals(paymentPluginId)) {
//                try {
//                    paymentService.handle(payment);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    //模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
//                }
//            }
//            return Message.success(parameters, "success");
//        } else {
//            return Message.error(parameters.get("result_msg").toString());
//        }
//
//    }
//
//    /**
//     * 查询支付状态
//     */
//    @RequestMapping(value = "/query", method = RequestMethod.POST)
//    @ResponseBody
//    public Message query(String sn, HttpServletRequest request) {
//        Payment payment = paymentService.findBySn(sn);
//        if (payment == null) {
//            return Message.error("无效支付单号");
//        }
//        if (payment.getStatus().equals(Status.success)) {
//            return Message.success((Object) "0000","支付成功");
//        } else
//        if (payment.getStatus().equals(Status.failure)) {
//            return Message.success((Object) "0001","支付失败");
//        }
//        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
//        String resultCode = null;
//        try {
//            if (paymentPlugin==null) {
//                resultCode = "0001";
//            } else {
//                resultCode = paymentPlugin.queryOrder(payment, request);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return Message.success(e.getMessage());
//        }
//        switch (resultCode) {
//            case "0000":
//                try {
//
//                    paymentService.handle(payment);
//                } catch (Exception e) {
//                    logger.error(e.getMessage());
//                }
//                return Message.success((Object) resultCode,"支付成功");
//            case "0001":
//                try {
//                    paymentService.close(payment);
//                } catch (Exception e) {
//                    logger.error(e.getMessage());
//                }
//                return Message.success((Object) resultCode,"支付失败");
//            default:
//                return Message.success((Object) resultCode,"支付中");
//        }
//    }
//

}