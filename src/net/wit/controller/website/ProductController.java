/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.website;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.GoodsViewModel;
import net.wit.controller.model.ProductModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 商品
 * 
 */
@Controller("websiteProductController")
@RequestMapping("/website/product")
public class ProductController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	/**
	 * 详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(Long id,HttpServletRequest request) {
		Goods goods = goodsService.find(id);
		GoodsViewModel model =new GoodsViewModel();
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
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("goods", Filter.Operator.eq,goods));
		filters.add(new Filter("deleted", Filter.Operator.eq,false));
		List<Article> art = articleService.findList(null,null,filters,null);
		if (art.size()==0) {
			return Message.error("没有详情");
		}
		return Message.success((Object) art.get(0).getId(),"获取成功");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(Long productCategoryId,Pageable pageable,HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Filter> filters = new ArrayList<Filter>();
		if (productCategory!=null) {
			filters.add(new Filter("productCategory", Filter.Operator.eq,productCategory));
		}
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