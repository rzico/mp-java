package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.entity.Article;
import net.wit.entity.ArticleFavorite;
import net.wit.entity.ArticleLaud;
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
 * @ClassName: WeexMemberLaudController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberLaudController")
@RequestMapping("/weex/member/laud")
public class ArticleLaudController extends BaseController {

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

    @Resource(name = "articleLaudServiceImpl")
    private ArticleLaudService articleLaudService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     *   点赞
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
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
        ArticleLaud laud = new ArticleLaud();
        laud.setIp(request.getRemoteAddr());
        laud.setMember(member);
        laud.setArticle(article);
        laud.setIsShow(true);
        laud.setAuthor(article.getMember());
        articleLaudService.save(laud);
        messageService.laudPushTo(laud);
        return Message.success("点赞成功");
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
        List<ArticleLaud> data = articleLaudService.findList(null,null,filters,null);
        for (ArticleLaud laud:data) {
            articleLaudService.delete(laud);
        }
        return Message.success("取消点赞");
    }


}