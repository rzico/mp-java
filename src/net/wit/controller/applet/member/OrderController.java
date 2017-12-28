/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.model.OrderListModel;
import net.wit.controller.model.OrderModel;
import net.wit.controller.website.BaseController;
import net.wit.entity.*;
import net.wit.entity.Order;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Controller - 会员中心 - 订单
 * 
 * @version 3.0
 */
@Controller("appletMemberOrderController")
@RequestMapping("/applet/member/order")
public class OrderController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

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
		if (order != null && member.equals(order.getMember()) && !order.isExpired() && !order.isLocked(member.userId())) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member.userId());
			orderService.update(order);
			return Message.success(true,"true");
		}
		return Message.success(false,"false");
	}

	/**
	 *  获取订单信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody Message info() {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车为空");
		}
		Order order = orderService.build(null,null, cart, null, null);
		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"success");
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody
	Message calculate(Long id,Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车为空");
		}
		Product product = null;
		if (id!=null) {
			product = productService.find(id);
		}
		Order order = orderService.build(product,quantity,cart, null,null);

		OrderModel model = new OrderModel();
		model.bindHeader(order);
		return Message.success(model,"success");
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	Message create(Long id,Integer quantity,Long receiverId,String memo) {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车为空");
		}
		if (cart.getIsLowStock()) {
			return Message.error("库存不足");
		}
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return Message.error("无效地址");
		}
		Product product = null;
		if (id!=null) {
			product = productService.find(id);
		}
		Order order = orderService.create(product,quantity,cart, receiver,memo, null);

		OrderModel model = new OrderModel();
		model.bindHeader(order);
		return Message.success(model,"success");
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public @ResponseBody Message payment(String sn, ModelMap model) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效订单号");
		}
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		try {
			if (member.equals(order.getMember()) && order.getOrderStatus() == Order.OrderStatus.unconfirmed && order.getPaymentStatus() == Order.PaymentStatus.unpaid) {
				Payment payment = orderService.payment(order, null);
				return Message.success((Object) payment.getSn(), "发起成功");
			} else {
				return Message.error("不是待付款订单");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return Message.error(e.getMessage());
		}
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(String sn) {
		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效订单号");
		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"success");
	}

	/**
	 * 关闭
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		if (member.equals(order.getMember()) && order.getOrderStatus() == Order.OrderStatus.unconfirmed && order.getPaymentStatus() == Order.PaymentStatus.unpaid) {
			try {
				orderService.cancel(order, null);
				return Message.success("关闭成功");
			} catch (Exception e) {
				return Message.error(e.getMessage());
			}
		} else {
			return Message.success("不能关闭订单");
		}
	}

	/**
	 * 签收
	 */
	@RequestMapping(value = "/completed", method = RequestMethod.POST)
	public @ResponseBody
	Message completed(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		if (member.equals(order.getMember()) && order.getOrderStatus() == Order.OrderStatus.confirmed && order.getShippingStatus() == Order.ShippingStatus.shipped) {
			try {
				orderService.cancel(order, null);
				return Message.success("关闭成功");
			} catch (Exception e) {
				return Message.error(e.getMessage());
			}
		} else {
			return Message.success("不能签收订单");
		}
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
	public @ResponseBody
	Message deliveryQuery(String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		return Message.success(data,"查询成功");
	}

	/**
	 * 列表  unpaid 待付款   unshipped 待发货  shipped 已发货  refund 退款/售后
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(String status, Pageable pageable, HttpServletRequest request) {

		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		List<Filter> filters = new ArrayList<Filter>();
		if ("unpaid".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, Order.OrderStatus.unconfirmed));
			filters.add(new Filter("paymentStatus", Filter.Operator.eq, Order.PaymentStatus.unpaid));
		}
		if ("unshipped".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, Order.OrderStatus.confirmed));
			filters.add(new Filter("shippingStatus", Filter.Operator.eq, Order.ShippingStatus.unshipped));
		}
		if ("shipped".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, Order.OrderStatus.confirmed));
			filters.add(new Filter("shippingStatus", Filter.Operator.eq, Order.ShippingStatus.shipped));
		}
		if ("refunding".equals(status)) {
			filters.add(new Filter("orderStatus", Filter.Operator.eq, Order.OrderStatus.confirmed));
			filters.add(new Filter("paymentStatus", Filter.Operator.eq, Order.PaymentStatus.refunding));
		}
		filters.add(new Filter("member", Filter.Operator.eq,member));

		pageable.setFilters(filters);
		pageable.setOrderDirection(net.wit.Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Order> page = orderService.findPage(null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(OrderListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}



}