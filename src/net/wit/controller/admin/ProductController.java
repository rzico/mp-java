package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.ProductModel;
import net.wit.util.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Product;
import net.wit.service.ProductService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ProductController
 * @author 降魔战队
 * @date 2017-10-11 15:37:12
 */
 
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseController {
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "distributionServiceImpl")
	private DistributionService distributionService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
//		model.addAttribute("productCategorys",productCategoryService.findAll());

		return "/admin/product/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategorys",productCategoryService.findAll());
		model.addAttribute("distributions",distributionService.findAll());

		return "/admin/product/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(String body, HttpServletRequest request, RedirectAttributes redirectAttributes){
		Member member = null;
		Admin admin = adminService.getCurrent();
		if (admin!=null && admin.getEnterprise()!=null) {
			member = admin.getEnterprise().getMember();
		}

		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}


		GoodsModel model = JsonUtils.toObject(body,GoodsModel.class);
		if (model==null) {
			return Message.error("无效数据包");
		}

		Goods goods = null;
		if (model.getId()==null) {
			goods = new Goods();
			goods.setRanking(0L);
		} else {
			goods = goodsService.find(model.getId());
		}

		List<Product> products = new ArrayList<Product>();
		int i = 0;
		for (Product product:goods.getProducts()) {
			product.setDeleted(true);
		}

		for (ProductModel pm:model.getProducts()) {
			Product product = null;
			if (pm.getProductId()==null) {
				product = new Product();
			} else {
				for (Product prod:goods.getProducts()) {
					if (prod.getId().equals(pm.getProductId())) {
						product = prod;
						break;
					}
				}
				product.setDeleted(false);
			}
			product.setName(model.getName());
			product.setUnit(model.getUnit());
			if (model.getProductCategory()!=null && model.getProductCategory().getId()!=null) {
				product.setProductCategory(productCategoryService.find(model.getProductCategory().getId()));
			}
			if (model.getDistribution()!=null && model.getDistribution().getId()!=null) {
				product.setDistribution(distributionService.find(model.getDistribution().getId()));
			}
			product.setThumbnail(pm.getThumbnail());
			product.setMarketPrice(pm.getPrice());
			product.setPrice(pm.getPrice());
			product.setVip1Price(pm.getPrice());
			product.setVip2Price(pm.getPrice());
			product.setVip3Price(pm.getPrice());
			product.setSpec1(pm.getSpec1());
			product.setSpec2(pm.getSpec2());
			product.setCost(BigDecimal.ZERO);
			product.setDeleted(false);
			product.setWeight(0);
			product.setPoint(0L);
			product.setGoods(goods);
			product.setMember(member);
			product.setStock(pm.getStock());
			product.setAllocatedStock(0);
			i = i+1;
			product.setOrders(i);
			if (i==1) {
				product.setIsList(true);
			} else {
				product.setIsList(false);
			}
			product.setIsMarketable(true);
			products.add(product);
		}
		goods.getProducts().addAll(products);
		goodsService.save(goods);
		//GoodsListModel data = new GoodsListModel();
		//data.bind(goods);
		return Message.success(goods.getProducts().get(0),"保存成功");
	}


	/**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        try {
			for(long id:ids){
				Goods goods = productService.find(id).getGoods();
				for(Product product: goods.getProducts()){
					product.setDeleted(true);
				}
				goodsService.save(goods);
			}
            //goodsService.delete(ids);
            return Message.success("admin.delete.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.delete.error");
        }
    }
	
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategorys",productCategoryService.findAll());
		model.addAttribute("distributions",distributionService.findAll());
		model.addAttribute("products",productService.find(id).getGoods().getProducts());

		return "/admin/product/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Product product, Long goodsId, Long productCategoryId){
		Product entity = productService.find(product.getId());
		
		entity.setCreateDate(product.getCreateDate());

		entity.setModifyDate(product.getModifyDate());

		entity.setCost(product.getCost());

		entity.setDeleted(product.getDeleted());

		entity.setIsList(product.getIsList());

		entity.setIsMarketable(product.getIsMarketable());

		entity.setMarketPrice(product.getMarketPrice());

		entity.setName(product.getName());

		entity.setPoint(product.getPoint() == null ? 0 : product.getPoint());

		entity.setPrice(product.getPrice());

		entity.setSn(product.getSn());

		entity.setSpec1(product.getSpec1());
		entity.setSpec2(product.getSpec2());

		entity.setUnit(product.getUnit());

		entity.setVip1Price(product.getVip1Price());

		entity.setVip2Price(product.getVip2Price());

		entity.setVip3Price(product.getVip3Price());

		entity.setWeight(product.getWeight() == null ? 0 : product.getWeight());

		entity.setGoods(goodsService.find(goodsId));

		entity.setProductCategory(productCategoryService.find(productCategoryId));

		entity.setThumbnail(product.getThumbnail());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            productService.update(entity);
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Long productCategoryId,String searchValue, Pageable pageable, ModelMap model) {
		Member member = null;
		Admin admin = adminService.getCurrent();
		if (admin != null && admin.getEnterprise() != null){
			member = admin.getEnterprise().getMember();
		}

		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		ArrayList<Filter> filters = (ArrayList<Filter>)pageable.getFilters();
		if(productCategory != null){
			Filter productCategoryFilter = new Filter("productCategory",Filter.Operator.eq,productCategory);
			filters.add(productCategoryFilter);
		}
		Filter isList = new Filter("isList",Filter.Operator.eq,true);
		filters.add(isList);

		Enterprise enterprise=admin.getEnterprise();

		if(enterprise==null){
			return Message.error("您还未绑定企业");
		}
		//判断企业是否被删除
		if(enterprise.getDeleted()){
			Message.error("您的企业不存在");
		}
		//代理商(無權限)
		//个人代理商(無權限)
		//商家
		if(enterprise.getType()== Enterprise.Type.shop){
			if(enterprise.getMember()!=null){
				Filter mediaTypeFilter = new Filter("member", Filter.Operator.eq, enterprise.getMember());
				filters.add(mediaTypeFilter);
			}
			else{
				return Message.error("该商家未绑定");
			}
		}

		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Product> page = productService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 商品管理视图
	 */
	@RequestMapping(value = "/goodsView", method = RequestMethod.GET)
	public String goodsView(Long id, ModelMap model) {
		model.addAttribute("goods",goodsService.find(id));
		return "/admin/product/view/goodsView";
	}


	/**
	 * 商品分类视图
	 */
	@RequestMapping(value = "/productCategoryView", method = RequestMethod.GET)
	public String productCategoryView(Long id, ModelMap model) {
		model.addAttribute("members",memberService.findAll());

		model.addAttribute("productCategory",productCategoryService.find(id));
		return "/admin/product/view/productCategoryView";
	}



}