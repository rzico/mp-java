package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DepositModel;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;
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
 
@Controller("weexMemberDepositController")
@RequestMapping("/weex/member/nihtan/deposit")
public class NihtanDepositController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "depositServiceImpl")
    private DepositService depositService;


    /**
     * 收银台
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效会员id");
        }
        Date b = DateUtils.truncate(new Date(),Calendar.DATE);
        Date e =DateUtils.addDays(b,1);

        List<DepositSummary> by = depositService.sumPage(member,b,e);

        b = DateUtils.truncate(new Date(),Calendar.DATE);
        b =DateUtils.addDays(b,-1);
        e =DateUtils.addDays(b,1);

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
    public Message list(Long id,Date billDate,Pageable pageable, HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效会员id");
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
    public Message summary(Long id,Date billDate,String type,HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效会员id");
        }
        Date b = DateUtils.truncate(new Date(),Calendar.DATE);
        Date e =DateUtils.addDays(b,1);
        List<NihtanDepositSummary> deps = depositService.sumNihtanPage(member,b,e);
        return Message.bind(deps,request);
    }

}