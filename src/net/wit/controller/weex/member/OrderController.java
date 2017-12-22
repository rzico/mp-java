/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.OrderListModel;
import net.wit.controller.model.ProductModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.entity.Order;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}