package net.wit.controller.website;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.controller.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @ClassName: CouponController
 * @author 降魔战队  优惠券
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteCouponController")
@RequestMapping("/website/coupon")
public class CouponController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  优惠券列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (authorId!=null) {
            Member author = memberService.find(authorId);
            filters.add(new Filter("distributor", Filter.Operator.eq,author));
        }
        Date today = DateUtils.truncate(new Date(), Calendar.DATE);
        filters.add(new Filter("beginDate", Filter.Operator.le,today));
        filters.add(new Filter("endDate", Filter.Operator.ge,today));
        filters.add(new Filter("stock", Filter.Operator.ge,0));
        pageable.setFilters(filters);
        Page<Coupon> page = couponService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CouponModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  优惠券
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Coupon coupon = couponService.find(id);
        CouponModel model = new CouponModel();
        model.bind(coupon);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("coupon",model);
        Member member = memberService.getCurrent();
        if (member!=null) {
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter("member", Filter.Operator.eq,member));
            filters.add(new Filter("coupon", Filter.Operator.eq,coupon));
            filters.add(new Filter("isUsed", Filter.Operator.eq,false));
            List<CouponCode> couponCodes = couponCodeService.findList(1,filters,null);
            if (couponCodes.size()>0) {
                data.put("activate",true);
            } else {
                data.put("activate",false);
            }
        } else {
            data.put("activate",false);
        }
        return Message.bind(data,request);
    }

}