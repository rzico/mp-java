package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleListModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleFavorite;
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
 * @ClassName: WeexMemberFavoriteController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberFavoriteController")
@RequestMapping("/weex/member/favorite")
public class ArticleFavoriteController extends BaseController {

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

    @Resource(name = "articleFavoriteServiceImpl")
    private ArticleFavoriteService articleFavoriteService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     *   收藏
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Message add(Long  articleId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章 id");
        }
        ArticleFavorite favorite = new ArticleFavorite();
        favorite.setIp(request.getRemoteAddr());
        favorite.setMember(member);
        favorite.setArticle(article);
        favorite.setIsShow(true);
        articleFavoriteService.save(favorite);
        messageService.favoritePushTo(favorite);
        return Message.success("收藏成功");
   }

    /**
     *   取消
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long  articleId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章 id");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        filters.add(new Filter("article", Filter.Operator.eq,article));
        List<ArticleFavorite> data = articleFavoriteService.findList(null,null,filters,null);
        for (ArticleFavorite favorite:data) {
            articleFavoriteService.delete(favorite);
        }
        return Message.success("取消成功");
    }


}