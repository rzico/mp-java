package net.wit.controller.usered;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.entity.Article;
import net.wit.entity.ArticleCatalog;
import net.wit.entity.Member;
import net.wit.entity.Redis;
import net.wit.service.ArticleCatalogService;
import net.wit.service.ArticleService;
import net.wit.service.MemberService;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2018/3/12.
 */
@Controller("useredIndexController")
@RequestMapping("/usered/index")
public class IndexController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;
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
        Member member = memberService.getCurrent();
        //筛选
        Pageable pageable = new Pageable();
        ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
        Filter memberFilter = new Filter("member", Filter.Operator.eq, member);
        filters.add(memberFilter);
        Page<ArticleCatalog> page = articleCatalogService.findPage(null, null, pageable);
        List<ArticleCatalog> list = page.getContent();
        map.addAttribute("articleCatalog", list);
        map.addAttribute("principal",principal);
        return "/usered/article/index";
    }

    /**
     * 桌面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/usered/article/index";
    }
}
