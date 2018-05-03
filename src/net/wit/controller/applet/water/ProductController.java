/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet.water;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.model.CouponModel;
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
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;


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
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("type", Filter.Operator.eq,Coupon.Type.exchange));
		filters.add(new Filter("deleted", Filter.Operator.eq,false));
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Coupon> page = couponService.findList(null,null,filters,null);
		List<Product> products = new ArrayList<>();
		for (Coupon coupon:page) {
			Product product = coupon.getGoods().product();
			if (product.getProductCategory()!=null && product.getProductCategory().equals(productCategory)) {
				products.add(product);
			}
		}
		return Message.bind(ProductModel.bindList(products.subList(pageable.getPageStart(),pageable.getPageStart()+pageable.getPageSize())),request);
	}

}