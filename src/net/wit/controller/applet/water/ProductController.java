/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet.water;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.model.CouponModel;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.ProductModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controller - 电子券
 * 
 */
@Controller("appletWaterProductController")
@RequestMapping("/applet/water/product")
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
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

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
		filters.add(new Filter("type", Filter.Operator.eq,Product.Type.warehouse));
		filters.add(new Filter("isList", Filter.Operator.eq,true));
		pageable.setFilters(filters);
		pageable.setOrderDirection(Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Product> page = productService.findPage(null,null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(GoodsListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}


	/**
	 * 列表
	 */
	@RequestMapping(value = "/water", method = RequestMethod.GET)
	public @ResponseBody
	Message water(Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("member", Filter.Operator.eq,member));
		filters.add(new Filter("stock", Filter.Operator.ge,0L));
		List<CouponCode> page = couponCodeService.findList(null,null,filters,null);
		List<Product> products = new ArrayList<>();
		for (CouponCode couponCode:page) {
			Coupon coupon = couponCode.getCoupon();
			if (coupon.getType().equals(Coupon.Type.exchange)) {
				Product product = coupon.getGoods().product();
				products.add(product);
			}
		}
		return Message.bind(GoodsListModel.bindList(products),request);
	}

}