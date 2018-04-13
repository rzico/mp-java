
package net.wit.controller.unspay;

import net.wit.Message;
import net.wit.controller.BaseController;
import net.wit.controller.model.PaymentModel;
import net.wit.entity.*;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
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

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "transferServiceImpl")
    private TransferService transferService;

    /**
     * 提交
     *
     * @param 银行名称 bankname
     * @param 卡号 cardno
     * @param 开户名 name
     * @param 城市 city
     * @param 转账金额 amount
     * @param 商户id member
     *
     */

    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String body,String sign, HttpServletRequest request) {
      String params = rsaService.decryptValue(body,request);

      Map<String,String> data = JsonUtils.toObject(params,Map.class);

      Member member = memberService.find(Long.parseLong(data.get("member")));
      if (member==null) {
          return Message.error("无效商户号");
      }
      String h = MD5Utils.getMD5Str(body+member.getPassword());
      if (h.equals(sign)) {
          return Message.error("无效商户号");
      }
      Admin admin = adminService.findByMember(member);
      if (admin==null) {
          return Message.error("无效商户号");
      }
      Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return Message.error("无效商户号");
        }

      BigDecimal fee = enterprise.getTransfer();
        if (fee.compareTo(BigDecimal.ONE)<0) {
            fee = BigDecimal.ONE;
        }

        Transfer transfer = new Transfer();
        transfer.setBankname(data.get("bankname"));
        transfer.setCardno(data.get("cardno"));
        transfer.setCity(data.get("city"));
        transfer.setName(data.get("name"));
        transfer.setMemo("银行卡代付");
        transfer.setAmount(new BigDecimal(data.get("amount")).add(fee));
        transfer.setMember(member);
        transfer.setStatus(Transfer.Status.waiting);
        transfer.setFee(fee);
        transfer.setType(Transfer.Type.bankcard);
        transfer.setSn(snService.generate(Sn.Type.transfer));
        try {
            transferService.submit(transfer);
            return Message.success((Object) transfer.getSn(),"提交成功");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }

    }

    /**
     * 查询支付状态
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Message query(String sn,String sign, HttpServletRequest request) {
        Transfer transfer = transferService.findBySn(sn);
        String h = MD5Utils.getMD5Str(sn+transfer.getMember().getPassword());
        if (h.equals(sign)) {
            return Message.error("无效商户号");
        }
        if (transfer == null) {
            return Message.error("无效付款单号");
        }
        if (transfer.getStatus().equals(Status.success)) {
            return Message.success((Object) "0000","支付成功");
        } else
        if (transfer.getStatus().equals(Status.failure)) {
            return Message.success((Object) "0001","支付失败");
        }
        try {
            String resp = UnsPay.query(transfer.getSn());
            if ("00".equals(resp)) {
                transferService.handle(transfer);
                return Message.success((Object)resp,"提现成功");
            } else
            if ("20".equals(resp)) {
                transferService.refunds(transfer);
                return Message.success((Object)resp,"提现失败,款项退回账号");
            } else
            if ("10".equals(resp)) {
                return Message.success((Object)resp,"正在处理中");
            } else {
                return Message.error("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
    }
}