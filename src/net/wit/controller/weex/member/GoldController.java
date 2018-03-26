package net.wit.controller.weex.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DepositModel;
import net.wit.controller.model.GameListModel;
import net.wit.controller.model.GoldModel;
import net.wit.entity.*;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;
import net.wit.plat.nihtan.Kaga;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: KagaController
 * @author 降魔战队  职业分类
 * @date 2017-9-14 19:42:9
 */
 
@Controller("goldMemberController")
@RequestMapping("/weex/member/gold")
public class GoldController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "gameListServiceImpl")
    private GameListService gameListService;

    @Resource(name = "goldServiceImpl")
    private GoldService goldService;

    @Resource(name = "goldBuyServiceImpl")
    private GoldBuyService goldBuyService;

    @Resource(name = "goldExchangeServiceImpl")
    private GoldExchangeService goldExchangeService;

    @Resource(name = "goldProductServiceImpl")
    private GoldProductService goldProductService;

    /**
     * 我的账单
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Date billDate, Pageable pageable, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        if (billDate != null) {
            Date d = DateUtils.addDays(DateUtils.truncate(billDate, Calendar.DATE), 1);
            filters.add(new Filter("createDate", Filter.Operator.lt, d));
        }
        filters.add(new Filter("member", Filter.Operator.eq, member));
        pageable.setFilters(filters);
        Page<Gold> page = goldService.findPage(null, null, pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GoldModel.bindList(page.getContent()));
        return Message.bind(model, request);
    }

    /**
     * 账单汇总
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Message summary(Date billDate, String type, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (type == null) {
            type = "1";
        }
        Date d = DateUtils.truncate(billDate, Calendar.DATE);
        Date e = DateUtils.truncate(billDate, Calendar.DATE);
        if (type != null) {
            if ("1".equals(type)) {
                d = DateUtils.truncate(billDate, Calendar.MONTH);
                e = DateUtils.truncate(billDate, Calendar.MONTH);
                e = DateUtils.addMonths(e, 1);
                e = DateUtils.addDays(e, -1);
            }
            if ("2".equals(type)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                d = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(d);
                calendar1.set(Calendar.MONTH, 0);
                calendar1.set(Calendar.DATE, 1);
                calendar1.roll(Calendar.DAY_OF_YEAR, -1);
                e = calendar1.getTime();
            }
        }
        List<NihtanDepositSummary> deps = goldService.sumPage(member, d, e);
        return Message.bind(deps, request);
    }


    /**
     * 购买金币
     */
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    @ResponseBody
    public Message buy(Long id, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        GoldProduct product = goldProductService.find(id);
        if (product == null) {
            return Message.error("无效金币");
        }
        GoldBuy goldBuy = new GoldBuy();
        goldBuy.setAmount(product.getPrice());
        goldBuy.setGold(product.getGold());
        goldBuy.setDeleted(false);
        goldBuy.setMember(member);
        goldBuy.setStatus(GoldBuy.Status.none);
        goldBuy.setMemo("金币充值");
        try {
            goldBuyService.submit(goldBuy);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
        return Message.success("充值成功");
    }


    /**
     * 兑换金币
     */
    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    @ResponseBody
    public Message exchange(Long amount, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        GoldExchange goldExchange = new GoldExchange();
        goldExchange.setAmount(new BigDecimal(amount).multiply());
        goldExchange.setGold(amount);
        goldExchange.setDeleted(false);
        goldExchange.setMember(member);
        goldExchange.setStatus(GoldExchange.Status.none);
        goldExchange.setMemo("金币兑换");
        try {
            goldExchangeService.submit(goldExchange);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
        return Message.success("充值成功");
    }

}