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
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


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


        if ("3".equals(bundle.getString("weex")) ) {
            authorId = Long.parseLong(bundle.getString("platform"));
        }

        Member author = memberService.find(authorId);

        Card card = member.card(author);

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("card", Filter.Operator.eq,card));
        List<BarrelStock> page = barrelStockService.findList(null,null,filters,null);

        return Message.bind(BarrelStockModel.bindList(page),request);
    }

}