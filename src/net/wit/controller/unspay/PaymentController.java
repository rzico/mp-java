
package net.wit.controller.unspay;

import net.wit.Message;
import net.wit.controller.BaseController;
import net.wit.entity.*;
import net.wit.entity.Payment.Status;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 付款接口
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("unspayPaymentController")
@RequestMapping("/unspay/payment")
public class PaymentController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "transferServiceImpl")
    private TransferService transferService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 提交
     *
     *  收款单号 sn
     *  摘要 memo
     *  收款金额 amount
     *  支付类型 weixinPayPlugin(公众号支付)  weixinLetPlugin(小程序支付)  safeKeyPlugin(商家扫用户二维码支付)  weixinQRPlugin(微信扫码支付)  alipayQRPlugin(阿里扫码支付)
     *  安全码 safeKey
     *  异步通知 url  (扫码付没有通知)
     *  商户id merchart
     *
     */

    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String body,String sign, HttpServletRequest request) {
        String params = rsaService.decryptValue(body,request);

        Map<String,String> data = JsonUtils.toObject(params,Map.class);

        Member member = memberService.find(Long.parseLong(data.get("merchart")));
        if (member==null) {
            return Message.error("无效商户号");
        }
        String h = MD5Utils.getMD5Str(body+member.getPassword());
        if (h.equals(sign)) {
            return Message.error("签名出错了");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("无效商户号");
        }
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return Message.error("无效商户号");
        }
        if (!enterprise.getStatus().equals(Enterprise.Status.success)) {
            return Message.error("商户没有审核通过");
        }

        String paymentPluginId = data.get("paymentPluginId");

        if ("weixinPayPlugin".equals(paymentPluginId)) {
            paymentPluginId = "weixinOcPayPlugin";
        } else
        if ("weixinLetPlugin".equals(paymentPluginId)) {
            paymentPluginId = "weixinOcLetPlugin";
        } else
        if ("weixinQRPlugin".equals(paymentPluginId)) {
            paymentPluginId = "weixinOcPayPlugin";
        } else
        if ("alipayQRPlugin".equals(paymentPluginId)) {
            paymentPluginId = "weixinOcPayPlugin";
        }
        String safeKey = data.get("safeKey");
        String sn = data.get("sn");

        Payment payment = new Payment();
        payment.setPayee(member);
        payment.setMember(member);
        payment.setStatus(Payment.Status.waiting);
        payment.setMethod(Payment.Method.online);
        payment.setType(Payment.Type.gather);
        payment.setMemo(data.get("memo"));
        payment.setAmount(new BigDecimal(data.get("amount")));
        payment.setSn(String.valueOf(member.getId())+"@"+sn);
        payment.setWay(Payment.Way.yundian);
        payment.setPaymentPluginId(paymentPluginId);
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
            return Message.error("支付插件无效");
        }

        payment.setPaymentMethod(paymentPlugin.getName());
        try {
            paymentService.save(payment);

            Map<String, Object> parameters = null;
            if (safeKey==null) {
                parameters = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request);
            } else {
                parameters = paymentPlugin.submit(payment,safeKey,request);
            }

            return Message.success(parameters, "success");

        } catch (Exception e) {
            return Message.error(e.getMessage());
        }

    }

    /**
     * 查询支付状态
     *
     * @param  sn 付款单号
     * @param  merchart 商户id
     *
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Message query(String sn,String merchart,String sign, HttpServletRequest request) {
        Transfer transfer = transferService.findByOrderSn(sn);
        if (transfer == null) {
            return Message.error("无效付款单号");
        }
        String h = MD5Utils.getMD5Str(sn+merchart+transfer.getMember().getPassword());
        if (h.equals(sign)) {
            return Message.error("签名出错了");
        }

        Member member = memberService.find(Long.parseLong(merchart));
        sn = String.valueOf(member.getId())+"@"+sn;

        Payment payment = paymentService.findBySn(sn);

        try {
            if (payment.getStatus().equals(Status.success)) {
                return Message.success((Object) "0000","支付成功");
            } else {
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
                if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
                    return Message.error("支付插件无效");
                }
                String resp = paymentPlugin.queryOrder(payment,request);

                if ("0000".equals(resp)) {
                    paymentService.handle(payment);
                }
                return Message.success((Object)resp,"支付成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
    }


}