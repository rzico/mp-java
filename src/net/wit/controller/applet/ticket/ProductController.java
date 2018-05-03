/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet.ticket;

import net.wit.*;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.Goods;
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
 * Controller - 电子券
 * 
 */
@Controller("appletTicketProductController")
@RequestMapping("/applet/ticket/product")
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
	Message list(Long authorId,Long productCategoryId,Pageable pageable,HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Filter> filters = new ArrayList<Filter>();
		if (productCategory!=null) {
			filters.add(new Filter("productCategory", Filter.Operator.eq,productCategory));
		}
		if (authorId!=null) {
			Member member = memberService.find(authorId);
			if (member != null) {
				filters.add(new Filter("member", Filter.Operator.eq, member));
			}
		}
		filters.add(new Filter("isList", Filter.Operator.eq,true));
		filters.add(new Filter("type", Filter.Operator.eq, Product.Type.dummy));
		pageable.setFilters(filters);
		pageable.setOrderDirection(Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Product> page = productService.findPage(null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(GoodsListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}

}