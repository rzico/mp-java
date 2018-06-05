package net.wit.controller.applet;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

@Controller("appletArticleController")
@RequestMapping("applet/article")
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

    @Resource(name = "articleLaudServiceImpl")
    private ArticleLaudService articleLaudService;

    @Resource(name = "articleFavoriteServiceImpl")
    private ArticleFavoriteService articleFavoriteService;

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "dragonServiceImpl")
    private DragonService dragonService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    /**
     * 文章预览信息
     */
    @RequestMapping(value = "/vview", method = RequestMethod.GET)
    @ResponseBody
    public Message vview(Long id,Long dragonId,HttpServletRequest request){
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
        Goods goods = article.getGoods();
        if (goods!=null) {
            goods.setHits(goods.getHits()+1);
            goodsService.update(goods);
        }
        ArticleViewModel model =new ArticleViewModel();
        model.bind(article,member);

        for (ArticleContentViewModel m:model.getTemplates()) {
            if (m.getMediaType().equals(Article.MediaType.product)) {
                goods = goodsService.find(m.getId());
                if (goods!=null) {
                    Product product = goods.product();
                    if (product!=null) {
                        m.setName(product.getName());
                        m.setPrice(product.getPrice());
                        m.setThumbnail(product.getThumbnail());
                        m.setMarketPrice(product.getMarketPrice());
                        if (member!=null) {
                            if (member.leaguer(product.getMember())) {
                                m.setRebate(product.calcRebate());
                            }
                        } else {
                            m.setRebate(BigDecimal.ZERO);
                        }
                    }
                }
            }
        }

        Dragon dragon = dragonService.find(article,member);

        if (dragon==null) {
            if (dragonId != null) {
                dragon = dragonService.find(dragonId);
            }
        }

        if (dragon==null) {
            dragon = dragonService.find(article,article.getMember());
        }

        if (member!=null) {

            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter("member", Filter.Operator.eq,member));
            filters.add(new Filter("article", Filter.Operator.eq,article));
            List<ArticleFavorite> favorites = articleFavoriteService.findList(null,null,filters,null);
            model.setHasFavorite(favorites.size()>0);

            List<Filter> laudfilters = new ArrayList<Filter>();
            laudfilters.add(new Filter("member", Filter.Operator.eq,member));
            laudfilters.add(new Filter("article", Filter.Operator.eq,article));
            List<ArticleLaud> lauds = articleLaudService.findList(null,null,laudfilters,null);
            model.setHasLaud(lauds.size()>0);

            MemberFollow memberFollow = memberFollowService.find(member, article.getMember());
            model.setHasFollow(memberFollow!=null);

        }

        if (dragon!=null && dragon.getStatus().equals(Dragon.Status.normal)) {
            model.setDragonName(dragon.getMember().displayName());
            model.setDragonId(dragon.getId());

            Long dg = orderService.count(new Filter("dragon", Filter.Operator.eq, dragon));
            model.setDragon(dg);

        }

        return Message.bind(model,request);
    }

    /**
     * 文章预览详情
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,Long dragonId,HttpServletRequest request){
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
        Goods goods = article.getGoods();
        if (goods!=null) {
            goods.setHits(goods.getHits()+1);
            goodsService.update(goods);
        }
        ArticleViewModel model =new ArticleViewModel();
        model.bind(article,member);

        for (ArticleContentViewModel m:model.getTemplates()) {
            if (m.getMediaType().equals(Article.MediaType.product)) {
                goods = goodsService.find(m.getId());
                if (goods!=null) {
                    Product product = goods.product();
                    if (product!=null) {
                        m.setName(product.getName());
                        m.setPrice(product.getPrice());
                        m.setThumbnail(product.getThumbnail());
                        m.setMarketPrice(product.getMarketPrice());
                    }
                }
            }
        }

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("article",model);

        Dragon dragon = dragonService.find(article,member);

        if (dragon==null) {
            if (dragonId != null) {
                dragon = dragonService.find(dragonId);
            }
        }

        if (dragon==null) {
            dragon = dragonService.find(article,article.getMember());
        }

        ArticlePreviewModel option =new ArticlePreviewModel();
        option.bind(article);
        if (member!=null) {

            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter("member", Filter.Operator.eq,member));
            filters.add(new Filter("article", Filter.Operator.eq,article));
            List<ArticleFavorite> favorites = articleFavoriteService.findList(null,null,filters,null);
            option.setHasFavorite(favorites.size()>0);

            List<Filter> laudfilters = new ArrayList<Filter>();
            laudfilters.add(new Filter("member", Filter.Operator.eq,member));
            laudfilters.add(new Filter("article", Filter.Operator.eq,article));
            List<ArticleLaud> lauds = articleLaudService.findList(null,null,laudfilters,null);
            option.setHasLaud(lauds.size()>0);

            MemberFollow memberFollow = memberFollowService.find(member, article.getMember());
            option.setHasFollow(memberFollow!=null);

        }
        data.put("option",option);

        if (dragon!=null && dragon.getStatus().equals(Dragon.Status.normal)) {
            model.setDragonName(dragon.getMember().displayName());
            model.setDragonId(dragon.getId());

            Long dg = orderService.count(new Filter("dragon", Filter.Operator.eq, dragon));
            model.setDragon(dg);

        }

        return Message.bind(data,request);
   }

     /**
     * 获取显示模版
     */
    @RequestMapping(value = "/template", method = RequestMethod.POST)
    @ResponseBody
    public Message template(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        String sn="1001";
        if (article.getTemplate()==null) {
            sn = article.getTemplate().getSn();
        }
        return Message.success((Object)sn,"发布成功");
    }

    /**
     *  接龙订单
     */

    @RequestMapping(value = "/dragon", method = RequestMethod.GET)
    public @ResponseBody
    Message  dragon(Long dragonId,Pageable pageable, HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        List<Filter> filters = new ArrayList<Filter>();
        if (dragonId!=null) {
            Dragon dragon = dragonService.find(dragonId);
            if (dragon!=null) {
                filters.add(new Filter("dragon", Filter.Operator.eq,dragon));
            }
        }

        pageable.setFilters(filters);
        pageable.setOrderDirection(net.wit.Order.Direction.desc);
        pageable.setOrderProperty("modifyDate");
        Page<net.wit.entity.Order> page = orderService.findPage(null,null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(OrderListModel.bindList(page.getContent()));
        return Message.bind(model,request);

    }

    /**
     *  分类查询列表
     *  会员 id
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,String keyword, Long tagId, Boolean isTop, Long articleCategoryId, Long articleCatalogId, Article.ArticleType mediaType, Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (articleCategoryId!=null) {
            ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
            filters.add(new Filter("articleCategory", Filter.Operator.eq,articleCategory));
        }
        if (articleCatalogId!=null) {
            ArticleCatalog  articleCatalog = articleCatalogService.find(articleCatalogId);
            filters.add(new Filter("articleCatalog", Filter.Operator.eq,articleCatalog));
        }
        if (isTop != null) {
            filters.add(new Filter("isTop", Filter.Operator.eq,isTop));
        }
        if (authorId!=null) {
            Member member = memberService.find(authorId);
            filters.add(new Filter("member", Filter.Operator.eq,member));
        } else {
            filters.add(new Filter("isAudit", Filter.Operator.eq, true));
        }
        List<Tag> tags = null;
        if (tagId!=null) {
            tags = tagService.findList(tagId);
        }
        filters.add(new Filter("authority", Filter.Operator.eq, Article.Authority.isPublic));
        filters.add(new Filter("isPublish", Filter.Operator.eq, true));
        if (mediaType==null) {
            filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.product));
            filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.html));
        } else {
            filters.add(new Filter("mediaType", Filter.Operator.eq, mediaType));
        }
        if (keyword!=null) {
            filters.add(Filter.like("title", "%" + keyword + "%"));
        }
        pageable.setFilters(filters);
        pageable.setOrderProperty("modifyDate");
        pageable.setOrderDirection(Order.Direction.desc);
        Page<Article> page = articleService.findPage(null,null,tags,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  接龙列表
     *  会员 id
     */
    @RequestMapping(value = "/dragonlist", method = RequestMethod.GET)
    @ResponseBody
    public Message dragonList(Long authorId,Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (authorId!=null) {
            Member member = memberService.find(authorId);
            filters.add(new Filter("member", Filter.Operator.eq,member));
        } else {
            filters.add(new Filter("isAudit", Filter.Operator.eq, true));
        }
        filters.add(new Filter("authority", Filter.Operator.eq, Article.Authority.isPublic));
        filters.add(new Filter("isPublish", Filter.Operator.eq, true));
        filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.product));
        filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.html));
        pageable.setFilters(filters);
        pageable.setOrderProperty("modifyDate");
        pageable.setOrderDirection(Order.Direction.desc);
        Page<Article> page = articleService.findPage(null,null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  热点查询列表
     *  会员 id
     */
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    @ResponseBody
    public Message hot(Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("isAudit", Filter.Operator.eq,true));
        filters.add(new Filter("isPublish", Filter.Operator.eq, true));
        filters.add(new Filter("isPitch", Filter.Operator.eq, true));
        filters.add(new Filter("authority", Filter.Operator.eq, Article.Authority.isPublic));
        filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.product));
        filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.html));

        List<Tag> tags = null;
//        tags = tagService.findList(4L);
        pageable.setFilters(filters);
        pageable.setOrderProperty("hits");
        pageable.setOrderDirection(Order.Direction.desc);
        Page<Article> page = articleService.findPage(null,null,tags,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  圈子查询列表
     *  会员 id
     */
    @RequestMapping(value = "/circle", method = RequestMethod.GET)
    @ResponseBody
    public Message circle(Long id,Pageable pageable, HttpServletRequest request){
        List<Tag> tags = null;
        Member member = null;
        List<Filter> filters = new ArrayList<Filter>();
        if (id!=null) {
            member = memberService.find(id);
        } else {
            filters.add(new Filter("isAudit", Filter.Operator.eq,true));
            tags = tagService.findList(4L,5L);
        }
        filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.product));
        filters.add(new Filter("mediaType", Filter.Operator.ne, Article.ArticleType.html));
        filters.add(new Filter("isPublish", Filter.Operator.eq, true));
        filters.add(new Filter("authority", Filter.Operator.eq, Article.Authority.isPublic));
        pageable.setFilters(filters);
        Page<Article> page = articleService.findCircle(member,tags,pageable);
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