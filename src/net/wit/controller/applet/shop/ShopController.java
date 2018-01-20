package net.wit.controller.applet.shop;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
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
 
@Controller("appletShopShopController")
@RequestMapping("/applet/shop/shop")
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
     *  我的店铺
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Double lat,Double lng,Pageable pageable, HttpServletRequest request){
        Member member = memberService.find(authorId);
        if (member==null) {
            return Message.error("作者id无效");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("owner", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<Shop> page = shopService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ShopModel.bindList(page.getContent(),lat,lng));
        return Message.bind(model,request);
    }

    /**
     *  门店信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long shopId,Double lat,Double lng,HttpServletRequest request){
        Shop shop = shopService.find(shopId);
        if (shop==null) {
            return Message.error("无效shopid");
        }

        ShopModel model = new ShopModel();
        model.bind(shop);
        return Message.bind(model,request);
    }

}