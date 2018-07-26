package net.wit.controller.applet.member;

import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleModel;
import net.wit.controller.model.ArticleOptionModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberArticleController")
@RequestMapping("/applet/member/article")
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

    @Resource(name = "articleLaudServiceImpl")
    private ArticleLaudService articleLaudService;

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "weixinUpServiceImpl")
    private WeixinUpService weixinUpService;

    /**
     *  文章列表,带分页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleCatalogId,Long timeStamp,Boolean isVote,Boolean isDraft,Pageable pageable, HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            Role role = roleService.find(1L);
            if (admin.getRoles().contains(role)) {
                member = admin.getEnterprise().getMember();
            }
        }

        ArticleCatalog  articleCatalog = articleCatalogService.find(articleCatalogId);
        List<Filter> filters = new ArrayList<Filter>();
        if (articleCatalog!=null) {
            filters.add(new Filter("articleCatalog", Filter.Operator.eq,articleCatalog));
        }
        if (timeStamp!=null) {
            filters.add(new Filter("modifyDate", Filter.Operator.ge,new Date(timeStamp)));
        }
        if (isVote!=null && isVote) {
            filters.add(new Filter().isNotNull("votes"));
        }
        if (isDraft!=null) {
            filters.add(new Filter("isDraft", Filter.Operator.eq,isDraft));
        }
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        pageable.setOrderDirection(Order.Direction.desc);
        pageable.setOrderProperty("modifyDate");
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
    public Message submit(String body,Long goodsId,Article.ArticleType articleType, HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            Role role = roleService.find(1L);
            if (admin.getRoles().contains(role)) {
                member = admin.getEnterprise().getMember();
            }
        }

        String bd = null;
        try {
            bd = new String(Base64.decode(body),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ArticleModel model = JsonUtils.toObject(bd,ArticleModel.class);
        if (model==null) {
            return Message.error("无效数据包");
        }

        if (articleType==null) {
            articleType = Article.ArticleType.article;
        }
        Long id = model.getId();

        String title = model.getTitle();
        String author = member.getNickName();
        String thumbnail = model.getThumbnail();
        String music = null;
        if (model.getMusic()!=null) {
            music = JsonUtils.toJson(model.getMusic());
        }
        String content = null;
        if (model.getTemplates()!=null) {
            content = JsonUtils.toJson(model.getTemplates());
        }

        Boolean isDraft = model.getIsDraft();

        String votes = null;
        if (model.getVotes()!=null) {
            votes = JsonUtils.toJson(model.getVotes());
        }
        Article article = null;
        Boolean isNew = false;
        if (id!=null) {
            article = articleService.find(id);
        }
        if (article==null) {
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
            article.setDragonStatus(Article.DragonStatus.none);
            article.setTemplate(templateService.findDefault(Template.Type.article));
            article.setMember(member);
            article.setMediaType(articleType);

            Goods goods = null;
            if (articleType.equals(Article.ArticleType.product)) {
                goods = goodsService.find(goodsId);
                article.setGoods(goods);
            }
        }

        article.setIsDraft(isDraft);
        article.setIsAudit(false);
        article.setTitle(title);
        article.setAuthor(author);
        article.setThumbnail(thumbnail);
        article.setMusic(music);
        article.setContent(content);
        article.setVotes(votes);

        article.setIsDraft(false);
        article.setIsPublish(true);

        if (isNew) {
            articleService.save(article);
        } else {
            articleService.update(article);
        }

        ArticleModel entityModel =new ArticleModel();
        entityModel.bind(article);
        return Message.success(entityModel,"保存成功");

    }

    /**
     * 修改显示模版
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,Long templateId,Long articleCatalogId,Boolean isTop,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        Boolean edited = false;
        if (templateId!=null) {
            article.setTemplate(templateService.find(templateId));
            edited = true;
        }
        if (articleCatalogId!=null) {
            article.setArticleCatalog(articleCatalogService.find(articleCatalogId));
            edited = true;
        }
        if (isTop!=null) {
            Tag tag = tagService.find(6L);
            if (isTop) {
                if (!article.getTags().contains(tag)) {
                    article.getTags().add(tag);
                }
            } else {
                if (article.getTags().contains(tag)) {
                    article.getTags().remove(tag);
                }
            }
            edited = true;
        }
        if (!edited) {
            return Message.error("传参不正确");
        }
        articleService.update(article);
        ArticleModel entityModel =new ArticleModel();
        entityModel.bind(article);
        return Message.success(entityModel,"保存成功");
    }

    /**
     * 标签
     */
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public @ResponseBody
    Message view(Long id,Long [] tagIds,HttpServletRequest request) {
        Article article = articleService.find(id);
        article.setTags(tagService.findList(tagIds));
        articleService.update(article);
        return Message.success("success");
    }

    /**
     * 获取显示模版
     */
    @RequestMapping(value = "/template")
    @ResponseBody
    public Message template(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        String sn="1001";
        if (article.getTemplate()!=null) {
           sn = article.getTemplate().getSn();
        }
        return Message.success((Object)sn,"发布成功");
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
        articleService.update(article);
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
        articleService.update(article);
        return Message.success("点击成功");

    }


    /**
     *  文章删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long articleId,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        articleService.delete(article);
        return Message.success("删除成功");

    }

    /**
     *  文章还原
     */
    @RequestMapping(value = "/revert", method = RequestMethod.POST)
    @ResponseBody
    public Message revert(Long articleId,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        article.setDeleted(false);
        articleService.update(article);
        return Message.success("还原成功");
    }

    /**
     *  文章抓取
     */
    @RequestMapping(value = "grabarticle", method = RequestMethod.GET)
    @ResponseBody
    public Message articleGrab(String articlePath, HttpServletRequest request){
        try {
            JSONObject jsonObject=weixinUpService.DownArticle(articlePath);
            if(jsonObject!=null){
                return Message.success(jsonObject,"抓取成功");
            }
            else {
                return Message.error("URL参数错误,抓取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("URL参数错误,抓取失败");
        }
    }

}