package net.wit.controller.weex;

import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
import net.wit.entity.BaseEntity.Save;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.StringUtils;
import net.wit.util.WebUtils;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.util.WeixinUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: LoginController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexLoginController")
@RequestMapping("/weex/login")
public class LoginController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

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
     * 手机验证码登录时，发送验证码
     * mobile 手机号
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(HttpServletRequest request) {
        String m = rsaService.decryptParameter("mobile", request);
        rsaService.removePrivateKey(request);
        if (m==null) {
            return Message.error("无效手机号");
        }
        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(m);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_LOGIN_CAPTCHA,JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(m);
        smsSend.setContent("验证码 :" + securityCode + ",只用于登录使用。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     * 验证码登录
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    @ResponseBody
    public Message captcha(HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MOBILE_LOGIN_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        redisService.remove(Member.MOBILE_LOGIN_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member =memberService.findByMobile(safeKey.getKey());
        try {
            String captcha = rsaService.decryptParameter("captcha", request);
            logger.debug("登录验证码："+captcha);
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
            if (member==null) {
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
            }

            Principal principal = new Principal(member.getId(),member.getUsername());
            redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
            String xuid = request.getHeader("x-uid");
            if (xuid!=null) {
                Member u = memberService.findByUUID(xuid);
                if (u!=null && !u.equals(member)) {
                    u.setUuid(null);
                    memberService.save(u);
                }
                member.setUuid(xuid);
            }
            member.setLoginDate(new Date());
            memberService.save(member);
            return Message.success(Message.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("登录失败");
        }
    }

    /**
     * 账号密码登录
     */
	@RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
	public Message login(String username, HttpServletRequest request){
		Member member =memberService.findByUsername(username);
        try {
            String password = rsaService.decryptParameter("enPassword", request);
            rsaService.removePrivateKey(request);
            if (member==null) {
                return Message.error("无效账号");
            }
            if (password==null) {
                return Message.error("无效密码");
            }
            if (!MD5Utils.getMD5Str(password).equals(member.getPassword())) {
                return Message.error("无效密码");
            }
            Principal principal = new Principal(member.getId(),member.getUsername());
            redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
            String xuid = request.getHeader("x-uid");
            if (xuid!=null) {
                Member u = memberService.findByUUID(xuid);
                if (u!=null && !u.equals(member)) {
                    u.setUuid(null);
                    memberService.save(u);
                }
                member.setUuid(xuid);
            }
            member.setLoginDate(new Date());
            memberService.save(member);
            return Message.success(Message.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("登录失败");
        }
	}

    /**
     *  体验账号一键登录
     */
    @RequestMapping(value = "demo", method = RequestMethod.GET)
    @ResponseBody
    public Message demo(HttpServletRequest request){
        Member member =memberService.find(1L);
        try {
            Principal principal = new Principal(member.getId(),member.getUsername());
            redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
            return Message.success(Message.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("登录失败");
        }
    }
    /**
     * 微信登录
     */
    @RequestMapping(value = "/weixin", method = RequestMethod.POST)
    @ResponseBody
    public Message weixin(String code,HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        AccessToken token = WeixinUtil.getOauth2AccessToken(bundle.getString("app.appid"), bundle.getString("app.secret"), code);
        String openId = null;
        if (token!=null) {
            openId = token.getOpenid();
         } else {
            return Message.error("无效授权码");
        }
        JSONObject userinfo = WeixinUtil.getUserInfoByCode(token.getToken(), openId);
        String nickName=null;
        String headImg=null;
        String unionId=null;
        if (userinfo.containsKey("nickname")) {
            nickName = StringUtils.filterEmoji(userinfo.getString("nickname"));
            headImg = userinfo.getString("headimgurl");
            if (userinfo.containsKey("unionid")) {
                unionId = userinfo.getString("unionid");
            } else {
                return Message.error("获取用户信息失败");
            }
        } else {
            return Message.error("获取用户信息失败");
        }

        BindUser bindUser = bindUserService.findUnionId(unionId, BindUser.Type.weixin);
        Member member = null;
        if (bindUser!=null) {
            member = bindUser.getMember();
        }
        if (member==null) {
            member = new Member();
            member.setNickName(nickName);
            member.setLogo(headImg);
            member.setPoint(0L);
            member.setBalance(BigDecimal.ZERO);
            member.setIsEnabled(true);
            member.setIsLocked(false);
            member.setLoginFailureCount(0);
            member.setRegisterIp(request.getRemoteAddr());
            memberService.save(member);
        }
        try {
            bindUser = bindUserService.findOpenId(openId,bundle.getString("app.appid"),BindUser.Type.weixin);
            if (bindUser==null) {
                bindUser = new BindUser();
                bindUser.setAppId(bundle.getString("app.appid"));
                bindUser.setType(BindUser.Type.weixin);
                bindUser.setMember(member);
                bindUser.setUnionId(unionId);
                bindUser.setOpenId(openId);
            } else {
                bindUser.setMember(member);
                bindUser.setUnionId(unionId);
            }
            bindUserService.save(bindUser);

            Principal principal = new Principal(member.getId(),member.getUsername());
            redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
            String xuid = request.getHeader("x-uid");
            if (xuid!=null) {
                Member u = memberService.findByUUID(xuid);
                if (u!=null && !u.equals(member)) {
                    u.setUuid(null);
                    memberService.save(u);
                }
                member.setUuid(xuid);
            }
            member.setLoginDate(new Date());
            memberService.save(member);
            return Message.success(Message.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("登录失败");
        }
    }

    /**
     * 注销会话
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public
    Message logout(HttpServletRequest request, HttpSession session) {
        Member member = memberService.getCurrent();
        if (member!=null) {
            member.setUuid(null);
            memberService.save(member);
        }
        redisService.remove(Member.PRINCIPAL_ATTRIBUTE_NAME);
        return Message.success("注销成功");
    }


    /**
     * 检查是否登录
     */
    @RequestMapping("/isAuthenticated")
    @ResponseBody
    public Message authorized(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> data = new HashMap<String,Object>();
        Member member = memberService.getCurrent();
        data.put("loginStatus",member!=null);
        if (member!=null) {
            data.put("uid", member.getId());
        }
        return Message.success(data,"success");
    }

}