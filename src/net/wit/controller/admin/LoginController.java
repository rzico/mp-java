
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.*;
import net.wit.AuthenticationToken;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.store.Lock.With;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller - 后台登录
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminLoginController")
@RequestMapping("/admin/login")
public class LoginController extends BaseController {

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    /**
     * 登录页面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String redirectUrl, String type, HttpServletRequest request, ModelMap model) {
        Setting setting = SettingUtils.get();
        if (redirectUrl != null && !redirectUrl.equalsIgnoreCase(setting.getSiteUrl()) && !redirectUrl.startsWith(request.getContextPath() + "/") && !redirectUrl.startsWith(setting.getSiteUrl() + "/")) {
            redirectUrl = null;
        }
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "/admin/login/index";
    }

    /**
     * 登录提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public
    Message submit(String captchaId, String captcha,Boolean rememberMe, String username, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            String password = rsaService.decryptParameter("enPassword", request);
            rsaService.removePrivateKey(request);
            //登出
            SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
            if (username.length()==11) {
                Member member = memberService.findByMobile(username);
                if (member!=null) {
                    Admin admin = adminService.findByMember(member);
                    username = admin.getUsername();
                }
            }
            //登录后存放进shiro token
            AuthenticationToken authenticationToken = new AuthenticationToken(username, password,captchaId,captcha,rememberMe,request.getRemoteHost());
            Subject subject = SecurityUtils.getSubject();
            subject.login(authenticationToken);
            //登录成功后跳转到successUrl配置的链接
            return Message.success("success");
        } catch (UnsupportedTokenException ex) {
            return Message.error("无效验证码");
        } catch (UnknownAccountException ex) {
            return Message.error("无效账号");
        } catch (DisabledAccountException ex) {
            return Message.error("账号被关闭");
        } catch (IncorrectCredentialsException ex) {
            return Message.error("密码不正确");
        } catch (Exception e) {
            return Message.error("登录出错了");
        }
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public String loginout(String redirectUrl, String type, HttpServletRequest request, ModelMap model) {
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        return "/admin/login/index";
    }

}