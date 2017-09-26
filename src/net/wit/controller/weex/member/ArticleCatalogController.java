package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleCatalogModel;
import net.wit.controller.weex.model.ArticleCategoryModel;
import net.wit.entity.ArticleCatalog;
import net.wit.entity.ArticleCategory;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ArticleCatalogController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberArticleCatalogController")
@RequestMapping("/weex/member/article_catalog")
public class ArticleCatalogController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    /**
     *  分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<ArticleCatalog> categories = articleCatalogService.findList(null,null,filters,null);
        return Message.success(ArticleCatalogModel.bindList(categories),"获取成功");
    }

}