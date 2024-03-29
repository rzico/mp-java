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
import net.wit.controller.model.PaymentModel;
import net.wit.controller.model.ReceiverModel;
import net.wit.controller.website.BaseController;
import net.wit.entity.*;
import net.wit.entity.Order;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
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

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

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
	public @ResponseBody Message info(Long receiverId) {
		Member member = memberService.getCurrent();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车为空");
		}
		Receiver receiver = null;
		if (receiverId!=null) {
			receiver = receiverService.find(receiverId);
		}
		Order order = orderService.build(member,null,null, cart, receiver, null);
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
	Message calculate(Long id,Integer quantity,Long receiverId) {
		Member member = memberService.getCurrent();
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
		Order order = orderService.build(member,product,quantity,cart, receiver,null);

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
	Message create(Long id,Integer quantity,Long receiverId,Long xuid,String memo) {
		Member member = memberService.getCurrent();
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
		Order order = orderService.create(member,product,quantity,cart, receiver,memo, xuid,null);
		if (cart != null) {
			cartService.delete(cart);
		}
		OrderModel model = new OrderModel();
		model.bindHeader(order);
		return Message.success(model,"success");
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/payment")
	public @ResponseBody Message payment(String sn) {
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
	 * 退款
	 */
	@RequestMapping(value = "/refunds", method = RequestMethod.POST)
	public @ResponseBody
	Message refunds(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		if (member.equals(order.getMember()) && order.getOrderStatus() == Order.OrderStatus.confirmed && order.getPaymentStatus() == Order.PaymentStatus.paid) {
			try {
				orderService.refunds(order, null);
				return Message.success("退款已提交");
			} catch (Exception e) {
				return Message.error(e.getMessage());
			}
		} else {
			return Message.success("不能退款");
		}
	}

	/**
	 * 退货
	 */
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	public @ResponseBody
	Message returns(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		if (member.equals(order.getMember()) && order.getOrderStatus() == Order.OrderStatus.confirmed && order.getShippingStatus() == Order.ShippingStatus.shipped) {
			try {
				orderService.returns(order, null);
				return Message.success("退货已提交");
			} catch (Exception e) {
				return Message.error(e.getMessage());
			}
		} else {
			return Message.success("不能退货");
		}
	}

	/**
	 * 签收
	 */
	@RequestMapping(value = "/completed")
	public @ResponseBody
	Message completed(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order.isLocked(member.userId())) {
			return Message.error("订单处理中，请稍候再试");
		}
		if (member.equals(order.getMember()) && order.getOrderStatus() == Order.OrderStatus.confirmed && order.getShippingStatus() == Order.ShippingStatus.shipped) {
			try {
				orderService.complete(order, null);

				return Message.success("签收成功");
			} catch (Exception e) {
				return Message.error(e.getMessage());
			}
		} else {
			return Message.success("不能签收订单");
		}
	}

	/**
	 * 提醒卖家发货
	 */
	@RequestMapping(value = "/shipp_remind", method = RequestMethod.POST)
	public @ResponseBody
	Message shippRemind(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order==null) {
			return Message.error("sn订单无效");
		}
		OrderLog orderLog = new OrderLog();
		orderLog.setOrder(order);
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setContent("请卖家尽快发货");
		orderLog.setOperator(member.userId());
		messageService.orderSellerPushTo(orderLog);

		return Message.success("提醒成功");
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
	public @ResponseBody
	Message deliveryQuery(String sn) {
		return Message.error("");
	}

	/**
	 * 列表  unpaid 待付款   unshipped 待发货  shipped 已发货  refund 退款/售后
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(Long authorId,String status, Pageable pageable, HttpServletRequest request) {

		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("member", Filter.Operator.eq,member));
		if (authorId!=null) {
			Member seller = memberService.find(authorId);
			if (seller!=null) {
				filters.add(new Filter("seller", Filter.Operator.eq,seller));
			}
		}


		pageable.setFilters(filters);
		pageable.setOrderDirection(net.wit.Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Order> page = orderService.findPage(null,null,status,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(OrderListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}


	@RequestMapping(value = "/promoter", method = RequestMethod.GET)
	public @ResponseBody
	Message promoter(Long authorId,Pageable pageable, HttpServletRequest request) {

		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error(Message.SESSION_INVAILD);
		}

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("promoter", Filter.Operator.eq,member));
		filters.add(new Filter("orderStatus", Filter.Operator.eq,Order.OrderStatus.completed));
		if (authorId!=null) {
			Member owner = memberService.find(authorId);
			if (owner!=null) {
				filters.add(new Filter("seller", Filter.Operator.eq,owner));
			}
		}
		pageable.setFilters(filters);
		pageable.setOrderDirection(net.wit.Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");
		Page<Order> page = orderService.findPage(null,null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(OrderListModel.bindAndRebate(page.getContent()));
		return Message.bind(model,request);
	}



}