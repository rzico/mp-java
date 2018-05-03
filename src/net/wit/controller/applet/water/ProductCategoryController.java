package net.wit.controller.applet.water;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.PageBlock;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ProductCategoryModel;
import net.wit.controller.model.ProductModel;
import net.wit.entity.Coupon;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ProductCategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletWaterProductCategoryController")
@RequestMapping("/applet/water/product_category")
public class ProductCategoryController extends BaseController {

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

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("type", Filter.Operator.eq,Coupon.Type.exchange));
        filters.add(new Filter("deleted", Filter.Operator.eq,false));
        List<Coupon> page = couponService.findList(null,null,filters,null);
        List<ProductCategory> productCategories = new ArrayList<>();
        for (Coupon coupon:page) {
            Product product = coupon.getGoods().product();
            if (product.getProductCategory()!=null) {
                if (!productCategories.contains(product.getProductCategory())) {
                    productCategories.add(product.getProductCategory());
                }
            }
        }
        return Message.bind(ProductCategoryModel.bindList(productCategories),request);
    }

}