/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.weex.member;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.model.ArticleModel;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.ProductModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 商品
 * 
 */
@Controller("weexMemberProductController")
@RequestMapping("/weex/member/product")
public class ProductController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "distributionServiceImpl")
	private DistributionService distributionService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkSn(String previousSn, String sn) {
		Member member = memberService.getCurrent();
		if (StringUtils.isEmpty(sn)) {
			return false;
		}
		Admin admin = adminService.findByMember(member);
		if (admin!=null && admin.getEnterprise()!=null) {
			member = admin.getEnterprise().getMember();
		}
		if (productService.snUnique(member,previousSn, sn)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody
	    Message save(String body,Product.Type type,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Admin admin = adminService.findByMember(member);
		if (admin!=null && admin.getEnterprise()!=null) {
			member = admin.getEnterprise().getMember();
		}

		GoodsModel model = JsonUtils.toObject(body,GoodsModel.class);
		if (model==null) {
			return Message.error("无效数据包");
		}

		if (type==null) {
			type = Product.Type.product;
		}

		Goods goods = null;
		Long [] tagIds =null;
		if (model.getId()==null) {
			goods = new Goods();
			goods.setRanking(0L);
			goods.setReview(0L);
			goods.setHits(0L);
		} else {
			goods = goodsService.find(model.getId());
			tagIds = goods.product().getTagIds();
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
				product.setCost(BigDecimal.ZERO);
				product.setWeight(0);
				product.setPoint(0L);
				product.setAllocatedStock(0);
				product.setDeleted(false);
			} else {
				for (Product prod:goods.getProducts()) {
					if (prod.getId().equals(pm.getProductId())) {
						product = prod;
						break;
					}
				}
				product.setDeleted(false);
			}

			product.setType(type);
			product.setName(model.getName());
			product.setUnit(model.getUnit());

			if (model.getProductCategory()!=null && model.getProductCategory().getId()!=null) {
				product.setProductCategory(productCategoryService.find(model.getProductCategory().getId()));
			}
			if (model.getDistribution()!=null && model.getDistribution().getId()!=null) {
				product.setDistribution(distributionService.find(model.getDistribution().getId()));
			}

			product.setTags(tagService.findList(tagIds));

			product.setThumbnail(pm.getThumbnail());
			product.setMarketPrice(pm.getPrice());
			product.setPrice(pm.getPrice());
			product.setVip1Price(pm.getPrice());
			product.setVip2Price(pm.getPrice());
			product.setVip3Price(pm.getPrice());
			product.setSpec1(pm.getSpec1());
			product.setSpec2(pm.getSpec2());
			product.setGoods(goods);
			product.setMember(member);
			product.setStock(pm.getStock());
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
		GoodsListModel data = new GoodsListModel();
		data.bind(goods);
		return Message.success(data,"保存成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		goodsService.delete(ids);
		return Message.success("删除成功");
	}


	/**
	 * 标签
	 */
	@RequestMapping(value = "/tag", method = RequestMethod.POST)
	public @ResponseBody
	Message view(Long id,Long [] tagIds,HttpServletRequest request) {
		Goods goods = goodsService.find(id);

		Product product = goods.product();
		product.setTags(tagService.findList(tagIds));

		productService.update(product);

		return Message.success("success");
	}


	/**
	 * 详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(Long id,HttpServletRequest request) {
		Goods goods = goodsService.find(id);
		GoodsModel model =new GoodsModel();
		model.bind(goods);
		return Message.bind(model,request);
	}

	/**
	 * 文章详情
	 */
	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public @ResponseBody
	Message article(Long id,HttpServletRequest request) {
		Goods goods = goodsService.find(id);
		Article article = goods.article();
		if (article==null) {
			return Message.success((Object) 0L,"获取成功");
		}
		return Message.success((Object) article.getId(),"获取成功");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(Long productCategoryId,String keyword,String type,Pageable pageable,HttpServletRequest request) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}
		Admin admin = adminService.findByMember(member);
		if (admin!=null && admin.getEnterprise()!=null) {
			member = admin.getEnterprise().getMember();
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Filter> filters = new ArrayList<Filter>();
		if (productCategory!=null) {
			filters.add(new Filter("productCategory", Filter.Operator.eq,productCategory));
		}
        if (keyword!=null) {
		    filters.add(Filter.like("name","%"+keyword+"%"));
		}
		if ("3".equals(bundle.getString("weex"))) {
            if (type.equals("query")) {
				Long memberId = Long.parseLong(bundle.getString("platform"));
				member = memberService.find(memberId);
			}
		}

		filters.add(new Filter("member", Filter.Operator.eq, member));
		filters.add(new Filter("isList", Filter.Operator.eq,true));
		pageable.setFilters(filters);
		pageable.setOrderDirection(Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Product> page = productService.findPage(null,null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(GoodsListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}
}