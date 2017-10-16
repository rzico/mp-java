package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
 
@Controller("weexMemberArticleController")
@RequestMapping("/weex/member/article")
public class ArticleController extends BaseController {

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

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "articleProductServiceImpl")
    private ArticleProductService articleProductService;

    @Resource(name = "articleLaudServiceImpl")
    private ArticleLaudService articleLaudService;


    /**
     *  文章列表,带分页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleCatalogId,Long timeStamp,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleCatalog  articleCatalog = articleCatalogService.find(articleCatalogId);
        List<Filter> filters = new ArrayList<Filter>();
        if (articleCatalog!=null) {
            filters.add(new Filter("articleCatalog", Filter.Operator.eq,articleCatalog));
        }
        if (timeStamp!=null) {
            filters.add(new Filter("modifyDate", Filter.Operator.le,new Date(timeStamp)));
        }
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<Article> page = articleService.findPage(null,null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     * 获取文章编辑信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }

        ArticleModel model =new ArticleModel();
        model.bind(article);
        return Message.bind(model,request);
   }

    /**
     * 获取文章发布属性
     */
    @RequestMapping(value = "/option", method = RequestMethod.GET)
    @ResponseBody
    public Message option(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        ArticleOptionModel model =new ArticleOptionModel();
        model.bind(article);
        return Message.bind(model,request);
    }

    /**
     * 保存文章信息
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String body, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleModel model = JsonUtils.toObject(body,ArticleModel.class);
        Long id = model.getId();
        String title = model.getTitle();
        String author = member.getNickName();
        String thumbnail = model.getThumbnail();
        String music = JsonUtils.toJson(model.getMusic());
        String content = JsonUtils.toJson(model.getTemplates());

        String votes = JsonUtils.toJson(model.getVotes());
        Article article = null;
        if (id!=null) {
            article = articleService.find(model.getId());
        }
        if (article==null) {
            article = new Article();
            article.setDeleted(false);
            article.setHits(0L);
            article.setFavorite(0L);
            article.setLaud(0L);
            article.setReview(0L);
            article.setIsDraft(true);
            ArticleOptions options = new ArticleOptions();
            article.setArticleOptions(options);
            article.getArticleOptions().setAuthority(ArticleOptions.Authority.isPublic);
            article.getArticleOptions().setIsExample(false);
            article.getArticleOptions().setIsPitch(false);
            article.getArticleOptions().setIsPublish(false);
            article.getArticleOptions().setIsReview(true);
            article.getArticleOptions().setIsTop(false);
            article.getArticleOptions().setIsReward(false);
            article.setTemplate(templateService.findDefault(Template.Type.article));
        }
        article.setTitle(title);
        article.setAuthor(author);
        article.setThumbnail(thumbnail);
        article.setMusic(music);
        article.setContent(content);
        article.setVotes(votes);
        article.setMember(member);
        article.setMediaType(Article.MediaType.image);

        articleService.save(article);

        for (ArticleProduct product:article.getProducts()) {
            articleProductService.delete(product);
        }
        if (model.getProducts()!=null) {
            for (ProductViewModel product : model.getProducts()) {
                Product prod = productService.find(product.getId());
                if (prod == null) {
                    return Message.error("无效商品");
                }
                ArticleProduct ap = new ArticleProduct();
                ap.setArticle(article);
                ap.setProduct(prod);
                articleProductService.save(ap);
            }
        }

        ArticleModel entityModel =new ArticleModel();
        entityModel.bind(article);
        return Message.success(entityModel,"保存成功");

    }

    /**
     * 发布文章信息
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public Message publish(Long id,Long articleCategoryId,Long articleCatalogId,ArticleOptions articleOptions,Location location,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        if (articleOptions!=null) {
            article.getArticleOptions().setIsReward(articleOptions.getIsReward());
            article.getArticleOptions().setIsTop(articleOptions.getIsTop());
            article.getArticleOptions().setIsReview(articleOptions.getIsReview());
            article.getArticleOptions().setIsPublish(articleOptions.getIsPublish());
            article.getArticleOptions().setAuthority(articleOptions.getAuthority());
            article.getArticleOptions().setPassword(articleOptions.getPassword());
        }
        if (location!=null && location.getLat()!=0 && location.getLng()!=0) {
            article.setLocation(location);
        }
        if (articleCategoryId!=null) {
            article.setArticleCategory(articleCategoryService.find(articleCategoryId));
        }
        if (articleCatalogId!=null) {
            article.setArticleCatalog(articleCatalogService.find(articleCatalogId));
        }
        article.setIsDraft(false);
        articleService.save(article);
        return Message.success("发布成功");
    }

    /**
     * 修改显示模版
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,Long templateId,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        if (templateId!=null) {
            article.setTemplate(templateService.find(templateId));
        }
        articleService.save(article);
        return Message.success("发布成功");
    }

    /**
     *  文章点赞
     */
    @RequestMapping(value = "/laud", method = RequestMethod.POST)
    @ResponseBody
    public Message laud(Long articleId,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleLaud laud = new ArticleLaud();
        laud.setArticle(article);
        laud.setIp(request.getRemoteAddr());
        laud.setMember(member);
        articleLaudService.save(laud);

        article.setLaud(article.getLaud()+1);
        articleService.save(article);
        return Message.success("点赞成功");

    }

    /**
     *  文章点击
     */
    @RequestMapping(value = "/hits", method = RequestMethod.POST)
    @ResponseBody
    public Message hits(Long articleId,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        article.setHits(article.getHits()+1);
        articleService.save(article);
        return Message.success("点击成功");

    }

}