package net.wit.controller.website.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CardBillModel;
import net.wit.controller.model.DepositModel;
import net.wit.entity.CardBill;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.service.CardBillService;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: DepositController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberDepositController")
@RequestMapping("/website/member/cardbill")
public class CardBillController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cardBillServiceImpl")
    private CardBillService cardBillService;

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
        Page<CardBill> page = cardBillService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CardBillModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}