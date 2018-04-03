package net.wit.controller;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCategoryModel;
import net.wit.entity.ArticleCategory;
import net.wit.plat.unspay.Merchant;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;


/**
 * @ClassName: ArticleCategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("testController")
@RequestMapping("/test")
public class TESTController extends BaseController {

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "refundsServiceImpl")
    private RefundsService refundsService;

    /**
     *  分类列表
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request) throws Exception {
        net.wit.entity.Merchant merchant = new net.wit.entity.Merchant();
        merchant.setScompany("芸店");
        merchant.setMerchantName("张森荣");
        merchant.setPhone("13860431130");
        merchant.setBankName("32429");
        merchant.setCardCity("2511");
        merchant.setCardProvince("25");
        merchant.setBranchBankName("厦门市海沧支行");
        merchant.setAddress("谊爱路海西文创大厦210");
        merchant.setCity("厦门市");
        merchant.setPhone("福建省");
        merchant.setLicenseNo("913502030899205666");
        merchant.setIndustryType("160");
        merchant.setEmail("zhangsr@rzico.com");
        merchant.setIdCard("352623197805181613");
        merchant.setCardNo("4367421930031121575");
        merchant.setBrokerage(new BigDecimal("0.38"));
        Merchant.addMerchant(merchant);
        return Message.error("error");
    }

    /**
     *  支付定时测试
     */
    @RequestMapping(value = "task", method = RequestMethod.GET)
    @ResponseBody
    public Message paymentQuery(HttpServletRequest request) throws Exception {
        paymentService.query();
        cartService.evictExpired();
        orderService.evictCompleted();
        orderService.releaseStock();
        refundsService.query();
        return Message.success("success");
    }

}