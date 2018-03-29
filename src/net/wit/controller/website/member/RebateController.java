package net.wit.controller.website.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DepositModel;
import net.wit.controller.model.RebateModel;
import net.wit.entity.Card;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.service.CardService;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
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
 * @ClassName: RebateController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberRebateController")
@RequestMapping("/website/member/rebate")
public class RebateController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;


    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    @Resource(name = "depositServiceImpl")
    private DepositService depositService;

    /**
     *  我的奖励金
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
        Member owner = memberService.find(authorId);

        BigDecimal sm = depositService.summary(Deposit.Type.rebate,member,owner);

        RebateModel model = new RebateModel();
        model.setRebate(sm);
        long cont = cardService.count(new Filter("owner", Filter.Operator.eq,owner) ,new Filter("promoter", Filter.Operator.eq,member) );
        model.setContacts(cont);

        long inv = cardService.count(new Filter("owner", Filter.Operator.eq,owner) ,new Filter("promoter", Filter.Operator.eq,member),new Filter("type", Filter.Operator.eq, Card.Type.team) );
        model.setInvalid(inv);

        return Message.bind(model,request);
    }

}