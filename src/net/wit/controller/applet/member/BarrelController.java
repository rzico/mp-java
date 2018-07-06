package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.BarrelStockModel;
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
import java.util.*;


/**
 * @ClassName: BarrelController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberBarrelController")
@RequestMapping("/applet/member/barrel")
public class BarrelController extends BaseController {

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

    @Resource(name = "barrelStockServiceImpl")
    private BarrelStockService barrelStockService;

     /**
     *  我的空桶
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        Card card = null;
        if ("3".equals(bundle.getString("weex")) ) {
            card = member.getCards().get(0);
        } else {
            Member author = memberService.find(authorId);
            card = member.card(author);
        }
        if (card==null) {
            return Message.error("无效会员卡");
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("card", Filter.Operator.eq,card));
        List<BarrelStock> page = barrelStockService.findList(null,null,filters,null);

        return Message.bind(BarrelStockModel.bindList(page),request);
    }

    /**
     *  我的空桶
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long authorId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Card card = null;
        if ("3".equals(bundle.getString("weex")) ) {
            card = member.getCards().get(0);
        } else {
            Member author = memberService.find(authorId);
            card = member.card(author);
        }
        if (card==null) {
            return Message.error("无效会员卡");
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("card", Filter.Operator.eq,card));
        List<BarrelStock> page = barrelStockService.findList(null,null,filters,null);
        BigDecimal total = BigDecimal.ZERO;
        for (BarrelStock stock:page) {
            total = total.add(stock.getPledge());
        }
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("pledge",total);
        return Message.bind(data,request);
    }
}