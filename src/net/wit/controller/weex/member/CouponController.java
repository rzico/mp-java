package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.CouponCodeModel;
import net.wit.controller.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: CouponController
 * @author 降魔战队  优惠券
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberCouponController")
@RequestMapping("/weex/member/coupon")
public class CouponController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

     /**
     *  保存优惠券
     */
    @RequestMapping(value = "/submit")
    @ResponseBody
    public Message submit(Coupon coupon,Long atveType,BigDecimal atveMinPrice,Long atveAmount, Long goodsId, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }

        Enterprise enterprise = admin.getEnterprise();
        Member owner = enterprise.getMember();
        Coupon entity = null;
        Boolean isNew = false;
        if (coupon.getId()!=null) {
            entity = couponService.find(coupon.getId());
        } else {
            entity = new Coupon();
            entity.setDeleted(false);
            entity.setDistributor(owner);
            entity.setType(coupon.getType());
            entity.setColor(Coupon.Color.c8);
            isNew = true;
        }

        entity.setMinimumPrice(coupon.getMinimumPrice());
        entity.setScope(coupon.getScope());
        entity.setIntroduction(coupon.getIntroduction());
        entity.setAmount(coupon.getAmount());
        entity.setEndDate(coupon.getEndDate());
        entity.setBeginDate(coupon.getBeginDate());
        entity.setStock(coupon.getStock());
        if (coupon.getColor()!=null) {
            entity.setColor(coupon.getColor());
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        String s = "";
        if (coupon.getMinimumPrice().compareTo(BigDecimal.ZERO)==0) {
            if (coupon.getType().equals(Coupon.Type.fullcut)) {
                s = "无门槛抵" + nf.format(coupon.getAmount()) + "元";
            } else {
                s = "无门槛打" + nf.format(coupon.getAmount()) + "折";
            }
        } else {
            if (coupon.getType().equals(Coupon.Type.fullcut)) {
                s = "满" + nf.format(coupon.getMinimumPrice()) + "元减" + nf.format(coupon.getAmount()) + "元";
            } else {
                s = "满" + nf.format(coupon.getMinimumPrice()) + "元打" + nf.format(coupon.getAmount()) + "折";
            }
        }
        if (coupon.getType().equals(Coupon.Type.exchange)) {
            if (goodsId==null) {
                return Message.error("无效商品 id");
            }
            Goods goods = goodsService.find(goodsId);
            if (goods==null) {
                return Message.error("无效商品 id");
            }
            entity.setGoods(goods);
            s = goods.product().getName();
            entity.setAmount(BigDecimal.ZERO);
        }
        entity.setName(s);

        if (atveType==null) {
            atveType = 0L;
            atveMinPrice = BigDecimal.ZERO;
            atveAmount = 0L;
        }
        Map<String,Object> activity = new HashMap<String,Object>();
        activity.put("type",atveType);
        activity.put("min",atveMinPrice);
        activity.put("amount",atveAmount);
        entity.setActivity(JsonUtils.toJson(activity));

        if (isNew) {
            couponService.save(entity);
        } else {
            couponService.update(entity);
        }
        CouponModel model = new CouponModel();
        model.bind(entity);
        return Message.success(model,"保存成功");

    }


    /**
     *
     */
    @RequestMapping(value = "/activate")
    @ResponseBody
    public Message activate(Long id, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        Coupon coupon = couponService.find(id);
        if (coupon==null) {
            return Message.error("无效优惠券id");
        }
        CouponCode couponCode = null;
        try {
            couponCode = couponCodeService.build(coupon,member);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }

        CouponCodeModel model = new CouponCodeModel();
        model.bind(couponCode);
        return Message.success(model,"保存成功");

    }

    /**
     *  删除投票
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long id,HttpServletRequest request){
        Coupon coupon = couponService.find(id);
        if (coupon==null) {
            return Message.error("无效投票编号");
        }

        couponService.delete(id);
        return Message.success("删除成功");

    }


    /**
     *  优惠券列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Enterprise enterprise = admin.getEnterprise();
        Member owner = enterprise.getMember();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("distributor", Filter.Operator.eq,owner));
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
    public Message view(Long id,Pageable pageable, HttpServletRequest request){
        Coupon coupon = couponService.find(id);
        CouponModel model = new CouponModel();
        model.bind(coupon);
        return Message.bind(model,request);
    }

}