package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.Message;
import net.wit.dao.*;
import net.wit.entity.Order;
import net.wit.entity.summary.OrderSummary;
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
 * @author 降魔战队
 * @ClassName: OrderDaoImpl
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

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

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

	@Resource(name = "orderRankingServiceImpl")
	private OrderRankingService orderRankingService;

	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "rebateServiceImpl")
	private RebateService rebateService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

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

	public Page<Order> findPage(Date beginDate, Date endDate, String status, Pageable pageable) {
		return orderDao.findPage(beginDate, endDate, status, pageable);
	}

	public Long count(Date beginDate, Date endDate, String status, List<Filter> filters) {
		return orderDao.count(beginDate, endDate, status, filters);
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
	public void releaseStock(Long orderId) {
		Order order = orderDao.find(orderId,LockModeType.PESSIMISTIC_WRITE);
		if (order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)
				&&
			order.getShippingStatus().equals(Order.ShippingStatus.unshipped))
		{
			try {
				cancel(order, null);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
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
	public Order build(Member member, Product product, Integer quantity, Cart cart, Receiver receiver, String memo, Long promotionId, Order.ShippingMethod shippingMethod,Dragon dragon) {

//		Assert.notNull(cart);
//		Assert.notNull(cart.getMember());
//		Assert.notEmpty(cart.getCartItems());

		Order order = new Order();
		order.setPaymentStatus(Order.PaymentStatus.unpaid);
		order.setShippingStatus(Order.ShippingStatus.unshipped);
		order.setFee(new BigDecimal(0));
		order.setCouponDiscount(new BigDecimal(0));
		order.setExchangeDiscount(BigDecimal.ZERO);
		order.setOffsetAmount(new BigDecimal(0));
		order.setPoint(0L);
		order.setPointDiscount(BigDecimal.ZERO);
		order.setDeleted(false);
		order.setMemo(memo);
		order.setMember(member);
		order.setPaymentMethod(Order.PaymentMethod.online);
		order.setIsAllocatedStock(false);
		order.setIsDistribution(false);
		order.setRebateAmount(BigDecimal.ZERO);
		order.setIsPartner(false);
		order.setPartnerAmount(BigDecimal.ZERO);

		if (shippingMethod==null) {
			shippingMethod = Order.ShippingMethod.shipping;
		}

		order.setShippingMethod(shippingMethod);

		if (receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
			order.setLocation(receiver.getLocation());
			order.setReceiverId(receiver.getId());
		}

		List<OrderItem> orderItems = order.getOrderItems();

		if (product != null) {

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
			orderItem.setCouponQuantity(0L);

			orderItems.add(orderItem);

			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			if (bundle.containsKey("weex") && bundle.getString("weex").equals("3")) {
                Card card = member.getCards().get(0);
                order.setSeller(card.getOwner());
			} else {
				order.setSeller(product.getMember());
			}

			if (promotionId!=null) {
				Promotion promotion = promotionDao.find(promotionId);
				if (promotion!=null) {
					Integer qt = promotion.calc(orderItem.getSubtotal(),orderItem.getQuantity());
					if (qt>0) {
						Product gift = promotion.getGift();
						OrderItem giftItem = new OrderItem();
						giftItem.setName(gift.getName());
						giftItem.setSpec(gift.getSpec());
						giftItem.setPrice(BigDecimal.ZERO);
						giftItem.setWeight(gift.getWeight());
						giftItem.setThumbnail(gift.getThumbnail());
						giftItem.setIsGift(true);
						giftItem.setQuantity(qt);
						giftItem.setShippedQuantity(0);
						giftItem.setReturnQuantity(0);
						giftItem.setCouponQuantity(0L);
						giftItem.setProduct(gift);
						giftItem.setPromotion(promotion);
						giftItem.setOrder(order);
						orderItems.add(giftItem);
					}
				}
			}
		} else {
			if (cart!=null) {
				for (CartItem cartItem : cart.getCartItems()) {
					if (cartItem != null && cartItem.getProduct() != null) {
						Product cartProduct = cartItem.getProduct();
						OrderItem orderItem = new OrderItem();
						orderItem.setName(cartProduct.getName());
						orderItem.setSpec(cartProduct.getSpec());
						orderItem.setPrice(cartItem.getEffectivePrice());
						orderItem.setWeight(cartProduct.getWeight());
						orderItem.setThumbnail(cartProduct.getThumbnail());
						orderItem.setIsGift(false);
						orderItem.setQuantity(cartItem.getQuantity());
						orderItem.setShippedQuantity(0);
						orderItem.setReturnQuantity(0);
						orderItem.setProduct(cartProduct);
						orderItem.setOrder(order);
						orderItem.setCouponQuantity(0L);
						orderItems.add(orderItem);


						ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
						if (bundle.containsKey("weex") && bundle.getString("weex").equals("3")) {
							Card card = member.getCards().get(0);
							order.setSeller(card.getOwner());
						} else {
							order.setSeller(cartItem.getSeller());
						}


						Promotion promotion = cartItem.getPromotion();
						if (promotion != null) {
							Integer qt = promotion.calc(orderItem.getSubtotal(), orderItem.getQuantity());
							if (qt > 0) {
								Product gift = promotion.getGift();
								OrderItem giftItem = new OrderItem();
								giftItem.setName(gift.getName());
								giftItem.setSpec(gift.getSpec());
								giftItem.setPrice(BigDecimal.ZERO);
								giftItem.setWeight(gift.getWeight());
								giftItem.setThumbnail(gift.getThumbnail());
								giftItem.setIsGift(true);
								giftItem.setQuantity(qt);
								giftItem.setShippedQuantity(0);
								giftItem.setReturnQuantity(0);
								giftItem.setCouponQuantity(0L);
								giftItem.setProduct(gift);
								giftItem.setPromotion(promotion);
								giftItem.setOrder(order);
								orderItems.add(giftItem);
							}
						}

					}
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

		if (member != null && !order.getShippingMethod().equals(Order.ShippingMethod.cardbkg)) {
			List<CouponCode> couponCodes = member.getCouponCodes();
			BigDecimal discount = BigDecimal.ZERO;
			for (CouponCode code : couponCodes) {
				if (    code.getCoupon().getDistributor().equals(order.getSeller())
						&& code.getCoupon().getType().equals(Coupon.Type.exchange)
						&& code.getEnabled()
						&& !code.getCoupon().getScope().equals(Coupon.Scope.shop)
						) {
					BigDecimal d = code.calculate(order.getPrice(),order);
					discount = discount.add(d);

				}
			}
			if (discount.compareTo(BigDecimal.ZERO) > 0) {
				order.setExchangeDiscount(discount);
			}
		}

		if (member != null && (order.getExchangeDiscount().compareTo(BigDecimal.ZERO)==0)) {
			List<CouponCode> couponCodes = member.getCouponCodes();
			BigDecimal discount = BigDecimal.ZERO;
			for (CouponCode code : couponCodes) {
				if (    code.getCoupon().getDistributor().equals(order.getSeller())
						&& !code.getCoupon().getType().equals(Coupon.Type.exchange)
						&& code.getEnabled()
						&& !code.getCoupon().getScope().equals(Coupon.Scope.shop)
				) {
					BigDecimal d = code.calculate(order.getPrice(),order);
					if (d.compareTo(discount) > 0) {
						order.setCouponDiscount(d);
						order.setCouponCode(code);
					}
				}
			}
		}

		//按楼层加价
		if (!order.getShippingMethod().equals(Order.ShippingMethod.cardbkg)) {
			order.setFreight(order.calcFreight(receiver));
		} else {
			order.setFreight(BigDecimal.ZERO);
		}

		order.setAmountPaid(new BigDecimal(0));

		order.setOrderStatus(Order.OrderStatus.unconfirmed);
		order.setPaymentStatus(Order.PaymentStatus.unpaid);

		//30分钟不付款过期
		order.setExpire(DateUtils.addMinutes(new Date(), 30));

		return order;

	}

	/**
	 *
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
	 *
	 */
	public Order create(Member member, Product product, Integer quantity, Cart cart, Receiver receiver, String memo, Long xuid, Admin operator, Long promotionId, Order.ShippingMethod shippingMethod,Dragon dragon,Date hopeDate) {

//		Assert.notNull(cart);
//		Assert.notNull(cart.getMember());
//		Assert.notEmpty(cart.getCartItems());

		Assert.notNull(receiver);

		Order order = build(member, product, quantity, cart, receiver, memo,promotionId,shippingMethod,dragon);

		order.setSn(snDao.generate(Sn.Type.order));

		order.setDragon(dragon);

		order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
		order.setOperator(member.userId());

		order.setHopeDate(hopeDate);

		if (order.getCouponCode() != null) {
			CouponCode couponCode = order.getCouponCode();
			couponCode.setUsedDate(new Date());
			couponCode.setStock(couponCode.getStock()-1);
			couponCode.setIsUsed(couponCode.getStock()<=0);
			couponCodeDao.merge(couponCode);
		}

		for (OrderItem orderItem:order.getOrderItems()) {
			if (orderItem.getCouponCode() != null) {
				CouponCode couponCode = orderItem.getCouponCode();
				couponCode.setUsedDate(new Date());
				couponCode.setStock(couponCode.getStock()-orderItem.getCouponQuantity());
				couponCode.setIsUsed(couponCode.getStock()<=0);
				couponCodeDao.merge(couponCode);
			}
		}

		order.setIsAllocatedStock(true);
		order.setDeleted(false);

		order.setFee(BigDecimal.ZERO);

		Card card = member.card(order.getSeller());
		if (card != null) {
			Long point = order.getAmount().setScale(0, BigDecimal.ROUND_DOWN).longValue();
			if (card.getPoint() >= point) {
				order.setPointDiscount(new BigDecimal(point));
			} else {
				order.setPointDiscount(new BigDecimal(card.getPoint()));
			}
		}

		//接龙订单,业绩归接龙人
		if (dragon!=null && !dragon.getMember().equals(order.getSeller())) {
            order.setPromoter(dragon.getMember());
		} else
		//股东自已消费，直接获取返利，不给再分配
		if (dragon==null && card!=null && card.getType().equals(Card.Type.partner)) {
			order.setPromoter(null);
			order.setPartner(member);
		} else {
			//分配给原有上传,只在成为团队成员，才能保持订单分配
			if (card != null && card.getPromoter() != null && card.getPromoter().leaguer(order.getSeller())) {
				Member promoter = card.getPromoter();
				if (promoter != null && promoter.equals(order.getSeller())) {
					promoter = null;
				}
				order.setPromoter(promoter);
			} else if (xuid != null) {
				//新客户给推广人
				Member promoter = memberDao.find(xuid);
				if (promoter.leaguer(order.getSeller())) {
					if (promoter != null && promoter.equals(order.getSeller()) ) {
						promoter = null;
					}
					order.setPromoter(promoter);
				}
			}

			if (order.getPromoter() != null) {
				order.setPartner(member.partner(order.getSeller()));
			}

		}
		orderDao.persist(order);

		cardService.decPoint(card, order.getPointDiscount().longValue(), "订单支付", order);

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
				if (orderProduct != null) {
					productDao.lock(orderProduct, LockModeType.PESSIMISTIC_WRITE);
					orderProduct.setAllocatedStock(orderProduct.getAllocatedStock() + orderItem.getQuantity());
					productDao.merge(orderProduct);
					orderDao.flush();
				}
			}
		}

		messageService.orderMemberPushTo(orderLog);
        //没有付款时，直接确定订单
		if (order.getAmountPayable().compareTo(BigDecimal.ZERO)==0) {
			orderDao.flush();
			try {
				Payment payment = payment(order,null);
				if (payment!=null) {
					payment.setTranSn(payment.getSn());
					payment.setMethod(Payment.Method.offline);
					payment.setPaymentPluginId("cashPayPlugin");
					payment.setPaymentMethod("电子券结算");
					paymentService.update(payment);

					paymentService.handle(payment);
					order.setPaymentMethod(Order.PaymentMethod.offline);
					orderDao.merge(order);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}

		} else {
			//判断月结客户
			if (card != null) {
				if (card.getPaymentMethod().equals(Card.PaymentMethod.monthly)) {
					orderDao.flush();
					try {
						Payment payment = payment(order, null);
						if (payment != null) {
							payment.setTranSn(payment.getSn());
							payment.setMethod(Payment.Method.offline);
							payment.setPaymentPluginId("monthPayPlugin");
							payment.setPaymentMethod("月结付款");
							paymentService.update(payment);

							paymentService.handle(payment);

							order.setPaymentMethod(Order.PaymentMethod.offline);
							orderDao.merge(order);
						}
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
				}
			}
		}
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
		return;
	}

	/**
	 * 订单确认
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void confirm(Order order, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			throw new RuntimeException("不能确定");
		}

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
		return;
	}

	/**
	 * 计算返利
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	private void createRebate(Order order, Admin operator) throws Exception {
		//判断是会员
		//计算分润
		if (order.getPromoter() != null && order.getShippingStatus() == Order.ShippingStatus.shipped  && order.getPaymentStatus() == Order.PaymentStatus.paid) {
			BigDecimal d = order.getDistribution();
			if (d.compareTo(BigDecimal.ZERO) > 0) {
				//扣除商家分配佣金
				Member seller = order.getSeller();
				memberDao.refresh(seller, LockModeType.PESSIMISTIC_WRITE);
				BigDecimal bal = seller.getBalance().subtract(d);
				if (bal.compareTo(BigDecimal.ZERO) >= 0) {
					seller.setBalance(seller.getBalance().subtract(d));
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
					deposit.setSeller(order.getSeller());
					deposit.setTrade(null);
					depositDao.persist(deposit);
					messageService.depositPushTo(deposit);

					order.setRebateAmount(d);
					order.setIsDistribution(true);
					orderDao.merge(order);
				} else {
					order.setRebateAmount(d);
					order.setIsDistribution(false);
					orderDao.merge(order);
				}
			}
			if (order.getIsDistribution()) {
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
							d1.setSeller(order.getSeller());
							d1.setTrade(order.getSeller());
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
								if (p2 != null) {
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
							d2.setSeller(order.getSeller());
							d2.setTrade(order.getSeller());
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
								if (p3 != null) {
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
							d3.setSeller(order.getSeller());
							d3.setTrade(order.getSeller());
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

		}

		//计算股东分红
		if (order.getPartner() != null && order.getShippingStatus() == Order.ShippingStatus.shipped  && order.getPaymentStatus() == Order.PaymentStatus.paid) {
			BigDecimal partnerAmount = order.calcPartner();
			Card pcard = order.getPartner().card(order.getSeller());

			BigDecimal pt = partnerAmount.subtract(order.getDistribution());
			if (pcard!=null && pt.compareTo(BigDecimal.ZERO)>0) {

				BigDecimal pte = pt.multiply(pcard.getBonus().multiply(new BigDecimal("0.01"))).setScale(2,BigDecimal.ROUND_HALF_DOWN);

				Member seller = order.getSeller();
				memberDao.refresh(seller, LockModeType.PESSIMISTIC_WRITE);

				//扣除股东分红

				BigDecimal bal = seller.getBalance().subtract(pte);
				if (bal.compareTo(BigDecimal.ZERO)<0) {
					seller.setBalance(seller.getBalance().subtract(pte));
					memberDao.merge(seller);
					memberDao.flush();
					Deposit deposit = new Deposit();
					deposit.setBalance(seller.getBalance());
					deposit.setType(Deposit.Type.product);
					deposit.setMemo("支付分红佣金");
					deposit.setMember(seller);
					deposit.setCredit(BigDecimal.ZERO.subtract(pte));
					deposit.setDebit(BigDecimal.ZERO);
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setOrder(order);
					deposit.setSeller(order.getSeller());
					deposit.setTrade(order.getPartner());
					depositDao.persist(deposit);
					messageService.depositPushTo(deposit);

					Member partner = order.getPartner();
					memberDao.refresh(partner, LockModeType.PESSIMISTIC_WRITE);

					//股东分红
					partner.setBalance(partner.getBalance().add(pte));
					memberDao.merge(partner);
					memberDao.flush();
					Deposit deposit_partner = new Deposit();
					deposit_partner.setBalance(seller.getBalance());
					deposit_partner.setType(Deposit.Type.rebate);
					deposit_partner.setMemo("分红佣金");
					deposit_partner.setMember(partner);
					deposit_partner.setCredit(pte);
					deposit_partner.setDebit(BigDecimal.ZERO);
					deposit_partner.setDeleted(false);
					deposit_partner.setOperator("system");
					deposit_partner.setOrder(order);
					deposit_partner.setSeller(order.getSeller());
					deposit_partner.setTrade(order.getSeller());
					depositDao.persist(deposit_partner);
					messageService.depositPushTo(deposit_partner);

					order.setIsPartner(true);
					orderDao.merge(order);

				} else {
					order.setIsPartner(false);
					orderDao.merge(order);

				}

			}

		}

		//计算公球公排
		if (order.getShippingStatus() == Order.ShippingStatus.shipped  && order.getPaymentStatus() == Order.PaymentStatus.paid) {
			orderRankingService.add(order);
		}


	}

		/**
         * 订单完成
         *
         * @param order
         *            订单
         * @param operator
         *            操作员
         */
	public void complete(Order order, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			throw new RuntimeException("不能完成");
		}

		Member member = order.getMember();
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (order.refundOrReturn()) {

			CouponCode couponCode = order.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(false);
				couponCode.setUsedDate(null);
				couponCode.setStock(couponCode.getStock()+1);
				couponCodeDao.merge(couponCode);

				order.setCouponCode(null);
				orderDao.merge(order);
			}


			for (OrderItem orderItem:order.getOrderItems()) {
				if (orderItem.getCouponCode() != null) {
					CouponCode couponCode1 = orderItem.getCouponCode();
					couponCode1.setIsUsed(false);
					couponCode1.setUsedDate(null);
					couponCode1.setStock(couponCode1.getStock()+orderItem.getCouponQuantity());
					couponCodeDao.merge(couponCode1);

					orderItem.setCouponCode(null);
					orderItem.setCouponQuantity(0L);
					orderItemDao.merge(orderItem);
				}
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
		orderDao.flush();

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setContent("订单交易完成");
		orderLog.setOrder(order);
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);

		//计算货款
		if (order.getPaymentStatus().equals(Order.PaymentStatus.paid) && order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
			if (order.getPaymentMethod().equals(Order.PaymentMethod.online) || order.getPaymentMethod().equals(Order.PaymentMethod.deposit)) {
				//扣除商家分配佣金
				Member seller = order.getSeller();
				memberDao.refresh(seller, LockModeType.PESSIMISTIC_WRITE);

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
				deposit.setMemo("货款结算(手续费:" + nf.format(order.getFee()) + ")");
				deposit.setMember(seller);
				deposit.setCredit(samt);
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setOrder(order);
				deposit.setSeller(order.getSeller());
				deposit.setTrade(order.getMember());
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);
			}
		}

		//建立分佣关系
		memberService.create(order.getMember(), order.getSeller());
		Card card = cardService.createAndActivate(order.getMember(), order.getSeller(), order.getPromoter(), order.getAmount(), order.getDistPrice());
		if (card != null) {
			memberService.create(order.getMember(), order.getPromoter());
		}

		try {

			//分享人不为空时，关联代理商和生成平台发展人关系
			if (order.getPromoter()!=null) {
				rebateService.link(order.getMember(),order.getPromoter());
			}

			order.setPersonal(order.getPromoter().getPersonal());

			order.setAgent(order.getPromoter().getAgent());

			order.setOperate(order.getPromoter().getOperate());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		//没有发货，或是退货等状态完成，是无效订单
		if (order.refundOrReturn()) {
			card = order.getMember().card(order.getSeller());
			if (card != null && order.getPointDiscount().compareTo(BigDecimal.ZERO) > 0) {
				cardService.addPoint(card, order.getPointDiscount().longValue(), "订单退货", order);
			}
		} else {
			memberService.addAmount(member, order.getAmount());
		}

		//分销结算

		if (order.getPaymentStatus().equals(Order.PaymentStatus.paid) && order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
			createRebate(order, operator);
		}

		//代理商佣金
//		rebateService.rebate(order.getFee(),order.getMember(),order.getPersonal(),order.getAgent(),order.getOperate(),order);

		//放入卡包
		if (order.getShippingMethod().equals(Order.ShippingMethod.cardbkg)) {
			for (OrderItem orderItem:order.getOrderItems()) {
				Coupon coupon = couponService.create(orderItem.getProduct(),order.getSeller());
				couponCodeService.build(coupon,order.getMember(),orderItem.getQuantity().longValue());
			}
		} else {
			for (OrderItem orderItem:order.getOrderItems()) {
				if (orderItem.getProduct().getType().equals(Product.Type.warehouse)) {
					Coupon coupon = couponService.create(orderItem.getProduct(), order.getSeller());
					couponCodeService.build(coupon, order.getMember(),0L);
				}
			}
		}

		return;

	}

		/**
		 * 订单取消
		 *
		 * @param order
		 *            订单
		 * @param operator
		 *            操作员
		 */

	public void cancel(Order order, Admin operator) throws Exception {
		Assert.notNull(order);
		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			throw new RuntimeException("不能关闭");
		}

		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			couponCode.setIsUsed(false);
			couponCode.setUsedDate(null);
			couponCode.setStock(couponCode.getStock()+1);
			couponCodeDao.merge(couponCode);

			order.setCouponCode(null);
			orderDao.merge(order);
		}



		for (OrderItem orderItem:order.getOrderItems()) {
			if (orderItem.getCouponCode() != null) {
				CouponCode couponCode1 = orderItem.getCouponCode();
				couponCode1.setIsUsed(false);
				couponCode1.setUsedDate(null);
				couponCode1.setStock(couponCode1.getStock()+orderItem.getCouponQuantity());
				couponCodeDao.merge(couponCode1);

				orderItem.setCouponCode(null);
				orderItem.setCouponQuantity(0L);
				orderItemDao.merge(orderItem);
			}
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
		if (card != null && order.getPointDiscount().compareTo(BigDecimal.ZERO) > 0) {
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
	public Payment payment(Order order, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			throw new RuntimeException("不能支付");
		}


		Payment payment = new Payment();

		if (order.getAmountPayable().compareTo(BigDecimal.ZERO)!=0) {
			Card card = order.getMember().card(order.getSeller());
			order.setFee(BigDecimal.ZERO);
			if (card != null) {
				if (card.getBalance().compareTo(order.getAmountPayable()) >= 0) {
					payment.setPaymentPluginId("cardPayPlugin");
				}
			}
			if (payment.getPaymentPluginId() == null) {
				Member member = order.getMember();
				if (member.getBalance().compareTo(order.getAmountPayable()) >= 0) {
					payment.setPaymentPluginId("balancePayPlugin");
				}
			}
			if (payment.getPaymentPluginId() == null) {
				Topic topic = order.getSeller().getTopic();
				if (topic == null) {
					order.setFee(order.getAmountPayable().multiply(new BigDecimal("0.006")).setScale(2, BigDecimal.ROUND_UP));
				} else {
					order.setFee(topic.calcFee(order.getAmountPayable()));
				}
			}
			orderDao.merge(order);
		} else {
			order.setFee(BigDecimal.ZERO);
			payment.setPaymentPluginId("cashPayPlugin");
			payment.setPaymentMethod("现金");
		}

		payment.setPayee(order.getSeller());
		payment.setMember(order.getMember());
		payment.setStatus(Payment.Status.waiting);
		payment.setMethod(Payment.Method.online);
		payment.setType(Payment.Type.payment);
		payment.setMemo("订单付款");
		payment.setAmount(order.getAmountPayable());
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setOrder(order);
		payment.setWay(Payment.Way.yundian);

		paymentService.save(payment);

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
	public void shipping(Order order, Order.ShippingMethod shippingMethod,String trackingNo, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
			throw new RuntimeException("不能发货");
		}

		for (OrderItem orderItem : order.getOrderItems()) {
			orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
			orderItem.setShippedQuantity(orderItem.getQuantity());
		}

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					if (product != null) {
						productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
						product.setStock(product.getStock() - orderItem.getQuantity());
						product.setAllocatedStock(product.getAllocatedStock() - orderItem.getQuantity());
						productDao.merge(product);
						orderDao.flush();
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		if (shippingMethod!=null) {
			order.setShippingMethod(shippingMethod);
		}
		order.setTrackingNo(trackingNo);
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

		if (!order.getShippingMethod().equals(Order.ShippingMethod.cardbkg)) {
		  //对同城配送商品，生成配送单
			shippingService.create(order);
		}

		return;

	}

	public void refunds(Order order, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.refresh(order, LockModeType.PESSIMISTIC_WRITE);

		if (!order.getPaymentStatus().equals(Order.PaymentStatus.paid)) {
			throw new RuntimeException("不能重复操作");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
			throw new RuntimeException("已发货不能退款");
		}

//		order.setAmountPaid(order.getAmountPaid());
		order.setExpire(null);
		order.setPaymentStatus(Order.PaymentStatus.refunding);
		orderDao.merge(order);

		for (Payment payment : order.getPayments()) {
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
		if (operator == null) {
			orderLog.setContent("买家申请退款");
		} else {
			orderLog.setContent("卖家确定退款");
		}
		orderLogDao.persist(orderLog);
		if (operator == null) {
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

	public void returns(Order order, Admin operator) throws Exception {
		Assert.notNull(order);

		orderDao.refresh(order, LockModeType.PESSIMISTIC_WRITE);

		if (operator == null) {
			if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped)) {
				throw new RuntimeException("不在发货状态");
			}
		} else {
			if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped) && !order.getShippingStatus().equals(Order.ShippingStatus.returning)) {
				throw new RuntimeException("不在发货状态");
			}
		}

		if (operator != null) {

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
							product.setStock(product.getStock() + orderItem.getQuantity());
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

//				order.setAmountPaid(order.getAmountPaid());
				order.setExpire(null);
				order.setPaymentStatus(Order.PaymentStatus.refunding);
				orderDao.merge(order);

				for (Payment payment : order.getPayments()) {
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
				complete(order, operator);
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

	public void evictCompleted(Long orderId) {
		Order order = orderDao.find(orderId,LockModeType.PESSIMISTIC_WRITE);
		if (order.getOrderStatus().equals(Order.OrderStatus.confirmed)
				&&
			order.getShippingStatus().equals(Order.ShippingStatus.shipped))
		{
			try {
				complete(order, null);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

	}

	public List<OrderSummary> summary(Member member, Date beginDate, Date endDate, Pageable pageable) {
	    return orderDao.summary(member,beginDate,endDate,pageable);
	}



	}