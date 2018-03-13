package net.wit.controller.usered;

import net.wit.Message;
import net.wit.Principal;
import net.wit.entity.Member;
import net.wit.entity.Redis;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Eric on 2018/3/13.
 */
@Controller("useredArticle1Controller")
@RequestMapping("/usered/article1")
public class Article1Controller {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    /**
     * 桌面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map) {
        //获取已登录的身份信息
        Redis redis = redisService.findKey(Member.PRINCIPAL_ATTRIBUTE_NAME);
        if(redis==null){
            return "/usered/login/index";
        }
        Principal principal= JsonUtils.toObject(redis.getValue(),Principal.class);

        return "/usered/article/index";
    }
}
