package net.wit.controller.usered;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Eric on 2018/3/12.
 */
@Controller("useredIndexController")
@RequestMapping("/usered/index")
public class IndexController {

    /**
     * 登录成功页面
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model) {
        return "/usered/index/main";
    }

    /**
     * 桌面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/usered/index/index";
    }
}
