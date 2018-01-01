/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.model.*;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.entity.Order;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller - 订单
 * 
 */
@Controller("weexMemberOrderController")
@RequestMapping("/weex/member/order")
public class OrderController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;


	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	Message lock(String sn) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order != null && !order.isLocked(member.userId())) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member.userId());
			orderService.update(order);
			return Message.success(true,"true");
		}
		return Message.success(false,"false");
	}

	/**
	 *  订单情况
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.find(id);
		if (order==null) {
			return Message.error("无效订单id");
		}
		OrderModel model = new OrderModel();
		return Message.bind(model,request);
	}

	/**
	 * 列表  unpaid 待付款   unshipped 待发货  shipped 已发货  refund 退款/售后
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(String status,Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}
		List<Filter> filters = new ArrayList<Filter>();
        if ("unpaid".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, net.wit.entity.Order.OrderStatus.unconfirmed));
			filters.add(new Filter("paymentStatus", Filter.Operator.eq, net.wit.entity.Order.PaymentStatus.unpaid));
		}
		if ("unshipped".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, net.wit.entity.Order.OrderStatus.confirmed));
			filters.add(new Filter("shippingStatus", Filter.Operator.eq, net.wit.entity.Order.ShippingStatus.unshipped));
		}
		if ("shipped".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, net.wit.entity.Order.OrderStatus.confirmed));
			filters.add(new Filter("shippingStatus", Filter.Operator.eq, net.wit.entity.Order.ShippingStatus.shipped));
		}
		if ("refunding".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, net.wit.entity.Order.OrderStatus.confirmed));
			filters.add(new Filter("paymentStatus", Filter.Operator.eq, net.wit.entity.Order.PaymentStatus.refunding));
		}
		filters.add(new Filter("seller", Filter.Operator.eq,member));
		pageable.setFilters(filters);
		pageable.setOrderDirection(net.wit.Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Order> page = orderService.findPage(null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(OrderListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}

	/**
	 *  订单审核
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public @ResponseBody
	Message payment(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.find(id);
		if (order==null) {
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("不是店铺员工");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单已审核");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
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
	Message shipping(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.find(id);
		if (order==null) {
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("不是店铺员工");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
			return Message.error("不是待发货订单");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
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
	Message returns(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.find(id);
		if (order==null) {
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("不是店铺员工");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
			return Message.error("发货状态才能退货");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			orderService.returns(order,admin);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}
		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"退款成功");
	}


	/**
	 *  退款
	 */

	@RequestMapping(value = "/refunds", method = RequestMethod.POST)
	public @ResponseBody
	Message refunds(Long id,HttpServletRequest request) {

		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.find(id);
		if (order==null) {
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("不是店铺员工");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
			return Message.error("发货状态才能退货");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
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
				if ("balancePayPlugin".equals(refunds.getPaymentPluginId()) || "cardPayPlugin".equals(refunds.getPaymentPluginId()) || "bankPayPlugin".equals(refunds.getPaymentPluginId()) || "cashPayPlugin".equals(refunds.getPaymentPluginId())) {
					try {
						refundsService.handle(refunds);
					} catch (Exception e) {
						e.printStackTrace();
						//模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
					}
				}
				return Message.success(parameters, "success");
			} else {
				try {
					refundsService.close(refunds);
				} catch (Exception e) {
					logger.error(e.getMessage());
					parameters.put("return_code","success");
					parameters.put("result_msg","撤消退款失败");
					return Message.success(parameters,"退款已提交，客服会尽快处理");
				}
				return Message.error(parameters.get("result_msg").toString());
			}

		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"退款成功");
	}


}