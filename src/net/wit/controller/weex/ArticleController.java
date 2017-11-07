package net.wit.controller.weex;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.entity.Article;
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
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexArticleController")
@RequestMapping("weex/article")
public class ArticleController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    /**
     * 文章预览信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        ArticleViewModel model =new ArticleViewModel();
        model.bind(article);
        return Message.bind(model,request);
   }

    /**
     *  分类查询列表
     *  会员 id
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Long articleCategoryId,Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (articleCategoryId!=null) {
            ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
            filters.add(new Filter("articleCategory", Filter.Operator.eq,articleCategory));
        }
        if (authorId!=null) {
            Member member = memberService.find(authorId);
            filters.add(new Filter("member", Filter.Operator.eq,member));
        }
        pageable.setFilters(filters);
        Page<Article> page = articleService.findPage(null,null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }
}