package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.OrderRankingModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
import net.wit.entity.Member;
import net.wit.entity.OrderRanking;
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
 * @ClassName: OrderRankingController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberRankingController")
@RequestMapping("/applet/member/ranking")
public class OrderRankingController extends BaseController {

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

    @Resource(name = "orderRankingServiceImpl")
    private OrderRankingService orderRankingService;

    /**
     *  订单排行
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        if (authorId!=null) {
            filters.add(new Filter("owner", Filter.Operator.eq,memberService.find(authorId)));
        }
        pageable.setFilters(filters);
        Page<OrderRanking> page = orderRankingService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(OrderRankingModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }
}