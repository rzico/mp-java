package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.controller.model.ProductCategoryModel;
import net.wit.entity.ArticleCatalog;
import net.wit.entity.Member;
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
 
@Controller("weexMemberProductCategoryController")
@RequestMapping("/weex/member/product_catalog")
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

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<ProductCategory> categories = productCategoryService.findList(null,null,filters,null);

        return Message.bind(ProductCategoryModel.bindList(categories),request);
    }

    /**
     *  排序
     */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ResponseBody
    public Message sort(Long[] ids,HttpServletRequest request){
        int i=0;
        for (Long id:ids) {
            ProductCategory catalog = productCategoryService.find(id);
            i=i+1;
            catalog.setOrders(i);
            productCategoryService.update(catalog);
        }
        return Message.success("success");
    }

    /**
     *  添加文集
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(String name,Integer orders,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ProductCategory catalog = new ProductCategory();
        catalog.setOrders(orders);
        catalog.setName(name);
        catalog.setMember(member);
        productCategoryService.save(catalog);

        ProductCategoryModel model = new ProductCategoryModel();
        model.bind(catalog);
        return Message.success(model,"添加成功");
    }

    /**
     *  修改文集
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,String name,Integer orders,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ProductCategory catalog = productCategoryService.find(id);
        if (catalog==null) {
            return Message.error("无效分类id");
        }
        catalog.setOrders(orders);
        catalog.setName(name);
        catalog.setMember(member);
        productCategoryService.save(catalog);
        ProductCategoryModel model = new ProductCategoryModel();
        model.bind(catalog);
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
        ProductCategory catalog = productCategoryService.find(id);
        if (catalog==null) {
            return Message.error("无效分类id");
        }
        if (catalog.getProducts().size()>0) {
            return Message.error("有商品不能删");
        }

        productCategoryService.delete(id);
        return Message.error("删除成功");
    }
}