package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.ArticleVoteListModel;
import net.wit.entity.*;
import net.wit.entity.summary.ArticleVoteSummary;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberVoteController")
@RequestMapping("/weex/member/vote")
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

    /**
     *  我的投票
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleId,String title,String value,Pageable pageable, HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("article", Filter.Operator.eq,article));
        if (title!=null) {
            filters.add(new Filter("title", Filter.Operator.eq,title));
        }
        if (value!=null) {
            filters.add(new Filter("value", Filter.Operator.eq,value));
        }
        pageable.setFilters(filters);
        Page<ArticleVote> page = articleVoteService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleVoteListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


    /**
     *  投票统计
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Message summary(Long articleId,Pageable pageable, HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("article", Filter.Operator.eq,article));
        pageable.setFilters(filters);
        List<ArticleVoteSummary> data = articleVoteService.sumPage(article);
        return Message.bind(data,request);
    }

}