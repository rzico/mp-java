
package net.wit.controller;

import com.sun.tools.internal.ws.wsdl.document.http.HTTPUrlEncoded;
import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.weex.model.PaymentModel;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.Sn;
import net.wit.entity.Transfer;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
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

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String paymentPluginId, String sn, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        //if (payment==null) {
        //    Message.error("无效付款单");
        //}
        payment = new Payment();
        payment.setMethod(Method.online);
        payment.setStatus(Status.waiting);
        payment.setAmount(new BigDecimal("0.1"));
        payment.setMemo("支付测试");
        payment.setSn(snService.generate(Sn.Type.payment));
        payment.setMember(memberService.find(1L));
        payment.setPayee(payment.getMember());
        payment.setType(Type.recharge);
        paymentService.save(payment);
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
            return Message.error("支付插件无效");
        }

        payment.setPaymentPluginId(paymentPluginId);
        payment.setPaymentMethod(paymentPlugin.getName());
        paymentService.update(payment);
        Map<String, Object> parameters = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request);
        return Message.success(parameters, "success");
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
     * 转几结果通知
     */
    @RequestMapping("/transfer/{sn}")
    public void notifyTransfer(@PathVariable String sn, HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println(sn);
        Transfer transfer = transferService.findBySn(sn);

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

    /**
     * 查询支付状态
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Message query(String sn, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        if (payment == null) {
            return Message.error("无效支付单号");
        }
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
        String resultCode = paymentPlugin.queryOrder(payment,request);
        switch (resultCode) {
            case "0000":
                try {
                    paymentService.handle(payment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Message.success((Object) resultCode,"支付成功");
            case "0001":
                return Message.success((Object) resultCode,"支付失败");
            default:
                return Message.success((Object) resultCode,"支付中");
        }
    }


}