package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.*;
import net.wit.service.SnService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.OrderService;
import org.springframework.util.Assert;

/**
 * @ClassName: OrderDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {
	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;

	@Resource(name = "productStockDaoImpl")
	private ProductStockDao productStockDao;

	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;

	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "cartDaoImpl")
	private CartDao cartDao;

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Resource(name = "orderDaoImpl")
	public void setBaseDao(OrderDao orderDao) {
		super.setBaseDao(orderDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Order order) {
		super.save(order);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Order update(Order order) {
		return super.update(order);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Order update(Order order, String... ignoreProperties) {
		return super.update(order, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Order order) {
		super.delete(order);
	}

	public Page<Order> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return orderDao.findPage(beginDate,endDate,pageable);
	}

	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public Order findBySn(String sn) {
		return orderDao.findBySn(sn);
	}

	/**
	 * 释放过期订单库存
	 */
	public void releaseStock() {
		orderDao.releaseStock();
	}

	/**
	 * 生成订单
	 *
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	public Order build(Product product, Integer quantity, Cart cart, Receiver receiver,String memo) {

		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());

		Member member = cart.getMember();

		Order order = new Order();
		order.setShippingStatus(Order.ShippingStatus.unshipped);
		order.setFee(new BigDecimal(0));
		order.setCouponDiscount(new BigDecimal(0));
		order.setOffsetAmount(new BigDecimal(0));
		order.setPoint(cart.getEffectivePoint());
		order.setMemo(memo);
		order.setMember(cart.getMember());
		order.setPaymentMethod(Order.PaymentMethod.online);

		if (receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}

		List<OrderItem> orderItems = order.getOrderItems();
		if (product!=null) {
			OrderItem orderItem = new OrderItem();
			orderItem.setName(product.getName());
			orderItem.setSpec(product.getSpec());
			orderItem.setPrice(product.getPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setIsGift(false);
			orderItem.setQuantity(quantity);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnQuantity(0);
			orderItem.setProduct(product);
			orderItem.setOrder(order);
			orderItems.add(orderItem);
			order.setSeller(product.getMember());
		} else {
			for (CartItem cartItem : cart.getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null) {
					Product cartProduct = cartItem.getProduct();
					OrderItem orderItem = new OrderItem();
					orderItem.setName(cartProduct.getName());
					orderItem.setSpec(cartProduct.getSpec());
					orderItem.setPrice(cartItem.getPrice());
					orderItem.setWeight(cartProduct.getWeight());
					orderItem.setThumbnail(cartProduct.getThumbnail());
					orderItem.setIsGift(false);
					orderItem.setQuantity(cartItem.getQuantity());
					orderItem.setShippedQuantity(0);
					orderItem.setReturnQuantity(0);
					orderItem.setProduct(cartProduct);
					orderItem.setOrder(order);
					orderItems.add(orderItem);
					order.setSeller(cartItem.getSeller());
				}
			}
		}

//		BigDecimal freight = shippingMethod.calculateFreight(cart.getWeight());
//		for (Promotion promotion : cart.getPromotions()) {
//			if (promotion.getIsFreeShipping()) {
//				freight = new BigDecimal(0);
//				break;
//			}
//		}

		order.setFreight(BigDecimal.ZERO);

		List<CouponCode> couponCodes = member.getCouponCodes();
		BigDecimal discount = BigDecimal.ZERO;
		for (CouponCode code:couponCodes) {
			if (code.getCoupon().getDistributor().equals(order.getSeller()) && code.getEnabled() && !code.getCoupon().getScope().equals(Coupon.Scope.shop)) {
				BigDecimal d = code.calculate(order.getPrice());
				if (d.compareTo(discount) > 0) {
					order.setCouponDiscount(d);
					order.setCouponCode(code);
				}
			}
		}

		order.setAmountPaid(new BigDecimal(0));


		order.setOrderStatus(Order.OrderStatus.unconfirmed);
		order.setPaymentStatus(Order.PaymentStatus.unpaid);

        //30分钟不付款过期
		order.setExpire(DateUtils.addMinutes(new Date(),30));

		return order;

	}

	/**
	 * 创建订单
	 *
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param memo
	 *            附言
	 * @param operator
	 *            操作员
	 * @return 订单
	 */
	public Order create(Product product, Integer quantity, Cart cart, Receiver receiver, String memo, Admin operator) {

		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());
		Assert.notNull(receiver);

		Order order = build(product ,quantity ,cart, receiver, memo);

		order.setSn(snDao.generate(Sn.Type.order));

		order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
		order.setOperator(cart.getMember().userId());

		if (order.getCouponCode() != null) {
			CouponCode couponCode = order.getCouponCode();
			couponCode.setIsUsed(true);
			couponCode.setUsedDate(new Date());
			couponCodeDao.merge(couponCode);
		}

		order.setIsAllocatedStock(true);

		orderDao.persist(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		//下单就锁定库存
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem != null) {
				Product orderProduct = orderItem.getProduct();
				ProductStock productStock = orderProduct.getProductStock(order.getSeller());
				productStockDao.lock(productStock, LockModeType.PESSIMISTIC_WRITE);
				if (productStock != null && productStock.getStock() != null) {
					productStock.setAllocatedStock(productStock.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
					productStockDao.merge(productStock);
					orderDao.flush();
				}
			}
		}
		cartDao.remove(cart);
		return order;
	}

	/**
	 * 更新订单
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void update(Order order, Admin operator) {
		return ;
	}

	/**
	 * 订单确认
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void confirm(Order order, Admin operator)  throws Exception {
		Assert.notNull(order);

		order.setOrderStatus(Order.OrderStatus.confirmed);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.confirm);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		return ;
	}

	/**
	 * 订单完成
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void complete(Order order, Admin operator)  throws Exception {
		Assert.notNull(order);

		Member member = order.getMember();
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
//
//		if (order.getShippingStatus() == Order.ShippingStatus.shipped) {
//			member.setPoint(member.getPoint() + order.getPoint());
//		}

		if (order.getShippingStatus() == Order.ShippingStatus.unshipped || order.getShippingStatus() == Order.ShippingStatus.returned) {
			CouponCode couponCode = order.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(false);
				couponCode.setUsedDate(null);
				couponCodeDao.merge(couponCode);

				order.setCouponCode(null);
				orderDao.merge(order);
			}
		}

//		member.setAmount(member.getAmount().add(order.getAmountPaid()));
		memberDao.merge(member);

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					ProductStock productStock = product.getProductStock(order.getSeller());
					productStockDao.lock(productStock, LockModeType.PESSIMISTIC_WRITE);
					if (productStock != null && productStock.getStock() != null) {
						productStock.setAllocatedStock(productStock.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productStockDao.merge(productStock);
						orderDao.flush();
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setOrderStatus(Order.OrderStatus.completed);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		return ;

	}

	/**
	 * 订单取消
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void cancel(Order order, Admin operator)  throws Exception {
		Assert.notNull(order);

		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			couponCode.setIsUsed(false);
			couponCode.setUsedDate(null);
			couponCodeDao.merge(couponCode);

			order.setCouponCode(null);
			orderDao.merge(order);
		}

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					ProductStock productStock = product.getProductStock(order.getSeller());
					productStockDao.lock(productStock, LockModeType.PESSIMISTIC_WRITE);
					if (productStock != null && productStock.getStock() != null) {
						productStock.setAllocatedStock(productStock.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productStockDao.merge(productStock);
						orderDao.flush();
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setOrderStatus(Order.OrderStatus.cancelled);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.cancel);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		return;
	}

	/**
	 * 订单支付
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public Payment payment(Order order,Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		Payment payment = new Payment();
		payment.setPayee(order.getSeller());
		payment.setMember(order.getMember());
		payment.setStatus(Payment.Status.waiting);
		payment.setMethod(Payment.Method.online);
		payment.setType(Payment.Type.payment);
		payment.setMemo("订单付款");
		payment.setAmount(order.getAmount());
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setOrder(order);
		paymentDao.persist(payment);
		return payment;

	}

	/**
	 * 订单发货
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void shipping(Order order, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		for (OrderItem orderItem:order.getOrderItems()) {
			orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
			orderItem.setShippedQuantity(orderItem.getQuantity());
		}

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					ProductStock productStock = product.getProductStock(order.getSeller());
					productStockDao.lock(productStock, LockModeType.PESSIMISTIC_WRITE);
					if (productStock != null && productStock.getStock() != null) {
						productStock.setAllocatedStock(productStock.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productStockDao.merge(productStock);
						orderDao.flush();
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setShippingStatus(Order.ShippingStatus.shipped);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		return;
	}

	public void refunds(Order order,Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.refresh(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getPaymentStatus().equals(Order.PaymentStatus.paid)) {
			throw new RuntimeException("不能重复操作");
		}

		order.setAmountPaid(order.getAmountPaid());
		order.setExpire(null);
		order.setPaymentStatus(Order.PaymentStatus.refunding);
		orderDao.merge(order);

		for (Payment payment:order.getPayments()) {
			if (payment.getStatus().equals(Payment.Status.success)) {
				Refunds refunds = new Refunds();
				refunds.setPaymentMethod(payment.getPaymentMethod());
				refunds.setPayment(payment);
				refunds.setPaymentPluginId(payment.getPaymentPluginId());
				refunds.setStatus(Refunds.Status.waiting);
				refunds.setAmount(payment.getAmount());
				refunds.setOrder(order);
				refunds.setMember(payment.getMember());
				refunds.setPayee(payment.getPayee());
				refunds.setMemo("订单退款");
				refunds.setMethod(Refunds.Method.values()[payment.getMethod().ordinal()]);
				refunds.setType(Refunds.Type.values()[payment.getType().ordinal()]);
				refunds.setSn(snService.generate(Sn.Type.refunds));
				refundsDao.persist(refunds);
			}
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.refunds);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

	}

	/**
	 * 订单退货
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */

	public void returns(Order order, Admin operator)  throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem != null) {
				orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
				orderItem.setReturnQuantity(orderItem.getShippedQuantity());
			}
		}
		order.setShippingStatus(Order.ShippingStatus.returned);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.returns);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		if (order.getPaymentStatus().equals(Order.PaymentStatus.paid)) {

			order.setAmountPaid(order.getAmountPaid());
			order.setExpire(null);
			order.setPaymentStatus(Order.PaymentStatus.refunding);
			orderDao.merge(order);

			for (Payment payment:order.getPayments()) {
				if (payment.getStatus().equals(Payment.Status.success)) {
					Refunds refunds = new Refunds();
					refunds.setPaymentMethod(payment.getPaymentMethod());
					refunds.setPayment(payment);
					refunds.setPaymentPluginId(payment.getPaymentPluginId());
					refunds.setStatus(Refunds.Status.waiting);
					refunds.setAmount(payment.getAmount());
					refunds.setOrder(order);
					refunds.setMember(payment.getMember());
					refunds.setPayee(payment.getPayee());
					refunds.setMemo("订单退款");
					refunds.setMethod(Refunds.Method.values()[payment.getMethod().ordinal()]);
					refunds.setType(Refunds.Type.values()[payment.getType().ordinal()]);
					refunds.setSn(snService.generate(Sn.Type.refunds));
					refundsDao.persist(refunds);
				}
			}

			OrderLog orderLog1 = new OrderLog();
			orderLog1.setType(OrderLog.Type.refunds);
			orderLog1.setOperator(operator != null ? operator.getUsername() : null);
			orderLog1.setOrder(order);
			orderLogDao.persist(orderLog1);
		}
	}

}