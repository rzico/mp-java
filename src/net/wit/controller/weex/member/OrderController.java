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
import java.util.*;

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

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "dragonServiceImpl")
	private DragonService dragonService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	/**
	 *  获取订单信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody Message info(Long receiverId,Long memberId,Order.ShippingMethod shippingMethod,Long dragonId) {
		Member member = memberService.getCurrent();
		if (memberId!=null) {
			member = memberService.find(memberId);
		}

		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车为空");
		}
		Receiver receiver = null;
		if (receiverId!=null) {
			receiver = receiverService.find(receiverId);
		}
		Dragon dragon = null;
		if (dragonId!=null) {
			dragon = dragonService.find(dragonId);
		}
		Order order = orderService.build(member,null,null, cart, receiver, null,null,shippingMethod,dragon);
		OrderModel model = new OrderModel();
		model.bind(order);
		if (member!=null) {
			if (receiver==null) {
				for (Receiver r : member.getReceivers()) {
					if (r.getIsDefault()) {
						receiver = r;
						break;
					}
				}
			}
			ReceiverModel m = new ReceiverModel();
			if (receiver!=null) {
				m.bind(receiver);
			}
			model.setReceiver(m);
		}
		return Message.success(model,"success");
	}

	/**
         * 计算
         */
	@RequestMapping(value = "/calculate")
	public @ResponseBody
	Message calculate(Long id,Integer quantity,Long receiverId,Long memberId,Long promotionId,Order.ShippingMethod shippingMethod,Long dragonId) {
		Member member = memberService.getCurrent();
		if (memberId!=null) {
			member = memberService.find(memberId);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		Product product = null;
		if (id!=null) {
			product = productService.find(id);
		}
		Receiver receiver = null;
		if (receiverId!=null) {
			receiver = receiverService.find(receiverId);
		}
		Dragon dragon = null;
		if (dragonId!=null) {
			dragon = dragonService.find(dragonId);
		}
		Order order = orderService.build(member,product,quantity,cart, receiver,null,promotionId,shippingMethod,dragon);

		OrderModel model = new OrderModel();
		model.bindHeader(order);
		if (member!=null) {
			if (receiver==null) {
				for (Receiver r : member.getReceivers()) {
					if (r.getIsDefault()) {
						receiver = r;
						break;
					}
				}
			}
			ReceiverModel m = new ReceiverModel();
			if (receiver!=null) {
				m.bind(receiver);
			}
			model.setReceiver(m);
		}
		return Message.success(model,"success");
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create")
	public @ResponseBody
	Message create(Long id,Integer quantity,Long receiverId,Long memberId,Long promotionId,Long xuid,String memo,Date hopeDate,Order.ShippingMethod shippingMethod,Long dragonId) {
		Member member = memberService.getCurrent();
		Member loginMember = member;
		if (memberId!=null) {
			member = memberService.find(memberId);
		}
		Cart cart = null;
		if (id==null) {
			cart = cartService.getCurrent();
			if (cart == null || cart.isEmpty()) {
				return Message.error("购物车为空");
			}
			if (cart.getIsLowStock()) {
				return Message.error("库存不足");
			}
		}
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return Message.error("无效地址");
		}
		Product product = null;
		if (id!=null) {
			product = productService.find(id);
			if (product.getIsLowStock(quantity)) {
				return Message.error("库存不足");
			}
		}
		Dragon dragon = null;
		if (dragonId!=null) {
			dragon = dragonService.find(dragonId);
		}
		Order order = orderService.create(member,product,quantity,cart, receiver,memo, xuid,null,promotionId,shippingMethod,dragon,hopeDate);
		order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
		order.setOperator(loginMember.userId());
		orderService.update(order);

		if (cart != null) {
			cartService.delete(cart);
		}

		OrderModel model = new OrderModel();
		model.bindHeader(order);
		return Message.success(model,"success");
	}

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
	Message view(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效订单id");
		}
		OrderModel model = new OrderModel();
		model.bind(order);


		ShippingTrackModel track = new ShippingTrackModel();
		track.setLng(0);
		track.setLat(0);
		if (order.getShippingMethod().equals(Order.ShippingMethod.cardbkg)) {
			track.setMethod("存入卡包");
		} else {
			track.setMethod("普通快递");
		}

		if (order.getShippings().size()>0) {
			Shipping shipping = order.getShippings().get(0);
			if (shipping.getAdmin()!=null && shipping.getAdmin().getMember()!=null) {
				Member shippingMember = shipping.getAdmin().getMember();
				if (shippingMember.getLocation()!=null) {
					track.setLng(shippingMember.getLocation().getLng());
					track.setLat(shippingMember.getLocation().getLng());
				}
				track.setMethod("同城配送");
				track.setName(shippingMember.realName());
				track.setStatus(shipping.getStatusDescr());
				track.setMobile(shippingMember.getMobile());
				track.setMemberId(shippingMember.getId());
			}
		} else {
			track.setStatus(order.getStatusDescr());
			track.setLat(0);
			track.setLng(0);
		}

		model.setTrack(track);

		return Message.bind(model,request);
	}

	/**
	 *  订单统计
	 */
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public @ResponseBody
	Message count(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}
		Admin admin = adminService.findByMember(member);
		if (admin!=null && admin.getEnterprise()!=null) {
			member = admin.getEnterprise().getMember();
		}
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("seller", Filter.Operator.eq,member));
		filters.add(new Filter("orderStatus", Filter.Operator.ne, Order.OrderStatus.cancelled));
		Long unpaid = orderService.count(null,null,"unpaid",filters);
		Long unshipped = orderService.count(null,null,"unshipped",filters);
		Long shipped = orderService.count(null,null,"shipped",filters);
		Long refund = orderService.count(null,null,"refund",filters);
		Map<String,Long> data = new HashMap<>();
		data.put("unpaid",unpaid);
		data.put("unshipped",unshipped);
		data.put("shipped",shipped);
		data.put("refund",refund);
		return Message.success(data,"success");
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
		Admin admin = adminService.findByMember(member);
		if (admin!=null && admin.getEnterprise()!=null) {
			member = admin.getEnterprise().getMember();
		}
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("seller", Filter.Operator.eq,member));
		filters.add(new Filter("orderStatus", Filter.Operator.ne, Order.OrderStatus.cancelled));
		pageable.setFilters(filters);
