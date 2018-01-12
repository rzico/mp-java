package net.wit.controller.website;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.ArticleModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.controller.model.GoodsListModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteArticleController")
@RequestMapping("website/article")
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

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

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
        Member member = memberService.getCurrent();
        if (member!=null) {
            if (!article.getMember().equals(member)) {
                article.setHits(article.getHits()+1);
                articleService.update(article);
            }
        } else {
            article.setHits(article.getHits()+1);
            articleService.update(article);
        }
        ArticleViewModel model =new ArticleViewModel();
        model.bind(article);
        return Message.bind(model,request);
   }

    /**
     *  文章列表,带分页
     *  会员 id
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Boolean isTop,Long articleCategoryId,Long articleCatalogId,Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (articleCategoryId!=null) {
            ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
            filters.add(new Filter("articleCategory", Filter.Operator.eq,articleCategory));
        }
        if (authorId!=null) {
            Member member = memberService.find(authorId);
            filters.add(new Filter("member", Filter.Operator.eq,member));
        }
        if (articleCatalogId!=null) {
            ArticleCatalog  articleCatalog = articleCatalogService.find(articleCatalogId);
            filters.add(new Filter("articleCatalog", Filter.Operator.eq,articleCatalog));
        }
        if (isTop != null) {
            filters.add(new Filter("isTop", Filter.Operator.eq,isTop));
        }
        filters.add(new Filter("isAudit", Filter.Operator.eq,true));
        filters.add(new Filter("authority", Filter.Operator.eq, Article.Authority.isPublic));

        pageable.setFilters(filters);
        Page<Article> page = articleService.findPage(null,null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  商品信息
     */
    @RequestMapping(value = "/goods", method = RequestMethod.GET)
    @ResponseBody
    public Message goods(Long id,HttpServletRequest request){
        Goods goods = goodsService.find(id);
        if (goods==null) {
            return Message.error("无效商品编号");
        }
        GoodsListModel model = new GoodsListModel();
        model.bind(goods);
        return Message.bind(model,request);
    }

}