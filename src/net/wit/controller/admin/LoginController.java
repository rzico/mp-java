
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
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.store.Lock.With;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
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

            if (!captchaService.isValid(captchaId, captcha)) {
                return Message.error("admin.captcha.invalid");
            }
            //用户校验
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                 return Message.error("无效用户名");
            }
            Setting setting = SettingUtils.get();
            if (admin.getIsLocked()) {
                int loginFailureLockTime = setting.getAccountLockTime();
                if (loginFailureLockTime == 0) {
                    return Message.error("用户已锁定，请稍候再重试");
                }
                Date lockedDate = admin.getLockedDate();
                Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
                if (new Date().after(unlockDate)) {
                    admin.setLoginFailureCount(0);
                    admin.setIsLocked(false);
                    admin.setLockedDate(null);
                    adminService.update(admin);
                } else {
                    return Message.error("用户已锁定，请稍候再重试");
                }
            }
            if (!admin.getPassword().equals(password)) {
                int loginFailureCount = admin.getLoginFailureCount() + 1;
                if (loginFailureCount >= setting.getAccountLockCount()) {
                    admin.setIsLocked(true);
                    admin.setLockedDate(new Date());
                    return Message.error("密码错误次数过多，账号已锁定，请休息会再试。");
                }

                admin.setLoginFailureCount(loginFailureCount);
                adminService.update(admin);
                //密码错误
                return Message.error("登录密码无效");
            }
            //用户禁用
            if(!admin.getIsEnabled()){
                return Message.error("该用户已经被禁用");
            }
            //登出
            SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
            //登录后存放进shiro token
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(admin.getUsername(), admin.getPassword());
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            //更新数据库记录
            SimpleDateFormat  reqTime =new SimpleDateFormat("yyyyMMddHHmmss");
            admin.setLoginDate(new Date());
            adminService.update(admin);
            //登录成功后跳转到successUrl配置的链接
            return Message.success("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            return Message.error("系统繁忙");
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