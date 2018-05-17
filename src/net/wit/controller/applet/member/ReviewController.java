package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
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
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberReviewController")
@RequestMapping("/applet/member/review")
public class ReviewController extends BaseController {

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

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "articleReviewServiceImpl")
    private ArticleReviewService articleReviewService;

     /**
     *  提交评论
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long articleId,String content,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }

        Member member = memberService.getCurrent();
        ArticleReview review = new ArticleReview();
        review.setArticle(article);
        review.setContent(content);
        review.setDeleted(false);
        review.setIp(request.getRemoteAddr());
        review.setMember(member);
        System.out.println("membermembermembermembermembermember====" + member.getId());
        review.setAuthor(article.getMember());
        articleReviewService.save(review);
        messageService.reviewPushTo(review);
        ArticleReviewModel model = new ArticleReviewModel();
        model.bind(review);
        return Message.success(model,"发布成功");

    }



    /**
     *  删除评论
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Message delete(Long id,HttpServletRequest request){
        ArticleReview articleReview = articleReviewService.find(id);
        if (articleReview==null) {
            return Message.error("无效评论编号");
        }

        articleReviewService.delete(id);
        return Message.success("删除成功");

    }


    /**
     *  我的评论
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("author", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<ArticleReview> page = articleReviewService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleReviewModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}