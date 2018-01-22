package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
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
import java.util.Date;


/**
 * @ClassName: PasswordController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberPasswordController")
@RequestMapping("/weex/member/password")
public class PasswordController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

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
    public Message sendMobile(HttpServletRequest request) {
        Member member = memberService.getCurrent();
        String mobile = member.getMobile();
        if (mobile==null) {
            mobile = rsaService.decryptParameter("mobile", request);
            rsaService.removePrivateKey(request);
        }
        if (mobile==null) {
            return Message.error("无效手机号");
        }
        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);
        SafeKey safeKey = new SafeKey();
        safeKey.setKey(mobile);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MEMBER_PASSWORD_CAPTCHA,JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",只用于修改密码。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     * 检查手机号
     * mobile 手机号
     */
    @RequestMapping(value = "/check_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message checkMobile(HttpServletRequest request) {
        Member member = memberService.getCurrent();
        String mobile = rsaService.decryptParameter("mobile", request);
        if (member.getMobile()!=null) {
            return Message.error("已绑定手机");
        }
        rsaService.removePrivateKey(request);
        if (memberService.findByMobile(mobile)!=null) {
           if (!mobile.equals(member.getMobile())) {
               return Message.error("手机号已被使用");
           }
        }
        return Message.success("检查通过成功");
    }

    /**
     * 验证合法性
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    @ResponseBody
    public Message captcha(HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MEMBER_PASSWORD_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member =memberService.getCurrent();
        try {
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

    /**
     * 修改密码
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MEMBER_PASSWORD_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        redisService.remove(Member.MEMBER_PASSWORD_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member =memberService.getCurrent();
        try {
            String password = rsaService.decryptParameter("enPassword", request);
            rsaService.removePrivateKey(request);
            if (member==null) {
                return Message.error("无效验证码");
            }
            safeKey.setExpire(DateUtils.addMinutes(safeKey.getExpire(),120));
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            member.setPassword(MD5Utils.getMD5Str(password));
            memberService.save(member);

            Admin admin = adminService.findByMember(member);
            if (admin!=null) {
                admin.setPassword(member.getPassword());
                adminService.update(admin);
            }

            return Message.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("修改失败");
        }
	}

}