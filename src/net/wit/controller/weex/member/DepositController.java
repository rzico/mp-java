package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.controller.model.DepositModel;
import net.wit.controller.model.PayBillSummaryModel;
import net.wit.entity.*;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.PayBillShopSummary;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: DepositController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberDepositController")
@RequestMapping("/weex/member/deposit")
public class DepositController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "depositServiceImpl")
    private DepositService depositService;

    /**
     *  我的账单
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<Deposit> page = depositService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(DepositModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  账单汇总
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Message summary(Date billDate,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Date b = DateUtils.truncate(billDate,Calendar.MONTH);
        Date e = DateUtils.truncate(billDate,Calendar.MONTH);
        e =DateUtils.addMonths(e,1);
        e =DateUtils.addDays(e,-1);
        List<DepositSummary> deps = depositService.sumPage(member,b,e);
        return Message.bind(deps,request);
    }

}