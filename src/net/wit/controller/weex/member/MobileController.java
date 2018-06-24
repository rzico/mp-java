package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @ClassName: RegisterController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberMobileController")
@RequestMapping("/weex/member/mobile")
public class MobileController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    /**
     * 发送验证码
     * mobile 手机号
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(HttpServletRequest request) {
        String m = rsaService.decryptParameter("mobile", request);
        rsaService.removePrivateKey(request);
        Boolean binded = false;
        if (m==null) {
            Member member = memberService.getCurrent();
            if (member!=null & member.getMobile()!=null) {
                m = member.getMobile();
                binded = true;
            } else {
                return Message.error("无效手机号");
            }
        }
        Member member = memberService.findByMobile(m);
        if (member!=null && !binded) {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            BindUser bindUser = bindUserService.findMember(member,bundle.getString("app.appid"), BindUser.Type.weixin);
            if (bindUser==null) {
                return Message.error("此手机已绑定另一账号");
            }
        }
        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(m);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_BIND_CAPTCHA,JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(m);
        smsSend.setContent("验证码 :" + securityCode + ",只用于设置密码。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     * 绑定手机
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MOBILE_BIND_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        redisService.remove(Member.MOBILE_BIND_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member = memberService.getCurrent();
        try {
            String captcha = rsaService.decryptParameter("captcha", request);
            rsaService.removePrivateKey(request);

            if (captcha==null) {
                return Message.error("无效验证码");
            }
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            if (!captcha.equals(safeKey.getValue())) {
                return Message.error("无效验证码");
            }
            Member m = memberService.findByMobile(safeKey.getKey());
            if (m!=null) {
                m.setUsername(null);
                m.setMobile(null);
                memberService.save(m);
            }
            member.setUsername(safeKey.getKey());
            member.setMobile(safeKey.getKey());
            memberService.save(member);
            for (Card card:member.getCards()) {
                card.setMobile(member.getMobile());
                cardService.update(card);
            }
            return Message.success("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("注册失败");
        }
    }

    /**
     * 验证合法性
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    @ResponseBody
    public Message captcha(HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MOBILE_BIND_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member =memberService.getCurrent();
        try {
            if (member.getMobile()!=null && !member.getMobile().equals(safeKey.getKey())) {
                return Message.error("无效验证码");
            }
            String captcha = rsaService.decryptParameter("captcha", request);
            rsaService.removePrivateKey(request);
            if (member==null) {
                return Message.error("无效验证码");
            }
            if (captcha==null) {
                return Message.error("无效验证码");
            }
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            if (!captcha.equals(safeKey.getValue())) {
                return Message.error("无效验证码");
            }
            return Message.success(member,"验证成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("验证失败");
        }
    }

}