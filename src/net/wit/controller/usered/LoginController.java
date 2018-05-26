package net.wit.controller.usered;

import net.wit.plat.weixin.util.WeixinApi;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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
        model.addAttribute("login",WeixinApi.askCode("wxbdc5610cc59c1631","https%3A%2F%2Fpassport.yhd.com%2Fwechat%2Fcallback.do","3d6be0a4035d839573b04816624a415e"));
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
