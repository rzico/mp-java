
package net.wit.controller;

import net.wit.Message;
import net.wit.entity.*;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.ScanUtil;
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

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

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

                 Member member = memberService.find(uid);
                 Admin admin = adminService.findByMember(member);
                 if (admin!=null && admin.getEnterprise()!=null) {
                     Topic topic = admin.getEnterprise().getMember().getTopic();
                     if (topic!=null && topic.getTopicCard()!=null) {
                         return "redirect:/#/card?code=85" + String.valueOf(100000000 + topic.getTopicCard().getId()) + "&xuid=" + String.valueOf(uid);
                     } else {
                         return "redirect:/#/topic?id=" + uid;
                     }
                 } else {
                    return "redirect:/#/topic?id=" + uid;
                 }
             } else
             /**
              * 会员卡 空卡，跑转领卡界面,会员卡界面判断跳转  会号规则 88100006165001042 实体卡  86100006165 商家码
              */
             if ("818801".equals(cmd)) {
                 String code = id.substring(6, id.length());
                 return "redirect:/#/card?code=" + code;
             } else
             if ("818802".equals(cmd)) {
                 String code = id.substring(6, id.length());
                 String c = code.substring(0,code.length()-6);
                 Card card = cardService.find(c);
                 if (card==null) {
                     return "redirect:/#/";
                 }
                 Topic topic = card.getOwner().getTopic();
                 if (topic!=null && topic.getTopicCard()!=null) {
                     return "redirect:/#/card?code=85" + String.valueOf(100000000+topic.getTopicCard().getId())+"&xuid="+card.getMembers().get(0).getId();
                 } else {
                     return "redirect:/#/";
                 }
             } else
             {
                 return "redirect:/#/";
             }
         } catch (Exception e) {
             return "redirect:/#/";
         }
     }


    /**
     * 二维码分解
     */
    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    @ResponseBody
    public  Message scan(String code,HttpServletRequest request,HttpServletResponse response) {

        Map<String, String> data = ScanUtil.scanParser(code);
//
//        名片：865380  + (10200 + 会员 id）
//        领卡:  818801  + 会员卡号
//        付款码:  818802  + 会员卡号+验证码
//        优惠券:  818803  + 代码
//        收钱码:  818804  + 编码
//        钱包付款码:  818805  + 会员号+验证码
//
        String c = data.get("code");
        if (data.get("type").toString().equals("865380")) {
            Long id = Long.parseLong(c.substring(6)) - 10200;
            data.put("id", String.valueOf(id));
            Member member = memberService.find(id);
            if (member==null) {
                return Message.error("无效名片");
            }
            Admin admin = adminService.findByMember(member);
            if (admin!=null && admin.getEnterprise()!=null) {
                data.put("tuid", String.valueOf(admin.getEnterprise().getMember().getId()));
                data.put("shopId",String.valueOf(admin.getShop().getId()));
            }
            data.put("xuid", String.valueOf(id));

        } else if (data.get("type").toString().equals("818801")) {
            String no = c.substring(6);
            if (no.substring(0, 2).equals("86")) {
                Long shopId = Long.parseLong(no.substring(2)) - 100000000;
                Shop shop = shopService.find(shopId);
                if (shop != null) {
                    data.put("tuid", String.valueOf(shop.getOwner().getId()));
                } else {
                    return Message.error("不能识别的二维码");
                }
            } else {
                return Message.error("不能识别的二维码");
            }
        } else if (data.get("type").toString().equals("818802")) {
            String no = c.substring(0, c.length() - 6);
            String sign = c.substring(c.length() - 6, c.length());
            Card card = cardService.find(no);
            if (card == null) {
                return Message.error("不能识别的二维码");
            }
//            if (!sign.equals(card.getSign())) {
//                return Message.error("不能识别的二维码");
//            }
            data.put("tuid", String.valueOf(card.getOwner().getId()));
            data.put("xuid", String.valueOf(card.getMembers().get(0).getId()));
        } else {
            return Message.error("不能识别的二维码");
        }

        return Message.success(data, "有效二维码");
    }
}