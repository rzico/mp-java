package net.wit.controller.weex;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.controller.admin.model.PageModel;
import net.wit.controller.weex.model.ArticleReviewModel;
import net.wit.controller.weex.model.ArticleViewModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
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
 
@Controller("weexReviewController")
@RequestMapping("/weex/review")
public class ReviewController extends BaseController {

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

    @Resource(name = "articleReviewServiceImpl")
    private ArticleReviewService articleReviewService;

    /**
     *  评论列表,带分页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleId, Pageable pageable, HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("article", Filter.Operator.eq,article));
        pageable.setFilters(filters);
        Page<ArticleReview> page = articleReviewService.findPage(null,null,pageable);
        PageModel model = PageModel.bind(page);
        model.setData(ArticleReviewModel.bindList(page.getContent()));
        return Message.success(model,"获取成功");
   }

}