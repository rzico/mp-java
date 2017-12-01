package net.wit.controller.website.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReward;
import net.wit.entity.Member;
import net.wit.entity.Payment;
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
 * @ClassName: RewardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberRewardController")
@RequestMapping("/website/member/reward")
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
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleReward reward = new ArticleReward();
        reward.setArticle(article);
        reward.setAmount(amount);
        reward.setAuthor(article.getMember());
        reward.setIp(request.getRemoteAddr());
        reward.setStatus(ArticleReward.Status.waiting);
        reward.setMember(member);
        reward.setFee(amount.multiply(new BigDecimal("0.1")));
        Payment payment = articleRewardService.saveAndPayment(reward);
        if (payment==null) {
            return Message.error("打赏失败");
        }
        return Message.success((Object) payment.getSn(),"发布成功");

    }
}