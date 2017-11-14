package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.controller.model.PayBillModel;
import net.wit.controller.model.PayBillViewModel;
import net.wit.controller.model.ShopModel;
import net.wit.entity.*;
import net.wit.entity.summary.PayBillShopSummary;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: PayBillController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberPayBillController")
@RequestMapping("weex/member/paybill")
public class PayBillController extends BaseController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    /**
     *  收银明细
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long shopId,Pageable pageable, HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效门店id");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("shop", Filter.Operator.eq,shop));
        filters.add(new Filter("status", Filter.Operator.eq,PayBill.Status.success));
        pageable.setFilters(filters);
        Page<PayBill> page = payBillService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(PayBillViewModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


    /**
     *  收银汇总
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Message summary(Long shopId,Date billDate,Pageable pageable, HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效门店id");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<PayBillShopSummary> dsum = payBillService.sumPage(shop,billDate,billDate);
        return Message.bind(dsum,request);
    }

}