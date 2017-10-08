
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

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     *  H5支付页面
     *
     * @param sn              支付单号
     *
     */

    @RequestMapping(value = "/h5_submit", method = RequestMethod.GET)
    public String h5(String sn, ModelMap model, HttpServletRequest request,HttpServletResponse response) {
        try {
            Payment payment = paymentService.findBySn(sn);
            //为空时，暂时开启测试
            if (payment == null) {
                payment = new Payment();
                payment.setMethod(Method.online);
                payment.setStatus(Status.waiting);
                payment.setAmount(new BigDecimal("0.1"));
                payment.setMemo("支付测试");
                payment.setSn(snService.generate(Sn.Type.payment));
                payment.setMember(memberService.find(1L));
                payment.setType(Type.recharge);
                paymentService.save(payment);
            }
            String paymentPluginId = "weixinH5Plugin";
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
            payment.setPaymentPluginId(paymentPluginId);
            payment.setPaymentMethod(paymentPlugin.getName());
            paymentService.update(payment);
            Map<String, Object> parameters = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request);
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            model.addAttribute("mweb_url", parameters.get("mweb_url") + "&redirect_url=http://" + URLEncoder.encode(bundle.getString("app.url") + "/payment/h5_notify.jhtml"));
            return "/common/submit";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "redirect:/payment/h5_error.jhtml";
        }
    }

    /**
     *  H5支付结果
     *
     */

    @RequestMapping(value = "/h5_notify", method = RequestMethod.GET)
    public String h5_notify(ModelMap model, HttpServletRequest request,HttpServletResponse response) {
        model.addAttribute("notifyMessage","success");
        return "/common/notify";
    }

    /**
     *  H5支付失败
     *
     */

    @RequestMapping(value = "/h5_error", method = RequestMethod.GET)
    public String h5_error(ModelMap model, HttpServletRequest request,HttpServletResponse response) {
        model.addAttribute("notifyMessage","error");
        return "/common/notify";
    }

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
        return Message.success(model, "success");
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
    public boolean notify(@PathVariable PaymentPlugin.NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request,HttpServletResponse response) throws Exception {
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
                response.setContentType("application/octet-stream");
                PrintWriter out = response.getWriter();
                out.print(paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
                out.flush();
                return true;
            }
        }
        return false;
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