package net.wit.controller.usered;

import net.sf.json.JSONArray;
import net.wit.*;
import net.wit.Message;
import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.ArticleModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2018/3/13.
 */
@Controller("useredArticle1Controller")
@RequestMapping("/usered/article1")
public class Article1Controller {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;


    /**
     * 桌面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map) {
        //获取已登录的身份信息
        Redis redis = redisService.findKey(Member.PRINCIPAL_ATTRIBUTE_NAME);
        if (redis == null) {
            return "/usered/login/index";
        }
        Member member = memberService.getCurrent();
        //筛选
        Pageable pageable = new Pageable();
        ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
        Filter memberFilter = new Filter("member", Filter.Operator.eq, member);
        filters.add(memberFilter);
        Page<ArticleCatalog> page = articleCatalogService.findPage(null, null, pageable);
        List<ArticleCatalog> list = page.getContent();
        map.addAttribute("articleCatalog", list);
        return "/usered/article/index";
    }

    /**
     * 文集列表
     */
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String open(Long id, Long start, Long draw, String seachValue, Pageable pageable, HttpServletRequest request, ModelMap map) {
        //获取已登录的身份信息
        Redis redis = redisService.findKey(Member.PRINCIPAL_ATTRIBUTE_NAME);
        if (redis == null) {
            return "/usered/login/index";
        }
        Member member = memberService.getCurrent();
        //筛选
        pageable.setPageSize(8);
        if (start != null) {
            pageable.setPageStart(start.intValue());
        }
        if (draw != null) {
            pageable.setDraw(draw.intValue());
        }
        //筛选
        ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
        Filter memberFilter = null;
        if (id != null) {
            memberFilter = new Filter("articleCatalog", Filter.Operator.eq, articleCatalogService.find(id));
        } else {
            memberFilter = new Filter("member", Filter.Operator.eq, member);
        }
        filters.add(memberFilter);

        if (seachValue != null) {
            filters.add(new Filter("title", Filter.Operator.like, "%" + seachValue + "%"));
        }

        Page<Article> page = articleService.findPage(null, null, null, pageable);
        map.addAttribute("page", page);
        if (id != null) {
            map.addAttribute("id", id);
        }
        return "/usered/article/list";
    }

    /**
     * 编辑文章
     */
    @RequestMapping(value = "/editView")
    public String openEdit(Long articleId, Long id, ModelMap modelMap) {
        List<Template> list=templateService.findAll();
        modelMap.addAttribute("templates",list);
        if (articleId != null) {
            modelMap.addAttribute("articleId", articleId);
        }
        if (id == null) {
            return "/usered/article/ueditor";
        }
        Article article = articleService.find(id);
        ArticleModel articleModel = new ArticleModel();
        articleModel.bind(article);
        modelMap.addAttribute("article", articleModel);
        return "/usered/article/ueditor";
    }

    /**
     * 编辑文章
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public net.wit.Message save(Long id, Long templateId, Long goodsId, String content, String thumbnail, String title, String music) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Article article = articleService.find(id);
        Boolean isNew = false;
        if (article == null) {
            isNew = true;
            article = new Article();
            article.setDeleted(false);
            article.setHits(0L);
            article.setFavorite(0L);
            article.setLaud(0L);
            article.setReview(0L);
            article.setShare(0L);
            article.setAuthority(Article.Authority.isPublic);
            article.setIsExample(false);
            article.setIsPitch(false);
            article.setIsPublish(false);
            article.setIsReview(true);
            article.setIsTop(false);
            article.setIsReward(false);

        }
        article.setIsDraft(false);
        article.setIsAudit(false);
        article.setTitle(title);
        article.setAuthor(member.getUsername());
        article.setThumbnail(thumbnail);
        article.setMusic(music);
        article.setContent(content);
        article.setVotes("[]");
        article.setMember(member);
        article.setMediaType(Article.MediaType.image);
        if (templateId == null) {
            article.setTemplate(templateService.findDefault(Template.Type.article));
        } else {
            article.setTemplate(templateService.find(templateId));
        }

        if (isNew) {
            if (goodsId != null) {
                article.setGoods(goodsService.find(goodsId));
            }
            articleService.save(article);
        } else {
            if (article.getGoods() == null) {
                if (goodsId != null) {
                    article.setGoods(goodsService.find(goodsId));
                }
            }
            articleService.update(article);
        }
        return Message.success("保存成功");
    }

    /**
     * 预览
     */
    @RequestMapping(value = "/articleview", method = RequestMethod.GET)
    public String articleView(String content, ModelMap model) {
        model.addAttribute("articles",JSONArray.fromObject(content));
        return "/admin/article/view/articleView";
    }
}
