package net.wit.controller.weex.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


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

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "weixinUpServiceImpl")
    private WeixinUpService weixinUpService;

    @Resource(name = "redPackageServiceImpl")
    private RedPackageService redPackageService;
    /**
     *  文章列表,带分页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleCatalogId,Long timeStamp,Boolean isVote,Boolean isForm, Boolean isDraft,Boolean isPublish, Pageable pageable, HttpServletRequest request){
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
            filters.add(new Filter("modifyDate", Filter.Operator.ge,new Date(timeStamp)));
        }
        if (isVote!=null && isVote) {
            filters.add(new Filter().isNotNull("votes"));
        }
        if (isForm!=null && isForm) {
            filters.add(new Filter("form", Filter.Operator.ne, "[]"));
            filters.add(new Filter("form", Filter.Operator.ne, ""));
            filters.add(new Filter("form", Filter.Operator.ne, null));
            filters.add(new Filter().isNotNull("form"));
        }
        if (isDraft!=null) {
            filters.add(new Filter("isDraft", Filter.Operator.eq,isDraft));
        }
        if(isPublish != null){
            filters.add(new Filter("isPublish" ,Filter.Operator.eq, isPublish));
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
    public Message submit(String body,Long goodsId, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleModel model = JsonUtils.toObject(body,ArticleModel.class);
        if (model==null) {
            return Message.error("无效数据包");
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
        if (goodsId==null) {
            for (ArticleContentModel acm : model.getTemplates()) {
                if (acm.getMediaType().equals(Article.MediaType.product)) {
                    goodsId = acm.getId();
                }
            }
        }

        Boolean isDraft = model.getIsDraft();

        String forms = null;
        if(model.getForms()!=null){
            forms = JsonUtils.toJson(model.getForms());
        }

        String votes = null;
        if (model.getVotes()!=null) {
            votes = JsonUtils.toJson(model.getVotes());
        }
        Article article = null;
        Boolean isNew = false;
        if (id!=null) {
            article = articleService.find(model.getId());
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
            article.setIsAudit(false);
            article.setIsTop(false);
            article.setIsReward(false);
            article.setTemplate(templateService.findDefault(Template.Type.article));
            article.setIsDraft(false);
        }
        article.setIsDraft(isDraft);
        article.setIsAudit(article.getIsAudit());//直接提交审核
        article.setTitle(title);
        article.setAuthor(author);
        article.setThumbnail(thumbnail);
        article.setMusic(music);
        article.setContent(content);
        article.setVotes(votes);
        article.setForm(forms);
        article.setMember(member);
        article.setMediaType(Article.MediaType.image);

        if (isNew) {
            if (goodsId!=null) {
                article.setGoods(goodsService.find(goodsId));
            }
            articleService.save(article);
        } else {
            if (article.getGoods()==null) {
                if (goodsId!=null) {
                    article.setGoods(goodsService.find(goodsId));
                }
            }
            articleService.update(article);
        }

        ArticleModel entityModel =new ArticleModel();
        entityModel.bind(article);

        System.out.println(JsonUtils.toJson(entityModel));
        return Message.success(entityModel,"保存成功");

    }

    /**
     * 发布文章信息
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public Message publish(Long id, Long articleCategoryId, Long articleCatalogId, ArticleOptionModel articleOptions, Location location, HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        if (articleOptions!=null) {
            article.setIsReward(articleOptions.getIsReward());
            article.setIsTop(articleOptions.getIsTop());
            article.setIsReview(articleOptions.getIsReview());
            article.setIsPublish(true);
            article.setAuthority(articleOptions.getAuthority());
            article.setIsPitch(articleOptions.getIsPitch());
            //更新设置红包
//            article.setIsRedPackage(articleOptions.getIsRedPackage());

            if (articleOptions.getPassword()!=null) {
                article.setPassword(MD5Utils.getMD5Str(articleOptions.getPassword()));
            }

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
        article.setIsDraft(true);
        article.setIsPublish(true);

        articleService.update(article);

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("follow", Filter.Operator.eq,article.getMember()));
        List<MemberFollow> data = memberFollowService.findList(null,null,filters,null);
        for (MemberFollow follow:data) {
           messageService.publishPushTo(article,follow.getMember());
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




    @RequestMapping(value = "/getRedPackage", method = RequestMethod.POST)
    @ResponseBody
    public Message getRedPackage(Long articleId, HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Article article = articleService.find(articleId);
        if(article == null){
            return Message.error("没有找到该文章");
        }
        RedPackage redPackage = new RedPackage();
        redPackage.setMember(member);
        redPackage.setStatus(RedPackage.Status.get);
        redPackage.setArticle(article);
        redPackage.setIp(request.getRemoteAddr());
        double getMoney = redPackageService.getRedPackage(redPackage);
        if(getMoney > 0.0){
            return Message.success(getMoney,"领取成功");
        }else if(getMoney == 0.0){
            return Message.error("已领取");
        }else{
            return Message.error("领取失败");
        }

    }
    /**
     * 向文章包红包
     */
    @RequestMapping(value = "/setRedPackage", method = RequestMethod.POST)
    @ResponseBody
    public Message setRedPackage(Long articleId, ArticleRedPackageModel articleRedPackage,Boolean isRedPackage, HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Article article = articleService.find(articleId);
        if(article == null){
            return Message.error("没有找到该文章");
        }


        ArticleRedPackage aredPackage = new ArticleRedPackage();
        if(articleRedPackage != null){
            aredPackage.setRedPackageType(articleRedPackage.getRedPackageType());
            aredPackage.setRemainSize(articleRedPackage.getRemainSize());
            aredPackage.setAmount(articleRedPackage.getRemainMoney());
            aredPackage.setIsPay(false);
        }
        article.setIsRedPackage(isRedPackage);
        article.setArticleRedPackage(aredPackage);
        articleService.update(article);

        RedPackage redPackage = new RedPackage();
        redPackage.setMember(member);
        redPackage.setArticle(article);
        redPackage.setAmount(aredPackage.getAmount());
        redPackage.setIp(request.getRemoteAddr());
        redPackage.setStatus(RedPackage.Status.wait);
        try {
            Payment payment = redPackageService.sendRedPackage(redPackage);
            return Message.success((Object) payment.getSn(), "发送成功");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Message.error(e.getMessage());
        }
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