/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet.shop;

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
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 商品
 * 
 */
@Controller("appletShopProductController")
@RequestMapping("/applet/shop/product")
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
	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(Long id,HttpServletRequest request) {
		Goods goods = goodsService.find(id);
		goods.setHits(goods.getHits()+1);
		goodsService.update(goods);
		GoodsViewModel model =new GoodsViewModel();
		model.bind(goods);
		return Message.bind(model,request);
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(Long authorId,Long productCategoryId,String keyword,Long tagId ,Pageable pageable,HttpServletRequest request) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (authorId==null) {
			authorId = Long.parseLong(bundle.getString("platform"));
		}
		Member member = memberService.find(authorId);
		if (member==null) {
			return Message.error("作者id无效");
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Filter> filters = new ArrayList<Filter>();
		if (productCategory!=null) {
			filters.add(new Filter("productCategory", Filter.Operator.eq,productCategory));
		}
		filters.add(new Filter("member", Filter.Operator.eq,member));
		filters.add(new Filter("isList", Filter.Operator.eq,true));
		pageable.setFilters(filters);
		pageable.setOrderDirection(Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");

		Tag tag = null;
		if (tagId!=null) {
			tag = tagService.find(tagId);
		}

		if (keyword!=null) {
			filters.add(Filter.like("name", "%" + keyword + "%"));
		}

		Page<Product> page = productService.findPage(null,null,tag,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(GoodsListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody
	Message search(Long authorId,Long productCategoryId,String keyword,Pageable pageable,HttpServletRequest request) {
		Member member = memberService.find(authorId);
		if (member==null) {
			return Message.error("作者id无效");
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Filter> filters = new ArrayList<Filter>();
		if (productCategory!=null) {
			filters.add(new Filter("productCategory", Filter.Operator.eq,productCategory));
		}
		if (keyword!=null) {
			filters.add(Filter.like("name","%"+keyword+"%"));
		}
		filters.add(new Filter("member", Filter.Operator.eq,member));
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