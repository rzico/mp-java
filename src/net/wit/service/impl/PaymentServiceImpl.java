package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.dao.*;
import net.wit.entity.Order;
import net.wit.entity.summary.OrderSummary;
import net.wit.entity.summary.PaymentSummary;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;

/**
 * @ClassName: PaymentDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "smssendServiceImpl")
	private SmssendService smssendService;

	@Resource(name = "cardServiceImpl")
	private CardService cardService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "payBillDaoImpl")
	private PayBillDao payBillDao;

	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "cardDaoImpl")
	private CardDao cardDao;

	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;

	@Resource(name = "cardBillDaoImpl")
	private CardBillDao cardBillDao;

	@Resource(name = "topicBillDaoImpl")
	private TopicBillDao topicBillDao;

	@Resource(name = "topicDaoImpl")
	private TopicDao topicDao;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "articleRewardDaoImpl")
	private ArticleRewardDao articleRewardDao;

	@Resource(name = "receiverDaoImpl")
	private ReceiverDao receiverDao;

	@Resource(name = "evaluationDaoImpl")
	private EvaluationDao evaluationDao;

	@Resource(name = "rebateServiceImpl")
	private RebateService rebateService;

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}

	// 自定义比较器：按书的价格排序
	static class AmountComparator implements Comparator {
		public int compare(Object object1, Object object2) {// 实现接口中的方法
			Map<String, Object> p1 = (Map<String, Object>) object1; // 强制转换
			Map<String, Object> p2 = (Map<String, Object>) object2;
			return new BigDecimal(p1.get("amount").toString()).compareTo(new BigDecimal(p2.get("amount").toString()));
		}
	}


	private Card.VIP calculateVip(Member owner, BigDecimal amount) {
		Topic topic = owner.getTopic();

		if (topic==null) {
			return null;
		}

		if (topic.getTopicCard()==null) {
			return null;
		}

		if (topic.getTopicCard().getActivity()==null) {
			return null;
		}

		List<Map<String, Object>> activitys = JsonUtils.toObject(topic.getTopicCard().getActivity(),List.class);
		Collections.sort(activitys, new AmountComparator());

		Map<String, Object> curr = null;
		for (Map<String, Object> model:activitys) {
			if (new BigDecimal(model.get("amount").toString()).compareTo(amount)>0) {
				break;
			} else {
				curr = model;
			}
		}

		if (curr!=null) {
			return Card.VIP.valueOf(curr.get("vip").toString().toLowerCase());
		} else {
			return null;
		}
	}

	@Transactional
	public synchronized void handle(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus().equals(Payment.Status.waiting)) {
			payment.setPaymentDate(new Date());
			payment.setStatus(Payment.Status.success);
			paymentDao.merge(payment);
			paymentDao.flush();

			//处理支付结果
			if (payment.getType() == Payment.Type.payment) {
				Order order = payment.getOrder();
				orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

				payment.setOrder(order);
				paymentDao.merge(payment);

				order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
				if (payment.getMethod().equals(Payment.Method.offline)) {
					order.setFee(BigDecimal.ZERO);
				} else
				if (payment.getMethod().equals(Payment.Method.card)) {
					order.setFee(BigDecimal.ZERO);
				}

				order.setWay(payment.getWay());

				order.setPaymentMethod(Order.PaymentMethod.values()[payment.getMethod().ordinal()]);
				order.setPaymentPluginId(payment.getPaymentPluginId());
				order.setPaymentPluginName(payment.getPaymentMethod());

				order.setExpire(null);
				order.setOrderStatus(Order.OrderStatus.confirmed);
				order.setPaymentStatus(Order.PaymentStatus.paid);
				orderDao.merge(order);

				OrderLog orderLog = new OrderLog();
				orderLog.setType(OrderLog.Type.payment);
				orderLog.setOperator(payment.getMember().userId());
				orderLog.setContent("买家付款成功");
				orderLog.setOrder(order);
				orderLogDao.persist(orderLog);

				if (payment.getMethod().equals(Payment.Method.online)) {
					Member member = payment.getMember();
					memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
					Deposit deposit = new Deposit();
					deposit.setBalance(member.getBalance());
					deposit.setType(Deposit.Type.payment);
					deposit.setMemo(payment.getMemo());
					deposit.setMember(member);
					deposit.setCredit(BigDecimal.ZERO);
					deposit.setDebit(payment.getAmount());
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setPayment(payment);
					deposit.setOrder(order);
					deposit.setSeller(order.getSeller());
					deposit.setTrade(order.getSeller());
					depositDao.persist(deposit);
				}

				//新客户不自动配送
				ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

				//卡包
				if (order.getShippingMethod().equals(Order.ShippingMethod.cardbkg)) {
					orderService.shipping(order,Order.ShippingMethod.cardbkg,null,null,null,null);
					orderService.complete(order,null);
				} else {

					if (!payment.getMethod().equals(Payment.Method.offline)) {
						messageService.orderMemberPushTo(orderLog);
					    messageService.orderSellerPushTo(orderLog);
				    }

					if (bundle.containsKey("weex") && "3".equals(bundle.getString("weex"))) {
						Receiver receiver = receiverDao.find(order.getReceiverId());
						if (receiver != null && receiver.getShop() != null) {
							orderService.shipping(order, Order.ShippingMethod.warehouse, null, null, null, null);
						}
					}

				}

			} else
			if (payment.getType() == Payment.Type.cashier) {
				PayBill payBill = payment.getPayBill();
				if (payment.getMethod().equals(Payment.Method.offline) || payment.getMethod().equals(Payment.Method.card)) {
					payBill.setFee(BigDecimal.ZERO);
					payBill.setMethod(PayBill.Method.offline);
					//线下或会员卡支付，不需要给商家结算
				} else {
					Member member = payment.getPayee();
					memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
					BigDecimal settle = payBill.getSettleAmount();
					if (settle.compareTo(BigDecimal.ZERO) > 0) {
						member.setBalance(member.getBalance().add(settle));
						memberDao.merge(member);
						memberDao.flush();
						Deposit deposit = new Deposit();
						deposit.setBalance(member.getBalance());
						deposit.setType(Deposit.Type.cashier);
						deposit.setMemo(payment.getMemo());
						deposit.setMember(member);
						deposit.setCredit(settle);
						deposit.setDebit(BigDecimal.ZERO);
						deposit.setDeleted(false);
						deposit.setOperator("system");
						deposit.setPayment(payment);
						deposit.setPayBill(payBill);
						deposit.setSeller(payBill.getOwner());
						deposit.setTrade(payBill.getOwner());
						depositDao.persist(deposit);
						messageService.depositPushTo(deposit);
					}
					payBill.setMethod(PayBill.Method.online);
				}
				payBill.setPaymentPluginId(payment.getPaymentPluginId());
				payBill.setPaymentPluginName(payment.getPaymentMethod());

				payBill.setMember(payment.getMember());
				payBill.setStatus(PayBill.Status.success);
				payBillDao.merge(payBill);
				if (payBill.getCouponCode()!=null) {
					CouponCode couponCode = payBill.getCouponCode();
					couponCode.setIsUsed(true);
					couponCodeDao.merge(couponCode);
				}
				messageService.payBillPushTo(payBill);

				if (payment.getMethod().equals(Payment.Method.online)) {
					Member member = payment.getMember();
					memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
					Deposit deposit = new Deposit();
					deposit.setBalance(member.getBalance());
					deposit.setType(Deposit.Type.payment);
					deposit.setMemo(payment.getMemo());
					deposit.setMember(member);
					deposit.setCredit(BigDecimal.ZERO);
					deposit.setDebit(payment.getAmount());
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setPayment(payment);
					deposit.setPayBill(payBill);
					deposit.setSeller(payBill.getOwner());
					deposit.setTrade(payBill.getOwner());
					depositDao.persist(deposit);
				}
//				cardService.createAndActivate(payBill.getMember(),payBill.getOwner(),null,payBill.getAmount(),BigDecimal.ZERO);
			}else
			if (payment.getType() == Payment.Type.cardFill) {
				PayBill payBill = payment.getPayBill();
				if (payment.getMethod().equals(Payment.Method.offline) || payment.getMethod().equals(Payment.Method.card)) {
					payBill.setFee(BigDecimal.ZERO);
					payBill.setMethod(PayBill.Method.offline);
					//线下或会员卡支付，不需要给商家结算
				} else {
					Member member = payment.getPayee();
					memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
					BigDecimal settle = payBill.getSettleAmount();
					if (settle.compareTo(BigDecimal.ZERO) > 0) {
						member.setBalance(member.getBalance().add(settle));
						memberDao.merge(member);
						memberDao.flush();
						Deposit deposit = new Deposit();
						deposit.setBalance(member.getBalance());
						deposit.setType(Deposit.Type.card);
						deposit.setMemo(payment.getMemo());
						deposit.setMember(member);
						deposit.setCredit(settle);
						deposit.setDebit(BigDecimal.ZERO);
						deposit.setDeleted(false);
						deposit.setOperator("system");
						deposit.setPayment(payment);
						deposit.setPayBill(payBill);
						deposit.setSeller(payBill.getOwner());
						deposit.setTrade(payBill.getOwner());
						depositDao.persist(deposit);
						messageService.depositPushTo(deposit);
					}
					payBill.setMethod(PayBill.Method.online);
				}
				payBill.setPaymentPluginId(payment.getPaymentPluginId());
				payBill.setPaymentPluginName(payment.getPaymentMethod());

				payBill.setMember(payment.getMember());
				payBill.setStatus(PayBill.Status.success);
				payBillDao.merge(payBill);
				if (payBill.getType().equals(PayBill.Type.card)) {
					Card card = payBill.getCard();
					cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);

					Card.VIP vip = calculateVip(payBill.getOwner(),payBill.getAmount());
                    if (vip!=null) {
                    	if (card.getVip().ordinal()<vip.ordinal()) {
                    		card.setVip(vip);
						}
					}
					card.setBalance(card.getBalance().add(payBill.getCardAmount()));
					cardDao.merge(card);
					cardDao.flush();

					CardBill cardBill = new CardBill();
					cardBill.setDeleted(false);
					cardBill.setOwner(payBill.getOwner());
					cardBill.setShop(payBill.getShop());
					cardBill.setCredit(payBill.getCardAmount());
					cardBill.setDebit(BigDecimal.ZERO);
					if (payBill.getAdmin()!=null) {
						cardBill.setMemo("充值，操作员:"+payBill.getAdmin().getName());
						cardBill.setOperator(payBill.getAdmin().getName());
					} else {
						cardBill.setMemo("自助充值");
						cardBill.setOperator(payBill.getCard().getName());
					}
					if (payment.getMethod().equals(Payment.Method.offline)) {
						cardBill.setMethod(CardBill.Method.offline);
					} else {
						cardBill.setMethod(CardBill.Method.online);
					}
					cardBill.setMember(card.getMembers().get(0));
					cardBill.setCard(card);
					cardBill.setType(CardBill.Type.recharge);
					cardBill.setBalance(card.getBalance());
					cardBillDao.persist(cardBill);
                    if (card.getMobile()!=null && card.getMobile().length()==11) {
                    	String content = "";
						DecimalFormat df=(DecimalFormat) NumberFormat.getInstance();
						df.setMaximumFractionDigits(2);
						if (payBill.getAmount().compareTo(payBill.getCardAmount())==0) {
                    		content = card.getTopicCard().getTopic().getName() + ",会员卡充值"+df.format(payBill.getAmount())+"元,余额:"+df.format(card.getBalance());
						} else {
							content = card.getTopicCard().getTopic().getName() + ",会员卡充值"+df.format(payBill.getAmount())+"送"+df.format(payBill.getCardAmount().subtract(payBill.getAmount()))+"元,余额:"+df.format(card.getBalance());
						}
						smssendService.send(payBill.getOwner(), card.getMobile(),content);
					}
				}
				messageService.payBillPushTo(payBill);

				if (payment.getMethod().equals(Payment.Method.online)) {
					Member member = payment.getMember();
					memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
					Deposit deposit = new Deposit();
					deposit.setBalance(member.getBalance());
					deposit.setType(Deposit.Type.payment);
					deposit.setMemo(payment.getMemo());
					deposit.setMember(member);
					deposit.setCredit(BigDecimal.ZERO);
					deposit.setDebit(payment.getAmount());
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setPayment(payment);
					deposit.setPayBill(payBill);
					deposit.setSeller(payBill.getOwner());
					deposit.setTrade(payBill.getOwner());
					depositDao.persist(deposit);
				}

//				cardService.createAndActivate(payBill.getMember(),payBill.getOwner(),null,payBill.getAmount(),BigDecimal.ZERO);
			} else
			if (payment.getType() == Payment.Type.reward) {
				ArticleReward reward = payment.getArticleReward();
				Member author = reward.getAuthor();
				memberDao.refresh(author,LockModeType.PESSIMISTIC_WRITE);
				author.setBalance(author.getBalance().add(reward.getAmount().subtract(reward.getFee())));
				memberDao.merge(author);
				memberDao.flush();

				Deposit deposit = new Deposit();
				deposit.setBalance(author.getBalance());
				deposit.setType(Deposit.Type.reward);
				deposit.setMemo(payment.getMemo());
				deposit.setMember(author);
				deposit.setCredit(reward.getAmount().subtract(reward.getFee()));
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setPayment(payment);
				deposit.setSeller(author);
				deposit.setTrade(reward.getMember());
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);
				reward.setStatus(ArticleReward.Status.success);
				articleRewardDao.merge(reward);
				messageService.rewardPushTo(reward);

				if (payment.getMethod().equals(Payment.Method.online)) {
					Member member = payment.getMember();
					memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
					Deposit memberDeposit = new Deposit();
					memberDeposit.setBalance(member.getBalance());
					memberDeposit.setType(Deposit.Type.payment);
					memberDeposit.setMemo(payment.getMemo());
					memberDeposit.setMember(member);
					memberDeposit.setCredit(BigDecimal.ZERO);
					memberDeposit.setDebit(payment.getAmount());
					memberDeposit.setDeleted(false);
					memberDeposit.setOperator("system");
					memberDeposit.setPayment(payment);
					memberDeposit.setOrder(null);
					memberDeposit.setSeller(author);
					memberDeposit.setTrade(author);
					depositDao.persist(memberDeposit);
				}
			} else
			if (payment.getType() == Payment.Type.recharge) {
				Member payee = payment.getPayee();
				memberDao.refresh(payee,LockModeType.PESSIMISTIC_WRITE);
				payee.setBalance(payee.getBalance().add(payment.getAmount()));
				memberDao.merge(payee);
				memberDao.flush();
				Deposit deposit = new Deposit();
				deposit.setBalance(payee.getBalance());
				deposit.setType(Deposit.Type.recharge);
				deposit.setMemo(payment.getMemo());
				deposit.setMember(payee);
				deposit.setCredit(payment.getAmount());
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setPayment(payment);
				deposit.setSeller(payment.getPayee());
				deposit.setTrade(payment.getPayee());
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);

				if (payment.getMethod().equals(Payment.Method.online)) {
					Member member = payment.getMember();
					memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
					Deposit memberDeposit = new Deposit();
					memberDeposit.setBalance(member.getBalance());
					memberDeposit.setType(Deposit.Type.payment);
					memberDeposit.setMemo(payment.getMemo());
					memberDeposit.setMember(member);
					memberDeposit.setCredit(BigDecimal.ZERO);
					memberDeposit.setDebit(payment.getAmount());
					memberDeposit.setDeleted(false);
					memberDeposit.setOperator("system");
					memberDeposit.setPayment(payment);
					memberDeposit.setSeller(payment.getPayee());
					memberDeposit.setTrade(payment.getPayee());
					depositDao.persist(memberDeposit);
				}

			} else
			if (payment.getType() == Payment.Type.topic) {
				TopicBill topicBill = payment.getTopicBill();
				topicBill.setStatus(TopicBill.Status.success);
				topicBillDao.merge(topicBill);
				Topic topic = topicBill.getTopic();
                topic.setStatus(Topic.Status.success);
				Calendar calendar   =   new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(calendar.YEAR, 1);
				topic.setExpire(calendar.getTime());
				topicDao.merge(topic);
				messageService.topicPushTo(topic);
				enterpriseService.create(topic);

				if (payment.getMethod().equals(Payment.Method.online)) {
					Member member = payment.getMember();
					memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
					Deposit memberDeposit = new Deposit();
					memberDeposit.setBalance(member.getBalance());
					memberDeposit.setType(Deposit.Type.payment);
					memberDeposit.setMemo(payment.getMemo());
					memberDeposit.setMember(member);
					memberDeposit.setCredit(BigDecimal.ZERO);
					memberDeposit.setDebit(payment.getAmount());
					memberDeposit.setDeleted(false);
					memberDeposit.setOperator("system");
					memberDeposit.setPayment(payment);
					memberDeposit.setSeller(payment.getPayee());
					memberDeposit.setTrade(null);
					depositDao.persist(memberDeposit);
				}
			}  else
			if (payment.getType() == Payment.Type.evaluation) {
				Evaluation evaluation = payment.getEvaluation();
				evaluation.setEvalStatus(Evaluation.EvalStatus.paid);
				evaluationDao.merge(evaluation);
				if (evaluation.getPromoter()!=null && evaluation.getRebate().compareTo(BigDecimal.ZERO)>0) {
					Member buyer = evaluation.getMember();
					rebateService.link(buyer,evaluation.getPromoter());

					evaluation.setPersonal(buyer.getPersonal());
					evaluation.setAgent(buyer.getAgent());
					evaluation.setOperate(buyer.getOperate());
					evaluationDao.merge(evaluation);

					Member member = evaluation.getPromoter();
					member.setBalance(member.getBalance().add(evaluation.getRebate()));
					memberDao.merge(member);
					memberDao.flush();

					Deposit deposit = new Deposit();
					deposit.setBalance(member.getBalance());
					deposit.setType(Deposit.Type.rebate);
					deposit.setMemo("推广奖励金");
					deposit.setMember(member);
					deposit.setCredit(evaluation.getRebate());
					deposit.setDebit(BigDecimal.ZERO);
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setPayment(payment);
					depositDao.persist(deposit);
					messageService.depositPushTo(deposit);
					rebateService.rebate(evaluation.getPrice(),buyer,evaluation.getPersonal(),evaluation.getAgent(),evaluation.getOperate(),null);
				}


			}
		}
	}



	@Transactional
	public synchronized void close(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus() == Payment.Status.waiting) {
			payment.setStatus(Payment.Status.failure);
			paymentDao.merge(payment);
			paymentDao.flush();
			if (payment.getType().equals(Payment.Type.cashier)) {
				PayBill payBill = payment.getPayBill();
				payBill.setStatus(PayBill.Status.failure);
				payBill.setPaymentPluginId(payment.getPaymentPluginId());
				payBillDao.merge(payBill);
				CouponCode couponCode = payBill.getCouponCode();
				if (couponCode!=null) {
					couponCode.setIsUsed(false);
					couponCode.setUsedDate(null);
					couponCodeDao.merge(couponCode);
				}
				if (payBill.getCardDiscount().compareTo(BigDecimal.ZERO)>0) {
					Card card = payBill.getCard();
					if (card != null) {
						cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
						card.setBalance(card.getBalance().add(payBill.getCardDiscount()));
						cardDao.merge(card);
						cardDao.flush();
						CardBill bill = new CardBill();
						bill.setBalance(card.getBalance());
						bill.setCard(card);
						bill.setCredit(BigDecimal.ZERO);
						bill.setDebit(BigDecimal.ZERO.subtract(payBill.getCardDiscount()));
						bill.setDeleted(false);
						bill.setType(CardBill.Type.consume);
						bill.setMember(payBill.getMember());
						bill.setPayBill(payBill);
						bill.setMethod(CardBill.Method.online);
						bill.setMemo("线下收款-取消");
						bill.setPayment(payment);
						bill.setOwner(payBill.getOwner());
						cardBillDao.persist(bill);
					}
				}
			} else
			if (payment.getType().equals(Payment.Type.cardFill)) {
				PayBill payBill = payment.getPayBill();
				payBill.setPaymentPluginId(payment.getPaymentPluginId());
				payBill.setStatus(PayBill.Status.failure);
				payBillDao.merge(payBill);
			} else
			if (payment.getType().equals(Payment.Type.reward)) {
				ArticleReward reward = payment.getArticleReward();
				reward.setStatus(ArticleReward.Status.failure);
				articleRewardDao.merge(reward);
			} else
			if (payment.getType().equals(Payment.Type.topic)) {
				TopicBill topicBill = payment.getTopicBill();
				topicBill.setStatus(TopicBill.Status.failure);
				topicBillDao.merge(topicBill);
			} else
			if (payment.getType().equals(Payment.Type.evaluation)) {
				Evaluation evaluation = payment.getEvaluation();
				evaluation.setEvalStatus(Evaluation.EvalStatus.cancelled);
				evaluationDao.merge(evaluation);
			}
		};
	}




	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Payment payment) {
		super.save(payment);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Payment update(Payment payment) {
		return super.update(payment);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Payment update(Payment payment, String... ignoreProperties) {
		return super.update(payment, ignoreProperties);
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
	public void delete(Payment payment) {
		super.delete(payment);
	}

	public Page<Payment> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return paymentDao.findPage(beginDate,endDate,pageable);
	}

	/**
	 * 查询状态
	 */
	public void query(Long id) {
		Payment payment = paymentDao.find(id,LockModeType.PESSIMISTIC_WRITE);
		if (payment.getStatus().equals(Payment.Status.waiting)) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			String resultCode = null;
			try {
				if (paymentPlugin == null) {
					resultCode = "0001";
				} else {
					resultCode = paymentPlugin.queryOrder(payment, null);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			switch (resultCode) {
				case "0000":
					try {
						this.handle(payment);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				case "0001":
					try {
						this.close(payment);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
			}
		}
	}


	public List<PaymentSummary> summary(Member member ,Date beginDate, Date endDate, Pageable pageable) {
		return paymentDao.summary(member,beginDate,endDate,pageable);
	}

	public List<PaymentSummary> summary_method(Member member ,Date beginDate, Date endDate, Pageable pageable) {
		return paymentDao.summary_method(member,beginDate,endDate,pageable);
	}

}