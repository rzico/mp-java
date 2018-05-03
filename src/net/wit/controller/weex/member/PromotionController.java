package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DistributionModel;
import net.wit.controller.model.PromotionListModel;
import net.wit.controller.model.PromotionModel;
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
 * @ClassName: PromotionController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberPromotionController")
@RequestMapping("/weex/member/promotion")
public class PromotionController extends BaseController {

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

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    /**
     *  详情
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Promotion promotion = promotionService.find(id);
        if (promotion==null) {
            return Message.error("无效活动 id");
        }

        PromotionModel model = new PromotionModel();
        model.bind(promotion);
        return Message.bind(model,request);
    }

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long goodsId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Goods goods = goodsService.find(goodsId);
        if (goods==null) {
            return Message.error("无效商品 id");
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("goods", Filter.Operator.eq,goods));
        List<Promotion> promotions = promotionService.findList(null,null,filters,null);

        return Message.bind(PromotionListModel.bindList(promotions),request);
    }

    /**
     *  添加活动
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Promotion.Type type, Integer quantity,Integer giftQuantity,Long giftId,Long goodsId, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
        }

        Goods goods = goodsService.find(goodsId);
        if (goods==null) {
            return Message.error("");
        }
        Product product = productService.find(giftId);

        Promotion promotion = new Promotion();
        promotion.setDeleted(false);
        promotion.setGift(product);
        promotion.setGoods(goods);
        promotion.setGiftQuantity(giftQuantity);
        promotion.setQuantity(quantity);
        promotion.setMinimumPrice(BigDecimal.ZERO);

        promotionService.save(promotion);

        PromotionListModel model = new PromotionListModel();
        model.bind(promotion);
        return Message.success(model,"添加成功");
    }

    /**
     *  修改文集
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,Promotion.Type type, Integer quantity,Integer giftQuantity,Long giftId,Long goodsId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
        }

        Goods goods = goodsService.find(goodsId);
        if (goods==null) {
            return Message.error("");
        }
        Product product = productService.find(giftId);

        Promotion promotion = promotionService.find(id);
        promotion.setDeleted(false);
        promotion.setGift(product);
        promotion.setGoods(goods);
        promotion.setGiftQuantity(giftQuantity);
        promotion.setQuantity(quantity);
        promotion.setMinimumPrice(BigDecimal.ZERO);

        promotionService.save(promotion);

        PromotionListModel model = new PromotionListModel();
        model.bind(promotion);
        return Message.success(model,"添加成功");

    }


    /**
     *  删除文集
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Promotion promotion = promotionService.find(id);
        if (promotion==null) {
            return Message.error("无效活动id");
        }

        promotionService.delete(promotion);
        return Message.success("删除成功");
    }
}