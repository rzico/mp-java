package net.wit.controller.weex;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.controller.admin.model.PageModel;
import net.wit.controller.weex.model.ArticleCategoryModel;
import net.wit.controller.weex.model.MessageModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: ArticleCategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexArticleCategoryController")
@RequestMapping("/weex/article_category")
public class ArticleCategoryController extends BaseController {

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

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    /**
     *  分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request){
        List<ArticleCategory> categories = articleCategoryService.findAll();
        return Message.success(ArticleCategoryModel.bindList(categories),"获取成功");
    }

}