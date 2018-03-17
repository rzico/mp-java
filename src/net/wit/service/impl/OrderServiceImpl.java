package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.dao.*;
import net.wit.entity.Order;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
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

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;

	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;

	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "cardServiceImpl")
	private CardService cardService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

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

	public Page<Order> findPage(Date beginDate,Date endDate, String status, Pageable pageable) {
		return orderDao.findPage(beginDate,endDate,status,pageable);
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
	public Order build(Member member ,Product product, Integer quantity, Cart cart, Receiver receiver,String memo) {

//		Assert.notNull(cart);
//		Assert.notNull(cart.getMember());
//		Assert.notEmpty(cart.getCartItems());

		Order order = new Order();
		order.setPaymentStatus(Order.PaymentStatus.unpaid);
		order.setShippingStatus(Order.ShippingStatus.unshipped);
		order.setFee(new BigDecimal(0));
		order.setCouponDiscount(new BigDecimal(0));
		order.setOffsetAmount(new BigDecimal(0));
		order.setPoint(0L);
		order.setPointDiscount(BigDecimal.ZERO);
		order.setDeleted(false);
		order.setMemo(memo);
		order.setMember(member);
		order.setPaymentMethod(Order.PaymentMethod.online);
		order.setShippingMethod(Order.ShippingMethod.shipping);
		order.setIsAllocatedStock(false);
		order.setIsDistribution(false);
		order.setRebateAmount(BigDecimal.ZERO);

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

		if (member != null) {
			List<CouponCode> couponCodes = member.getCouponCodes();
			BigDecimal discount = BigDecimal.ZERO;
			for (CouponCode code : couponCodes) {
				if (code.getCoupon().getDistributor().equals(order.getSeller()) && code.getEnabled() && !code.getCoupon().getScope().equals(Coupon.Scope.shop)) {
					BigDecimal d = code.calculate(order.getPrice());
					if (d.compareTo(discount) > 0) {
						order.setCouponDiscount(d);
						order.setCouponCode(code);
					}
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
	public Order create(Member member ,Product product, Integer quantity, Cart cart, Receiver receiver, String memo,Long xuid, Admin operator) {

//		Assert.notNull(cart);
//		Assert.notNull(cart.getMember());
//		Assert.notEmpty(cart.getCartItems());

		Assert.notNull(receiver);

		Order order = build(member,product ,quantity ,cart, receiver, memo);

		order.setSn(snDao.generate(Sn.Type.order));

		order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
		order.setOperator(member.userId());

		if (order.getCouponCode() != null) {
			CouponCode couponCode = order.getCouponCode();
			couponCode.setIsUsed(true);
			couponCode.setUsedDate(new Date());
			couponCodeDao.merge(couponCode);
		}

		order.setIsAllocatedStock(true);
		order.setDeleted(false);

		order.setFee(BigDecimal.ZERO);

		Card card = member.card(order.getSeller());
		if (card!=null) {
			Long point = order.getAmount().setScale(0,BigDecimal.ROUND_DOWN).longValue();
			if (card.getPoint()>=point) {
				order.setPointDiscount(new BigDecimal(point));
			} else {
				order.setPointDiscount(new BigDecimal(card.getPoint()));
			}
		}

		if (card!=null && card.getPromoter()!=null) {
			Member promoter = card.getPromoter();
			if (promoter!=null && promoter.equals(order.getSeller())) {
				promoter = null;
			}
			order.setPromoter(promoter);
		} else
		if (xuid!=null) {
			Member promoter = memberDao.find(xuid);
			if (promoter!=null && promoter.equals(order.getSeller())) {
				promoter = null;
			}
			order.setPromoter(promoter);
		}


		orderDao.persist(order);
		cardService.decPoint(card,order.getPointDiscount().longValue(),"订单支付",order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.create);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setContent("订单创建成功");
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);

		//下单就锁定库存
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem != null) {
				Product orderProduct = orderItem.getProduct();
				if (orderProduct!=null) {
					productDao.lock(orderProduct, LockModeType.PESSIMISTIC_WRITE);
					orderProduct.setAllocatedStock(orderProduct.getAllocatedStock() + orderItem.getQuantity());
					productDao.merge(orderProduct);
					orderDao.flush();
				}
			}
		}
		if (cart!=null) {
			cartDao.remove(cart);
		}

		messageService.orderMemberPushTo(orderLog);

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

		orderDao.lock(order,LockModeType.PESSIMISTIC_WRITE);

		order.setOrderStatus(Order.OrderStatus.confirmed);
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.confirm);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setContent("卖家已经接单");
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);
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

		orderDao.lock(order,LockModeType.PESSIMISTIC_WRITE);

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
					if (product != null) {
     					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
						product.setAllocatedStock(product.getAllocatedStock() - orderItem.getQuantity());
						productDao.merge(product);
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
		orderLog.setContent("订单交易完成");
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);

		//计算货款
		if (order.getPaymentStatus().equals(Order.PaymentStatus.paid)) {
			if (order.getPaymentMethod().equals(Order.PaymentMethod.online) || order.getPaymentMethod().equals(Order.PaymentMethod.deposit)) {
				//扣除商家分配佣金
				Member seller = order.getSeller();
				memberDao.refresh(seller,LockModeType.PESSIMISTIC_WRITE);
				//扣除平台手续费
				BigDecimal samt = order.getAmountPaid().subtract(order.getFee());
				seller.setBalance(seller.getBalance().add(samt));
				memberDao.merge(seller);
				memberDao.flush();
				Deposit deposit = new Deposit();
				deposit.setBalance(seller.getBalance());
				deposit.setType(Deposit.Type.product);
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMaximumFractionDigits(2);
				deposit.setMemo("货款结算(手续费:"+nf.format(order.getFee())+")");
				deposit.setMember(seller);
				deposit.setCredit(samt);
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setOrder(order);
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);
		    }
		}

		//建立分佣关系
		memberService.create(order.getMember(),order.getSeller());
		Card card = cardService.createAndActivate(order.getMember(),order.getSeller(),order.getPromoter());
		if (card!=null) {
			memberService.create(order.getMember(),order.getPromoter());
		}

		//没有发货，或是退货等状态完成，是无效订单
		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
			card = order.getMember().card(order.getSeller());
			if (card!=null && order.getPointDiscount().compareTo(BigDecimal.ZERO)>0) {
				cardService.addPoint(card, order.getPointDiscount().longValue(), "订单退货", order);
			}
		}  else {
			memberService.addAmount(member,order.getAmount());
		}

		//判断是会员
		//计算分润
		if (order.getPromoter()!=null && order.getShippingStatus() == Order.ShippingStatus.shipped) {
			BigDecimal d = order.getDistribution();
			if (d.compareTo(BigDecimal.ZERO)>0) {
				//扣除商家分配佣金
				Member seller = order.getSeller();
				memberDao.refresh(seller, LockModeType.PESSIMISTIC_WRITE);
				seller.setBalance(seller.getBalance().subtract(d));
				if (seller.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
					memberDao.merge(seller);
					memberDao.flush();
					Deposit deposit = new Deposit();
					deposit.setBalance(seller.getBalance());
					deposit.setType(Deposit.Type.product);
					deposit.setMemo("支付分销佣金");
					deposit.setMember(seller);
					deposit.setCredit(BigDecimal.ZERO.subtract(d));
					deposit.setDebit(BigDecimal.ZERO);
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setOrder(order);
					depositDao.persist(deposit);
					messageService.depositPushTo(deposit);

					order.setRebateAmount(d);
					order.setIsDistribution(true);
					orderDao.merge(order);
				}
			}
					for (OrderItem orderItem : order.getOrderItems()) {
						if (orderItem != null) {
							Member p1 = order.getPromoter();
							Card c1 = p1.card(order.getSeller());
							BigDecimal r1 = orderItem.calcPercent1();
							if (r1.compareTo(BigDecimal.ZERO) > 0 && p1 != null && p1.leaguer(order.getSeller())) {
								memberDao.refresh(p1, LockModeType.PESSIMISTIC_WRITE);
								p1.setBalance(p1.getBalance().add(r1));
								memberDao.merge(p1);
								memberDao.flush();
								Deposit d1 = new Deposit();
								d1.setBalance(p1.getBalance());
								d1.setType(Deposit.Type.rebate);
								d1.setMemo(orderItem.getName() + "奖励金");
								d1.setMember(p1);
								d1.setCredit(r1);
								d1.setDebit(BigDecimal.ZERO);
								d1.setDeleted(false);
								d1.setOperator("system");
								d1.setOrder(order);
								depositDao.persist(d1);
								messageService.depositPushTo(d1);
							}
							Long point1 = orderItem.calcPoint1();
							if (point1.compareTo(0L) > 0 && c1 != null && p1 != null && p1.leaguer(order.getSeller())) {
								cardService.addPoint(c1, point1, orderItem.getName() + "奖励", order);
							}
							Member p2 = null;
							Card c2 = null;
							if (p1 != null) {
								c1 = p1.card(order.getSeller());
								if (c1 != null) {
									p2 = c1.getPromoter();
									if (p2!=null) {
										c2 = p2.card(order.getSeller());
									}
								}
							}
							BigDecimal r2 = orderItem.calcPercent2();
							if (r2.compareTo(BigDecimal.ZERO) > 0 && p2 != null && p2.leaguer(order.getSeller())) {
								memberDao.refresh(p2, LockModeType.PESSIMISTIC_WRITE);
								p2.setBalance(p2.getBalance().add(r2));
								memberDao.merge(p2);
								memberDao.flush();
								Deposit d2 = new Deposit();
								d2.setBalance(p2.getBalance());
								d2.setType(Deposit.Type.rebate);
								d2.setMemo(orderItem.getName() + "奖励金");
								d2.setMember(p2);
								d2.setCredit(r2);
								d2.setDebit(BigDecimal.ZERO);
								d2.setDeleted(false);
								d2.setOperator("system");
								d2.setOrder(order);
								depositDao.persist(d2);
								messageService.depositPushTo(d2);
							}
							Long point2 = orderItem.calcPoint2();
							if (point2.compareTo(0L) > 0 && c2 != null && p2 != null && p2.leaguer(order.getSeller())) {
								cardService.addPoint(c2, point2, orderItem.getName() + "奖励", order);
							}
							Member p3 = null;
							Card c3 = null;
							if (p2 != null) {
								c2 = p2.card(order.getSeller());
								if (c2 != null) {
									p3 = c2.getPromoter();
									if (p3!=null) {
										c3 = p3.card(order.getSeller());
									}
								}
							}
							BigDecimal r3 = orderItem.calcPercent3();
							if (r3.compareTo(BigDecimal.ZERO) > 0 && p3 != null && p3.leaguer(order.getSeller())) {
								memberDao.refresh(p3, LockModeType.PESSIMISTIC_WRITE);
								p3.setBalance(p3.getBalance().add(r3));
								memberDao.merge(p3);
								memberDao.flush();
								Deposit d3 = new Deposit();
								d3.setBalance(p3.getBalance());
								d3.setType(Deposit.Type.rebate);
								d3.setMemo(orderItem.getName() + "奖励金");
								d3.setMember(p3);
								d3.setCredit(r3);
								d3.setDebit(BigDecimal.ZERO);
								d3.setDeleted(false);
								d3.setOperator("system");
								d3.setOrder(order);
								depositDao.persist(d3);
								messageService.depositPushTo(d3);
							}
							Long point3 = orderItem.calcPoint3();
							if (point3.compareTo(0L) > 0 && c3 != null && p3 != null && p3.leaguer(order.getSeller())) {
								cardService.addPoint(c3, point3, orderItem.getName() + "奖励", order);
							}

						}
					}
		}

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
		orderDao.lock(order,LockModeType.PESSIMISTIC_WRITE);

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
					if (product != null) {
    					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
						product.setAllocatedStock(product.getAllocatedStock() - orderItem.getQuantity());
						productDao.merge(product);
						orderDao.flush();
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setOrderStatus(Order.OrderStatus.cancelled);
		order.setExpire(null);
		orderDao.merge(order);

		Card card = order.getMember().card(order.getSeller());
		if (card!=null && order.getPointDiscount().compareTo(BigDecimal.ZERO)>0) {
			cardService.addPoint(card, order.getPointDiscount().longValue(), "取消订单", order);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.cancel);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setContent("关闭订单成功");
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);

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

		Card card = order.getMember().card(order.getSeller());
		Payment payment = new Payment();

		order.setFee(BigDecimal.ZERO);
		if (card!=null) {
			if (card.getBalance().compareTo(order.getAmountPayable()) >= 0) {
				payment.setPaymentPluginId("cardPayPlugin");
			}
		} else
		if (payment.getPaymentPluginId()==null) {
			Member member = order.getMember();
			if (member.getBalance().compareTo(order.getAmountPayable())>=0) {
				payment.setPaymentPluginId("balancePayPlugin");
			}
		}
		if (payment.getPaymentPluginId()==null) {
			Topic topic = order.getSeller().getTopic();
			if (topic==null) {
				order.setFee(order.getAmountPayable().multiply(new BigDecimal("0.006")).setScale(2,BigDecimal.ROUND_UP));
			} else {
				order.setFee(topic.calcFee(order.getAmountPayable()));
			}
		}
		orderDao.merge(order);

		payment.setPayee(order.getSeller());
		payment.setMember(order.getMember());
		payment.setStatus(Payment.Status.waiting);
		payment.setMethod(Payment.Method.online);
		payment.setType(Payment.Type.payment);
		payment.setMemo("订单付款");
		payment.setAmount(order.getAmountPayable());
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
					if (product != null) {
					    productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
						product.setStock(product.getStock() - orderItem.getQuantity() );
						product.setAllocatedStock(product.getAllocatedStock() - orderItem.getQuantity());
						productDao.merge(product);
						orderDao.flush();
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setShippingStatus(Order.ShippingStatus.shipped);
		order.setShippingDate(new Date());
		order.setExpire(null);
		orderDao.merge(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setContent("卖家已发货");
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);
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
		if (operator==null) {
			orderLog.setContent("买家申请退款");
		} else {
			orderLog.setContent("卖家确定退款");
		}
		orderLogDao.persist(orderLog);
		if (operator==null) {
			messageService.orderSellerPushTo(orderLog);
		} else {
			messageService.orderMemberPushTo(orderLog);
		}

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

		orderDao.refresh(order, LockModeType.PESSIMISTIC_WRITE);

		if (operator==null) {
			if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
				throw new RuntimeException("不在发货状态");
			}
		} else {
			if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped) && !order.getShippingStatus().equals(Order.ShippingStatus.returning)) {
				throw new RuntimeException("不在发货状态");
			}
		}

		if (operator!=null) {

			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
					orderItem.setReturnQuantity(orderItem.getShippedQuantity());
				}
			}

			if (order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
				for (OrderItem orderItem : order.getOrderItems()) {
					if (orderItem != null) {
						Product product = orderItem.getProduct();
						if (product != null) {
							productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
							product.setStock(product.getStock() + orderItem.getQuantity() );
							productDao.merge(product);
							orderDao.flush();
						}
					}
				}
			}

			order.setShippingStatus(Order.ShippingStatus.returned);
			order.setReturnedDate(new Date());
			order.setExpire(null);
			orderDao.merge(order);
			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.returns);
			orderLog.setOperator(operator != null ? operator.getUsername() : null);
			orderLog.setContent("卖家确定退货");
			orderLog.setOrder(order);
			orderLogDao.persist(orderLog);
			messageService.orderMemberPushTo(orderLog);

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
				orderLog1.setContent("卖家确定退款");
				orderLog1.setOrder(order);
				orderLogDao.persist(orderLog1);
				messageService.orderMemberPushTo(orderLog);

			} else {
				//不要退款，自动完成
				complete(order,operator);
			}

		} else {
			order.setShippingStatus(Order.ShippingStatus.returning);
			order.setExpire(null);
			orderDao.merge(order);

			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.returns);
			orderLog.setOperator(operator != null ? operator.getUsername() : null);
			orderLog.setContent("买家申请退货");
			orderLog.setOrder(order);
			orderLogDao.persist(orderLog);
			messageService.orderSellerPushTo(orderLog);
		}

	}

	public void evictCompleted() {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("orderStatus", Filter.Operator.eq,Order.OrderStatus.confirmed));
		filters.add(new Filter("shippingStatus", Operator.le, Order.ShippingStatus.shipped));
		filters.add(new Filter("shippingDate", Operator.le, DateUtils.addDays(new Date(),-6) ));
		List<Order> data = orderDao.findList(null,null,filters,null);
		for (Order order:data) {
			try {
				complete(order,null);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

	}


}