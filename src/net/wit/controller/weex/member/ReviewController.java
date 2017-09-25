package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleReviewModel;
import net.wit.controller.weex.model.TemplateModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
import net.wit.entity.Member;
import net.wit.entity.Template;
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
 
@Controller("weexMemberReviewController")
@RequestMapping("/weex/member/review")
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
        articleReviewService.save(review);

        article.setReview(article.getReview()+1);
        articleService.save(article);

        ArticleReviewModel model = new ArticleReviewModel();
        model.bind(review);
        return Message.success(model,"发布成功");

    }

}