package net.wit.controller.applet;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReward;
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
 * @ClassName: RewardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletRewardController")
@RequestMapping("/applet/reward")
public class RewardController extends BaseController {

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

    @Resource(name = "articleRewardServiceImpl")
    private ArticleRewardService articleRewardService;

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
        filters.add(new Filter("type", Filter.Operator.eq,ArticleReward.Type.article));
        pageable.setFilters(filters);
        Page<ArticleReward> page = articleRewardService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleRewardModel.bindList(page.getContent()));
        return Message.bind(model,request);
   }

}