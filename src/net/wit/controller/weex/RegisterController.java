package net.wit.controller.weex;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.MemberModel;
import net.wit.entity.Member;
import net.wit.entity.Redis;
import net.wit.entity.SafeKey;
import net.wit.entity.Smssend;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @ClassName: RegisterController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexRegisterController")
@RequestMapping("/weex/register")
public class RegisterController extends BaseController {

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


    /**
     * 发送验证码
     * mobile 手机号
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(String mobile, HttpServletRequest request) {
        Member member = memberService.findByMobile(mobile);
        if (member!=null) {
            return Message.error("当前手机已经注册");
        }
        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(mobile);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_LOGIN_CAPTCHA,JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",只用于注册账号。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     * 注册用户
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message captcha(HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MOBILE_LOGIN_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        redisService.remove(Member.MOBILE_LOGIN_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member =memberService.findByMobile(safeKey.getKey());
        if (member!=null) {
            return Message.error("当前手机已经注册");
        }
        try {
            String captcha = rsaService.decryptParameter("captcha", request);
            rsaService.removePrivateKey(request);

            if (captcha==null) {
                return Message.error("无效验证码");
            }
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            if (captcha.equals(safeKey.getValue())) {
                return Message.error("无效验证码");
            }

            member = new Member();
            member.setUsername(safeKey.getKey());
            member.setMobile(safeKey.getKey());
            member.setNickName(null);
            member.setLogo(null);
            member.setPoint(0L);
            member.setBalance(BigDecimal.ZERO);
            member.setIsEnabled(true);
            member.setIsLocked(false);
            member.setLoginFailureCount(0);
            member.setRegisterIp(request.getRemoteAddr());
            memberService.save(member);

            String xuid = request.getHeader("x-uid");
            if (xuid!=null) {
                member.setUuid(xuid);
            }
            member.setLoginDate(new Date());
            memberService.save(member);
            MemberModel model = new MemberModel();
            model.bind(member);
            return Message.success(model,"注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("注册失败");
        }
    }

}