package net.wit.controller.usered;

import net.wit.Setting;
import net.wit.util.SettingUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by Eric on 2018/3/12.
 */
@Controller("useredLoginController")
@RequestMapping("/usered/login")
public class LoginController {

    /**
     * 登录页面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model) {
        return "/usered/login/index";
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public String loginout(String redirectUrl, String type, HttpServletRequest request, ModelMap model) {
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        return "/usered/login/index";
    }
}
