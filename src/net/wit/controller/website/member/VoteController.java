package net.wit.controller.website.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleVoteListModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleVote;
import net.wit.entity.Member;
import net.wit.entity.summary.ArticleVoteSummary;
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
 * @ClassName: VoteController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberVoteController")
@RequestMapping("/website/member/vote")
public class VoteController extends BaseController {

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

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleVoteServiceImpl")
    private ArticleVoteService articleVoteService;

     /**
     *  提交投票
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long articleId, String title,String value,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleVote vote = new ArticleVote();
        vote.setArticle(article);
        vote.setAuthor(article.getMember());
        vote.setIp(request.getRemoteAddr());
        vote.setMember(member);
        vote.setTitle(title);
        vote.setValue(value);
        articleVoteService.save(vote);
        return Message.success("投票成功");

    }

}