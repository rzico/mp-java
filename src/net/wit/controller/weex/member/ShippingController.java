/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.model.OrderListModel;
import net.wit.controller.model.OrderModel;
import net.wit.controller.model.ShippingListModel;
import net.wit.controller.model.ShippingModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;

import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller - 送货单
 * 
 */
@Controller("weexMemberShippingController")
@RequestMapping("/weex/member/shipping")
public class ShippingController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	/**
	 * 送货锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	Message lock(String sn) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Shipping shipping = shippingService.findBySn(sn);
		if (shipping != null && !shipping.isLocked(member.userId())) {
			shipping.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			shipping.setOperator(member.userId());
			shippingService.update(shipping);
			return Message.success(true,"true");
		}
		return Message.success(false,"false");
	}

	/**
	 *  送货情况
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Shipping shipping = shippingService.findBySn(sn);
		if (shipping==null) {
			return Message.error("无效送货id");
		}
		ShippingModel model = new ShippingModel();
		model.bind(shipping);
		return Message.bind(model,request);
	}


	/**
	 *  派单
	 */
	@RequestMapping(value = "/dispatch", method = RequestMethod.POST)
	public @ResponseBody
	Message dispatch(String sn,Long shopId,Long adminId,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Shipping shipping = shippingService.findBySn(sn);
		if (shipping==null) {
			return Message.error("无效送货id");
		}

		Shop shop = shopService.find(shopId);
		if (shop==null) {
			return Message.error("无效配送点 id");
		}

		Admin admin = null;
		if (adminId!=null) {
			admin = adminService.find(adminId);
		}
		shipping.setShop(shop);

		shipping.setEnterprise(shop.getEnterprise());
		shipping.setAdmin(admin);
		if (admin!=null) {
			shipping.setShippingStatus(Shipping.ShippingStatus.dispatch);
			shipping.setOrderStatus(Shipping.OrderStatus.confirmed);
		}

		shippingService.update(shipping);

		ShippingModel model = new ShippingModel();
		model.bind(shipping);
		return Message.bind(model,request);
	}


	/**
	 *  送达
	 */
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public @ResponseBody
	Message receive(String sn,Long [] barrelIds,Long [] retrieves,Long sends,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Shipping shipping = shippingService.findBySn(sn);
		if (shipping==null) {
			return Message.error("无效送货id");
		}


		shipping.setShippingStatus(Shipping.ShippingStatus.receive);

		shippingService.update(shipping);

		ShippingModel model = new ShippingModel();
		model.bind(shipping);
		return Message.bind(model,request);
	}



	/**
	 *  计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody
	Message calculate(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Shipping shipping = shippingService.findBySn(sn);
		if (shipping==null) {
			return Message.error("无效送货id");
		}


		shipping.setShippingStatus(Shipping.ShippingStatus.completed);

		shippingService.update(shipping);

		ShippingModel model = new ShippingModel();
		model.bind(shipping);
		return Message.bind(model,request);
	}

	/**
	 *  核销
	 */
	@RequestMapping(value = "/completed", method = RequestMethod.POST)
	public @ResponseBody
	Message completed(String sn,Long [] barrelIds,Long [] retrieves,Long sends,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Shipping shipping = shippingService.findBySn(sn);
		if (shipping==null) {
			return Message.error("无效送货id");
		}


		shipping.setShippingStatus(Shipping.ShippingStatus.completed);

		shippingService.update(shipping);

		ShippingModel model = new ShippingModel();
		model.bind(shipping);
		return Message.bind(model,request);
	}

	/**
	 * 列表  unconfirmed 待确认   confirmed 配送中  completed 已完成
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(String status,Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}
		Admin admin = adminService.findByMember(member);
		Shop shop = null;
		if (admin!=null && admin.getEnterprise()!=null) {
			shop = admin.getShop();
		}
		if (shop==null) {
			return Message.error("没有分配店铺");
		}
		List<Filter> filters = new ArrayList<Filter>();
		if ("unconfirmed".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq,Shipping.OrderStatus.unconfirmed));
		} else
		if ("confirmed".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq,Shipping.OrderStatus.unconfirmed));
		} else {
			filters.add(new Filter("orderStatus", Filter.Operator.gt,Shipping.OrderStatus.unconfirmed));
		}
		if (admin.isRole("3")) {
			filters.add(new Filter("admin", Filter.Operator.eq,admin));
		} else {
			filters.add(new Filter("shop", Filter.Operator.eq,shop));
		}
		pageable.setFilters(filters);
		pageable.setOrderDirection(net.wit.Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Shipping> page = shippingService.findPage(null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(ShippingListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}


}