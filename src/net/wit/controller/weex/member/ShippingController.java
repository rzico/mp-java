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

	/**
	 *  送货审核
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效送货id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("送货已审核");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("送货处理中，请稍候再试");
		}

		try {
			orderService.cancel(order,admin);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"关闭送货成功");
	}


	/**
	 *  送货审核
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public @ResponseBody
	Message payment(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效送货id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

//		if (order.getPaymentStatus().equals(Order.PaymentStatus.unpaid)) {
//			return Message.error("没有付款不能审核");
//		}
//
		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("送货已审核");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("送货处理中，请稍候再试");
		}

		try {
			orderService.confirm(order,admin);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"退款成功");
	}

	/**
	 *  发货
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public @ResponseBody
	Message shipping(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效送货id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("送货未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
			return Message.error("不是待发货送货");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("送货处理中，请稍候再试");
		}

		try {
			orderService.shipping(order,admin);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}
		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"退款成功");
	}


	/**
	 *  退货
	 */
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	public @ResponseBody
	Message returns(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效送货id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("送货未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped) && !order.getShippingStatus().equals(Order.ShippingStatus.returning)) {
			return Message.error("不能退货状态");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("送货处理中，请稍候再试");
		}

		try {
			orderService.returns(order,admin);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}

		for (Refunds refunds:order.getRefunds()) {

			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return Message.error("支付插件无效");
			}

			try {
				if (refunds.getStatus().equals(Refunds.Status.waiting)) {
					refundsService.refunds(refunds, request);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return Message.error(e.getMessage());
			}

			Map<String, Object> parameters = paymentPlugin.refunds(refunds,request);
			if ("SUCCESS".equals(parameters.get("return_code"))) {
				try {
					refundsService.handle(refunds);
				} catch (Exception e) {
					logger.error(e.getMessage());
					//模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
					return Message.success(parameters,"退款失败，客服会尽快处理");
				}
			} else {
				String resultCode  = null;
				try {
					resultCode = paymentPlugin.refundsQuery(refunds,request);
				} catch (Exception e) {
					e.printStackTrace();
					resultCode = "9999";
				}
				switch (resultCode) {
					case "0000":
						try {
							refundsService.handle(refunds);
						} catch (Exception e) {
							logger.error(e.getMessage());
							return Message.error("退款失败，客服会尽快处理");
						}
					case "0001":
						try {
							refundsService.close(refunds);
						} catch (Exception e) {
							logger.error(e.getMessage());
							return Message.error("退款失败，客服会尽快处理");
						}
						OrderModel model = new OrderModel();
						model.bind(order);
						return Message.success(model,"退款失败");
					default:
						return Message.error("查询失败，稍候再试");
				}
			}

		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"退款成功");
	}

	/**
	 *  退款
	 */

	@RequestMapping(value = "/refunds")
	public @ResponseBody
	Message refunds(String sn,HttpServletRequest request) {

		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效送货id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("送货未审核");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("送货处理中，请稍候再试");
		}

		try {
			if (order.getPaymentStatus().equals(Order.PaymentStatus.paid) ) {
				orderService.refunds(order, admin);
			}
			//执行退款
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}

		for (Refunds refunds:order.getRefunds()) {

			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return Message.error("支付插件无效");
			}

			try {
				if (refunds.getStatus().equals(Refunds.Status.waiting)) {
					refundsService.refunds(refunds, request);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return Message.error(e.getMessage());
			}

			Map<String, Object> parameters = paymentPlugin.refunds(refunds,request);
			if ("SUCCESS".equals(parameters.get("return_code"))) {
				try {
					refundsService.handle(refunds);
				} catch (Exception e) {
					logger.error(e.getMessage());
					//模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
					return Message.error("退款失败，客服会尽快处理");
				}
			} else {
				String resultCode  = null;
				try {
					resultCode = paymentPlugin.refundsQuery(refunds,request);
				} catch (Exception e) {
					e.printStackTrace();
					resultCode = "9999";
				}
				switch (resultCode) {
					case "0000":
						try {
							refundsService.handle(refunds);
						} catch (Exception e) {
							logger.error(e.getMessage());
							return Message.error("退款失败，客服会尽快处理");
						}
					case "0001":
						try {
							refundsService.close(refunds);
						} catch (Exception e) {
							logger.error(e.getMessage());
							return Message.error("退款失败，客服会尽快处理");
						}
						OrderModel model = new OrderModel();
						model.bind(order);
						return Message.success(model,"退款失败");
					default:
						return Message.error("正在处理中，稍候再试");
				}
			}

		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"退款成功");
	}


}