//		pageable.setOrderDirection(net.wit.Order.Direction.desc);
//		pageable.setOrderProperty("modifyDate");
		Page<Order> page = orderService.findPage(null,null,status,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(OrderListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}

	/**
	 *  订单审核
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
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单已审核");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			orderService.cancel(order,admin);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}

		OrderModel model = new OrderModel();
		model.bind(order);
		return Message.success(model,"关闭订单成功");
	}


	/**
	 *  订单审核
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
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

//		if (order.getPaymentStatus().equals(Order.PaymentStatus.unpaid)) {
//			return Message.error("没有付款不能审核");
//		}

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
	Message shipping(String sn, Order.ShippingMethod shippingMethod,String trackingNo, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
			return Message.error("不是待发货订单");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			if (shippingMethod==null) {
				shippingMethod = order.getShippingMethod();
			}
			orderService.shipping(order,shippingMethod,trackingNo,admin);
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
	@RequestMapping(value = "/returns")
	public @ResponseBody
	Message returns(String sn,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped) && !order.getShippingStatus().equals(Order.ShippingStatus.returning)) {
			return Message.error("不能退货状态");
		}

		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
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
			return Message.error("无效订单id");
		}

		Admin admin = adminService.findByMember(member);
		if (admin==null) {
			return Message.error("没有开通专栏");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("订单未审核");
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

	/**
	 * 支付
	 */
	@RequestMapping(value = "/payment")
	public @ResponseBody Message payment(String sn,String tranSn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("无效订单号");
		}
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		try {
			if (order.getOrderStatus() == Order.OrderStatus.unconfirmed && order.getPaymentStatus() == Order.PaymentStatus.unpaid) {
				Payment payment = orderService.payment(order, null);
				PaymentModel model = new PaymentModel();
				model.bind(payment);
				return Message.success(model, "发起成功");
			} else {
				return Message.error("不是待付款订单");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return Message.error(e.getMessage());
		}

	}
}