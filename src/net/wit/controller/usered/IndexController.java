package net.wit.controller.usered;

import net.wit.Principal;
import net.wit.entity.Member;
import net.wit.entity.Redis;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by Eric on 2018/3/12.
 */
@Controller("useredIndexController")
@RequestMapping("/usered/index")
public class IndexController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;
    /**
     * 登录成功页面
     */
    @RequestMapping(value = "/main1", method = RequestMethod.GET)
    public String main(ModelMap map) {
        //获取已登录的身份信息
        Redis redis = redisService.findKey(Member.PRINCIPAL_ATTRIBUTE_NAME);
        if(redis==null){
            return "/usered/login/index";
        }
        Principal principal= JsonUtils.toObject(redis.getValue(),Principal.class);
        map.addAttribute("principal",principal);
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
