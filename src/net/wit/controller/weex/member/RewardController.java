package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleReviewModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
import net.wit.entity.ArticleReward;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberRewardController")
@RequestMapping("/weex/member/reward")
public class RewardController extends BaseController {

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

    @Resource(name = "articleRewardServiceImpl")
    private ArticleRewardService articleRewardService;

     /**
     *  提交打赏
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long articleId, BigDecimal amount,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }

        Member member = memberService.getCurrent();
        ArticleReward reward = new ArticleReward();
        reward.setArticle(article);
        reward.setAmount(amount);
        reward.setAuthor(article.getMember());
        reward.setIp(request.getRemoteAddr());
        reward.setStatus(ArticleReward.Status.waiting);
        reward.setFee(BigDecimal.ZERO);
        reward.setMember(member);
        articleRewardService.save(reward);
        return Message.success("发布成功");

    }

}