package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleRewardModel;
import net.wit.controller.model.ShopModel;
import net.wit.entity.*;
import net.wit.service.*;
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
 * @ClassName: ShopController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberShopController")
@RequestMapping("/weex/member/shop")
public class ShopController extends BaseController {

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

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "categoryServiceImpl")
    private CategoryService categoryService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

     /**
     *  保存店铺
     */

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Shop shop,Long areaId,Long categoryId,Double lat,Double lng, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        Enterprise enterprise = admin.getEnterprise();
        Shop entity = null;
        Boolean isNew = false;
        if (shop.getId()!=null) {
            entity = shopService.find(shop.getId());
        } else {
            entity = new Shop();
            entity.setDeleted(false);
            entity.setOwner(enterprise.getMember());
            entity.setEnterprise(enterprise);
            isNew = true;
        }
        entity.setName(shop.getName());
        entity.setArea(areaService.find(areaId));
        entity.setAddress(shop.getAddress());
        entity.setLicense(shop.getLicense());
        entity.setScene(shop.getScene());
        entity.setThedoor(shop.getThedoor());
        entity.setLinkman(shop.getLinkman());
        entity.setTelephone(shop.getTelephone());
        if (categoryId!=null) {
            entity.setCategory(categoryService.find(categoryId));
        }
        Location lc = entity.getLocation();
        if (lc==null) {
            lc = new Location();
        }
        if (lng!=null && lat!=null) {
            lc.setLng(lng);
            lc.setLat(lat);
        }
        entity.setLocation(lc);
        if (isNew) {
            shopService.save(entity);
        } else {
            shopService.update(entity);
        }
        ShopModel model = new ShopModel();
        model.bind(entity);
        return Message.success(model,"发布成功");
    }

    /**
     *  我的店铺
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        Enterprise enterprise = admin.getEnterprise();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("enterprise", Filter.Operator.eq,enterprise));
        pageable.setFilters(filters);
        Page<Shop> page = shopService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ShopModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  门店信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long shopId,HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效shopid");
        }

        ShopModel model = new ShopModel();
        model.bind(shop);
        return Message.bind(model,request);
    }

    /**
     *  绑定收款码
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseBody
    public Message bind(Long shopId,String code){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效shopid");
        }

        Shop p = shopService.find(code);
        if (p!=null && !p.equals(shop)) {
            return Message.error("收款码已被使用");
        }

        shop.setCode(code);
        shopService.update(shop);

        return Message.success("绑定成功");
    }


    /**
     *  1分钱交易测试款
     *  shopid
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public Message test(Long shopId,String code,HttpServletRequest request){
        Shop shopCode =shopService.find(code);
        if (shopCode==null) {
            return Message.error("收款码没绑定");
        }
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效店铺");
        }
        if (!shop.equals(shopCode)) {
            return Message.error("收钱码验证不正确");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        PayBill payBill = new PayBill();
        payBill.setAmount(new BigDecimal("0.01"));
        payBill.setNoDiscount(BigDecimal.ZERO);
        payBill.setCouponCode(null);
        payBill.setCouponDiscount(BigDecimal.ZERO);
        payBill.setCard(null);
        payBill.setCardDiscount(BigDecimal.ZERO);
        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(effective.multiply(shop.getEnterprise().getBrokerage()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
        payBill.setMethod(PayBill.Method.online);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(shop.getOwner());
        payBill.setShop(shop);
        payBill.setEnterprise(shop.getEnterprise());
        try {
            Payment payment = payBillService.submit(payBill);
            return Message.success((Object)payment.getSn() ,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }


}