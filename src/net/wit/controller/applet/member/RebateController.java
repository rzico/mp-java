package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DepositModel;
import net.wit.controller.model.RebateModel;
import net.wit.entity.*;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.service.*;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @ClassName: RebateController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberRebateController")
@RequestMapping("/applet/member/rebate")
public class RebateController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    @Resource(name = "depositServiceImpl")
    private DepositService depositService;

    @Resource(name = "orderRankingServiceImpl")
    private OrderRankingService orderRankingService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     * 我的奖励金
     * id 会员
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Long id,HttpServletRequest request,HttpServletResponse response){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member==null) {
            String url = "http://"+bundle.getString("weixin.url")+"/website/topic/index.jhtml";
            String redirectUrl = "http://"+bundle.getString("weixin.url")+"/website/login/weixin.jhtml?redirectURL="+ StringUtils.base64Encode(url.getBytes());
            redirectUrl = URLEncoder.encode(redirectUrl);
            return "redirect:"+ MenuManager.codeUrlO2(redirectUrl);
        }

        return "redirect:/member/rabate?id="+id;
    }

    /**
     *  我的奖励金
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
            filters.add(new Filter("seller", Filter.Operator.eq,memberService.find(authorId)));
        }
        filters.add(new Filter("type", Filter.Operator.eq, Deposit.Type.rebate));
        pageable.setFilters(filters);
        Page<Deposit> page = depositService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(DepositModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  合计
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Message summary(Deposit.Type type ,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        BigDecimal sm = depositService.summary(type,member);
        if (sm==null) {
            sm = BigDecimal.ZERO;
        }
        return Message.bind(sm,request);
    }

    /**
     *  总览
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long authorId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member owner = null;
        if (authorId!=null) {
            owner = memberService.find(authorId);
        }

        BigDecimal sm = BigDecimal.ZERO;
        if (owner==null) {
            sm = depositService.summary(Deposit.Type.rebate,member);
        } else {
            sm = depositService.summary(Deposit.Type.rebate, member, owner);
        }
        if (sm==null) {
            sm = BigDecimal.ZERO;
        }

        RebateModel model = new RebateModel();
        model.setLogo(member.getLogo());
        model.setNickName(member.getNickName());
        model.setRebate(sm);
        if (owner!=null) {
            long cont = cardService.count(new Filter("owner", Filter.Operator.eq, owner), new Filter("promoter", Filter.Operator.eq, member));
            model.setContacts(cont);

            long inv = cardService.count(new Filter("owner", Filter.Operator.eq, owner), new Filter("promoter", Filter.Operator.eq, member), new Filter("type", Filter.Operator.eq, Card.Type.team));
            model.setInvalid(inv);


            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("owner", Filter.Operator.eq, owner));
            List<OrderRanking> ors = orderRankingService.findList(null, 1, filters, null);

            if (ors.size() == 0) {
                model.setRanking(0L);
            } else {
                model.setRanking(ors.get(0).getOrders());
            }

        } else {

            model.setRanking(0L);
            model.setContacts(0L);
            model.setInvalid(0L);

        }

        model.setAgentType("none");

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            Enterprise ent = admin.getEnterprise();
            if (!ent.getType().equals(Enterprise.Type.shop) && !ent.getType().equals(Enterprise.Type.personal) && ent.getStatus().equals(Enterprise.Status.success)) {
                model.setAgentType(ent.getType().name());
            }
        }

        return Message.bind(model,request);

    }

}