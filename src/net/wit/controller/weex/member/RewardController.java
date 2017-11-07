package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.entity.*;
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
        reward.setMember(member);
        reward.setFee(amount.multiply(new BigDecimal("0.05")));
        Payment payment = articleRewardService.saveAndPayment(reward);
        if (payment==null) {
            return Message.error("打赏失败");
        }
        return Message.success((Object) payment.getSn(),"发布成功");

    }

    /**
     *  我的赏金
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<ArticleReward> page = articleRewardService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleRewardModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}