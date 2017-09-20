package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleModel;
import net.wit.controller.weex.model.ArticleOptionModel;
import net.wit.controller.weex.model.MemberModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

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
        return Message.success(model,"获取成功");
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
        return Message.success(model,"获取成功");
    }

    /**
     * 保存文章信息
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long id,String title, String author, ArticleTitle articleTitle,
                          String thumbnial, String music, String content,
                          Long areaId, Long templateId,HttpServletRequest request){
        Article article = null;
        if (id!=null) {
            article = articleService.find(id);
        }
        if (article==null) {
            article = new Article();
            article.setDeleted(false);
            article.setHits(0L);
            article.setFavorite(0L);
            article.setLaud(0L);
            article.setReview(0L);
            article.setIsDraft(true);
            article.getArticleOptions().setAuthority(ArticleOptions.Authority.isPublic);
            article.getArticleOptions().setIsExample(false);
            article.getArticleOptions().setIsPitch(false);
            article.getArticleOptions().setIsPublish(false);
            article.getArticleOptions().setIsReview(true);
            article.getArticleOptions().setTop(false);
            article.getArticleOptions().setIsReward(false);
            article.setTemplate(templateService.findDefault(Template.Type.article));
        }
        article.setTitle(title);
        article.setAuthor(author);
        article.setThumbnial(thumbnial);
        article.setMusic(music);
        article.setContent(content);
        if (areaId!=null) {
            article.setArea(areaService.find(areaId));
        }
        if (templateId!=null) {
            article.setTemplate(templateService.find(templateId));
        }
        if (articleTitle!=null) {
            article.setArticleTitle(articleTitle);
        }
        articleService.save(article);
        ArticleModel model =new ArticleModel();
        model.bind(article);
        return Message.success(model,"获取成功");
    }

    /**
     * 发布文章信息
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public Message publish(Long id,ArticleOptions articleOptions,Location location,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        if (articleOptions!=null) {
            article.setArticleOptions(articleOptions);
        }
        if (location!=null && location.getLat()!=0 && location.getLng()!=0) {
            article.setLocation(location);
        }
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

}