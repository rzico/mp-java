package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.entity.Article;
import net.wit.entity.ArticleFavorite;
import net.wit.entity.ArticleShare;
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
 * @ClassName: ArticleShareController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberShareController")
@RequestMapping("/weex/member/share")
public class ArticleShareController extends BaseController {

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

    @Resource(name = "articleShareServiceImpl")
    private ArticleShareService articleShareService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     *   分享
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Message add(Long  articleId, ArticleShare.ShareType shareType, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章 id");
        }
        ArticleShare share = new ArticleShare();
        share.setIp(request.getRemoteAddr());
        share.setMember(member);
        share.setArticle(article);
        share.setIsShow(true);
        share.setShareType(shareType);
        share.setAuthor(article.getMember());
        articleShareService.save(share);
        messageService.sharePushTo(share);
        return Message.success("分享成功");
   }


    /**
     *   分享公众号
     */
    @RequestMapping(value = "/platform")
    @ResponseBody
    public Message platform(Long  articleId, ArticleShare.ShareType shareType, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章 id");
        }
        ArticleShare share = new ArticleShare();
        share.setIp(request.getRemoteAddr());
        share.setMember(member);
        share.setArticle(article);
        share.setIsShow(true);
        share.setShareType(shareType);
        share.setAuthor(article.getMember());
        articleShareService.save(share);
        messageService.sharePushTo(share);
        return Message.success("分享成功");
    }

}