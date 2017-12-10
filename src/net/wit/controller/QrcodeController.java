
package net.wit.controller;

import net.wit.Message;
import net.wit.entity.Payment;
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
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller - 二维码
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("qrcodeController")
@RequestMapping("/q")
public class QrcodeController extends BaseController {

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

     /**
      * 生成二维码
      */
     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     public String qrcode(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) {
         try {
             String cmd = id.substring(0, 6);
             //名片
             if ("865380".equals(cmd)) {
                 String userId = id.substring(6, id.length());
                 Long uid = Long.parseLong(userId) - 10200L;
                 return "redirect:/topic?id=" + uid;
             } else
             /**
              * 会员卡 空卡，跑转领卡界面,会员卡界面判断跳转  会号规则 88100006165001042 实体卡  86100006165 商家码
              */
             if ("818801".equals(cmd)) {
                 String code = id.substring(6, id.length());
                 return "redirect:/card?code=" + code;
             } else {
                 return "redirect:/";
             }
         } catch (Exception e) {
             return "redirect:/";
         }
     }


}