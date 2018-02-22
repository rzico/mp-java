package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DepositModel;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.summary.DepositSummary;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: DepositController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberDepositController")
@RequestMapping("/applet/member/deposit")
public class DepositController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "depositServiceImpl")
    private DepositService depositService;

    /**
     * 汇总
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Date d = DateUtils.truncate(new Date(), Calendar.DATE);
        Date y = DateUtils.addDays(d,-1);
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Date b = DateUtils.truncate(new Date(),Calendar.MONTH);
        Date e = DateUtils.truncate(new Date(),Calendar.MONTH);
        e =DateUtils.addMonths(e,1);
        e =DateUtils.addDays(e,-1);

        List<DepositSummary> by = depositService.sumPage(member,b,e);

        b =DateUtils.addMonths(e,-1);
        e =DateUtils.addMonths(e,-1);

        List<DepositSummary> sy = depositService.sumPage(member,b,e);

        Map<String,Object> data = new HashMap<String,Object>();
        BigDecimal thm = BigDecimal.ZERO;
        for (DepositSummary s:by) {
            thm = thm.add(s.getAmount());
        }
        data.put("thisMonth",thm);

        BigDecimal lam = BigDecimal.ZERO;
        for (DepositSummary l:sy) {
            lam = lam.add(l.getAmount());
        }
        data.put("lastMonth",lam);
        return Message.bind(data,request);
    }

    /**
     *  我的账单
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Date billDate,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        if (billDate!=null) {
            Date d = DateUtils.addDays(DateUtils.truncate(billDate, Calendar.DATE),1);
            filters.add(new Filter("createDate", Filter.Operator.lt,d));
        }
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