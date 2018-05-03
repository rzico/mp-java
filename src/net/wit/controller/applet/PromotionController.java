/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.PromotionListModel;
import net.wit.controller.model.PromotionModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
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
 * Controller - 促销
 * 
 */
@Controller("appletPromotionController")
@RequestMapping("/applet/promotion")
public class PromotionController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	/**
	 * 详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(Long id,HttpServletRequest request) {
		Promotion promotion = promotionService.find(id);
		PromotionModel model =new PromotionModel();
		model.bind(promotion);
		return Message.bind(model,request);
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(Long goodsId,Pageable pageable,HttpServletRequest request) {
		Goods goods = goodsService.find(goodsId);
		List<Filter> filters = new ArrayList<Filter>();
		if (goods!=null) {
			filters.add(new Filter("goods", Filter.Operator.eq,goods));
		}
		pageable.setFilters(filters);
		pageable.setOrderDirection(Order.Direction.asc);
		pageable.setOrderProperty("type");
		Page<Promotion> page = promotionService.findPage(null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(PromotionListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}

}