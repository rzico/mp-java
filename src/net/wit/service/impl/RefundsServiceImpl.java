package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.*;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;

/**
 * @ClassName: RefundsDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("refundsServiceImpl")
public class RefundsServiceImpl extends BaseServiceImpl<Refunds, Long> implements RefundsService {
	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;
	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "smssendServiceImpl")
	private SmssendService smssendService;

	@Resource(name = "payBillDaoImpl")
	private PayBillDao payBillDao;

	@Resource(name = "cardDaoImpl")
	private CardDao cardDao;

	@Resource(name = "cardBillDaoImpl")
	private CardBillDao cardBillDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "refundsDaoImpl")
	public void setBaseDao(RefundsDao refundsDao) {
		super.setBaseDao(refundsDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Refunds refunds) {
		super.save(refunds);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Refunds update(Refunds refunds) {
		return super.update(refunds);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Refunds update(Refunds refunds, String... ignoreProperties) {
		return super.update(refunds, ignoreProperties);
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
	public void delete(Refunds refunds) {
		super.delete(refunds);
	}

	@Transactional(readOnly = true)
	public Refunds findBySn(String sn) {
		return refundsDao.findBySn(sn);
	}

	public Page<Refunds> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return refundsDao.findPage(beginDate,endDate,pageable);
	}

	//退款成功
	@Transactional
	public synchronized void handle(Refunds refunds) throws Exception {
		refundsDao.refresh(refunds, LockModeType.PESSIMISTIC_WRITE);
		if (refunds.getStatus().equals(Refunds.Status.confirmed)) {
			refunds.setRefundsDate(new Date());
			refunds.setStatus(Refunds.Status.success);
			refundsDao.merge(refunds);
			refundsDao.flush();

			Payment payment = refunds.getPayment();
			if (payment!=null) {
				paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
				if (payment.getStatus().equals(Payment.Status.refund_waiting)) {
					payment.setStatus(Payment.Status.refund_success);
					paymentDao.merge(payment);
				}
				PayBill paymentBill = payment.getPayBill();
				if (paymentBill!=null) {
					paymentBill.setStatus(PayBill.Status.refund_success);
					payBillDao.merge(paymentBill);
				}
			}
			if (refunds.getType().equals(Refunds.Type.payment)) {
				Order order = refunds.getOrder();
				orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
				order.setPaymentStatus(Order.PaymentStatus.refunded);
				orderDao.merge(order);

				OrderLog orderLog = new OrderLog();
				orderLog.setType(OrderLog.Type.refunds);
				orderLog.setOperator(refunds.getMember().userId());
				orderLog.setContent("卖家已退款");
				orderLog.setOrder(order);
				orderLogDao.persist(orderLog);

				if (refunds.getMethod().equals(Refunds.Method.online)) {
					Deposit deposit = new Deposit();
					deposit.setBalance(payment.getMember().getBalance());
					deposit.setType(Deposit.Type.refunds);
					deposit.setMemo(payment.getMemo());
					deposit.setMember(payment.getMember());
					deposit.setCredit(refunds.getAmount());
					deposit.setDebit(BigDecimal.ZERO);
					deposit.setDeleted(false);
					deposit.setOperator("system");
					deposit.setPayment(payment);
					deposit.setOrder(order);
					depositDao.persist(deposit);
				}

				messageService.orderMemberPushTo(orderLog);

				orderService.complete(order,null);

			} else {
				PayBill payBill = refunds.getPayBill();
				if (payBill != null) {
					payBill.setStatus(PayBill.Status.refund_success);
					payBillDao.merge(payBill);
				}
				messageService.payBillPushTo(payBill);
			}
		}
	}

	//提交退款
	@Transactional
	public synchronized Boolean refunds(Refunds refunds, HttpServletRequest request) throws Exception {
			refundsDao.refresh(refunds, LockModeType.PESSIMISTIC_WRITE);
			if (refunds.getStatus().equals(Refunds.Status.waiting)) {
				refunds.setStatus(Refunds.Status.confirmed);
				refundsDao.merge(refunds);
				refundsDao.flush();
				Payment payment = refunds.getPayment();
				if (payment!=null) {
					paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
					if (payment.getStatus().equals(Payment.Status.success)) {
						payment.setStatus(Payment.Status.refund_waiting);
						paymentDao.merge(payment);
					} else {
						throw new RuntimeException("重复提交");
					}
				}
				if (refunds.getType().equals(Refunds.Type.cashier)) {
					PayBill payBill = refunds.getPayBill();
					Member member =  refunds.getPayee();
					memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
					if (refunds.getMethod().equals(Refunds.Method.offline) || refunds.getMethod().equals(Refunds.Method.card)) {
						payBill.setFee(BigDecimal.ZERO);
						//线下业务，本身没有结款
					} else {
						BigDecimal settle = payBill.getSettleAmount();
						if (settle.compareTo(BigDecimal.ZERO) != 0) {
							if (member.getBalance().add(settle).compareTo(BigDecimal.ZERO)<0) {
								throw new RuntimeException("余额不足");
							}
							member.setBalance(member.getBalance().add(settle));
							memberDao.merge(member);
							Deposit deposit = new Deposit();
							deposit.setBalance(member.getBalance());
							deposit.setType(Deposit.Type.cashier);
							deposit.setMemo(refunds.getMemo());
							deposit.setMember(member);
							deposit.setCredit(settle);
							deposit.setDebit(BigDecimal.ZERO);
							deposit.setDeleted(false);
							deposit.setOperator("system");
							deposit.setRefunds(refunds);
							deposit.setPayBill(payBill);
							depositDao.persist(deposit);
							messageService.depositPushTo(deposit);
						}
					}
					payBill.setPaymentPluginId(refunds.getPaymentPluginId());
					payBill.setMember(refunds.getMember());
					payBill.setStatus(PayBill.Status.refund_waiting);
					payBillDao.merge(payBill);
				}else
				if (refunds.getType() == Refunds.Type.card) {
					PayBill payBill = refunds.getPayBill();
					Member member =  refunds.getPayee();
					memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
					if (refunds.getMethod().equals(Refunds.Method.offline) || refunds.getMethod().equals(Refunds.Method.card)) {
						payBill.setFee(BigDecimal.ZERO);
						//线下业务，本身没有结款
					} else {
						BigDecimal settle = payBill.getSettleAmount();
						if (settle.compareTo(BigDecimal.ZERO) != 0) {
							if (member.getBalance().add(settle).compareTo(BigDecimal.ZERO)<0) {
								throw new RuntimeException("余额不足");
							}
							member.setBalance(member.getBalance().add(settle));
							memberDao.merge(member);
							Deposit deposit = new Deposit();
							deposit.setBalance(member.getBalance());
							deposit.setType(Deposit.Type.card);
							deposit.setMemo(refunds.getMemo());
							deposit.setMember(member);
							deposit.setCredit(settle);
							deposit.setDebit(BigDecimal.ZERO);
							deposit.setDeleted(false);
							deposit.setOperator("system");
							deposit.setRefunds(refunds);
							deposit.setPayBill(payBill);
							depositDao.persist(deposit);
							messageService.depositPushTo(deposit);
						}
					}
					payBill.setMember(refunds.getMember());
					payBill.setPaymentPluginId(refunds.getPaymentPluginId());
					payBill.setStatus(PayBill.Status.refund_waiting);
					payBillDao.merge(payBill);
					if (payBill.getType().equals(PayBill.Type.cardRefund)) {
						Card card = payBill.getCard();
						cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
						if (card.getBalance().add(payBill.getCardAmount()).compareTo(BigDecimal.ZERO)<0) {
							throw new RuntimeException("会员卡余额不足");
						}
						card.setBalance(card.getBalance().add(payBill.getCardAmount()));
						cardDao.merge(card);

						CardBill cardBill = new CardBill();
						cardBill.setDeleted(false);
						cardBill.setOwner(payBill.getOwner());
						cardBill.setShop(payBill.getShop());
						cardBill.setCredit(BigDecimal.ZERO);
						cardBill.setDebit(BigDecimal.ZERO.subtract(payBill.getCardAmount()));
						if (payBill.getAdmin()!=null) {
							cardBill.setMemo("退款，操作员:"+payBill.getAdmin().getName());
							cardBill.setOperator(payBill.getAdmin().getName());
						} else {
							cardBill.setMemo("自助退款");
							cardBill.setOperator(payBill.getCard().getName());
						}
						if (refunds.getMethod().equals(Refunds.Method.offline)) {
							cardBill.setMethod(CardBill.Method.offline);
						} else {
							cardBill.setMethod(CardBill.Method.online);
						}
						cardBill.setMember(card.getMembers().get(0));
						cardBill.setCard(card);
						cardBill.setType(CardBill.Type.refunds);
						cardBill.setBalance(card.getBalance());
						cardBillDao.persist(cardBill);

						if (card.getMobile()!=null && card.getMobile().length()==11) {
							String content = "";
							DecimalFormat df=(DecimalFormat) NumberFormat.getInstance();
							df.setMaximumFractionDigits(2);
							content = card.getTopicCard().getTopic().getName() + ",会员卡退款"+df.format(payBill.getCardDiscount())+"元,余额:"+df.format(card.getBalance());
							smssendService.send(payBill.getOwner(), card.getMobile(),content);
						}

					}
				}
				return true;
			} else {
				throw new RuntimeException("重复退款");
			}
	}

	//关闭退款
	@Transactional
	public synchronized void close(Refunds refunds) throws Exception {
		refundsDao.refresh(refunds, LockModeType.PESSIMISTIC_WRITE);
		if (refunds.getStatus().equals(Refunds.Status.confirmed)) {
			refunds.setStatus(Refunds.Status.failure);
			refundsDao.merge(refunds);
			refundsDao.flush();
			Payment payment = refunds.getPayment();
			if (payment!=null) {
				paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
				if (payment.getStatus().equals(Payment.Status.refund_waiting)) {
					payment.setStatus(Payment.Status.success);
					paymentDao.merge(payment);
				} else {
					throw new RuntimeException("重复提交");
				}
				PayBill payBill = payment.getPayBill();
				if (payBill!=null) {
					if (payBill.getStatus().equals(PayBill.Status.refund_waiting)) {
						payBill.setStatus(PayBill.Status.success);
						payBillDao.merge(payBill);
					}
				}
			}
			if (refunds.getType().equals(Refunds.Type.cashier)) {
				Member member =  refunds.getPayee();
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				PayBill payBill = refunds.getPayBill();
				if (refunds.getMethod().equals(Refunds.Method.offline) || refunds.getMethod().equals(Refunds.Method.card)) {
					payBill.setFee(BigDecimal.ZERO);
					//线下业务，本身没有结款
				} else {
					BigDecimal settle = payBill.getSettleAmount();
					if (settle.compareTo(BigDecimal.ZERO) != 0) {
						member.setBalance(member.getBalance().subtract(settle));
						memberDao.merge(member);
						Deposit deposit = new Deposit();
						deposit.setBalance(member.getBalance());
						deposit.setType(Deposit.Type.cashier);
						deposit.setMemo("【撤消】"+refunds.getMemo());
						deposit.setMember(member);
						deposit.setCredit(BigDecimal.ZERO.subtract(settle));
						deposit.setDebit(BigDecimal.ZERO);
						deposit.setDeleted(false);
						deposit.setOperator("system");
						deposit.setRefunds(refunds);
						deposit.setPayBill(payBill);
						depositDao.persist(deposit);
						messageService.depositPushTo(deposit);
					}
				}
				payBill.setPaymentPluginId(refunds.getPaymentPluginId());
				payBill.setMember(refunds.getMember());
				payBill.setStatus(PayBill.Status.failure);
				payBillDao.merge(payBill);
			}else
			if (refunds.getType() == Refunds.Type.card) {
				Member member =  refunds.getPayee();
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				PayBill payBill = refunds.getPayBill();
				if (refunds.getMethod().equals(Refunds.Method.offline) || refunds.getMethod().equals(Refunds.Method.card)) {
					payBill.setFee(BigDecimal.ZERO);
					//线下业务，本身没有结款
				} else {
					BigDecimal settle = payBill.getSettleAmount();
					if (settle.compareTo(BigDecimal.ZERO) != 0) {
						member.setBalance(member.getBalance().subtract(settle));
						memberDao.merge(member);
						Deposit deposit = new Deposit();
						deposit.setBalance(member.getBalance());
						deposit.setType(Deposit.Type.card);
						deposit.setMemo("【撤消】"+refunds.getMemo());
						deposit.setMember(member);
						deposit.setCredit(BigDecimal.ZERO.subtract(settle));
						deposit.setDebit(BigDecimal.ZERO);
						deposit.setDeleted(false);
						deposit.setOperator("system");
						deposit.setRefunds(refunds);
						deposit.setPayBill(payBill);
						depositDao.persist(deposit);
						messageService.depositPushTo(deposit);
					}
				}
				payBill.setMember(refunds.getMember());
				payBill.setPaymentPluginId(refunds.getPaymentPluginId());
				payBill.setStatus(PayBill.Status.failure);
				payBillDao.merge(payBill);
				if (payBill.getType().equals(PayBill.Type.cardRefund)) {
					Card card = payBill.getCard();
					cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
					card.setBalance(card.getBalance().subtract(payBill.getCardAmount()));
					cardDao.merge(card);

					CardBill cardBill = new CardBill();
					cardBill.setDeleted(false);
					cardBill.setOwner(payBill.getOwner());
					cardBill.setShop(payBill.getShop());
					cardBill.setCredit(BigDecimal.ZERO);
					cardBill.setDebit(payBill.getCardAmount());
					if (payBill.getAdmin()!=null) {
						cardBill.setMemo("【撤消】退款，操作员:"+payBill.getAdmin().getName());
						cardBill.setOperator(payBill.getAdmin().getName());
					} else {
						cardBill.setMemo("【撤消】自助退款");
						cardBill.setOperator(payBill.getCard().getName());
					}
					if (refunds.getMethod().equals(Refunds.Method.offline)) {
						cardBill.setMethod(CardBill.Method.offline);
					} else {
						cardBill.setMethod(CardBill.Method.online);
					}
					cardBill.setMember(card.getMembers().get(0));
					cardBill.setCard(card);
					cardBill.setType(CardBill.Type.refunds);
					cardBill.setBalance(card.getBalance());
					cardBillDao.persist(cardBill);
				}
			}
		} else {
			throw new RuntimeException("重复关闭");
		}
	}



	/**
	 * 查询状态
	 */
	public void query() {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("status", Filter.Operator.eq,Refunds.Status.confirmed));
		filters.add(new Filter("createDate", Operator.le, DateUtils.addMinutes(new Date(),-30) ));
		List<Refunds> data = refundsDao.findList(null,null,filters,null);
		for (Refunds refunds:data) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
			String resultCode = null;
			try {
				if (paymentPlugin == null) {
					resultCode = "0001";
				} else {
					resultCode = paymentPlugin.refundsQuery(refunds,null);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			switch (resultCode) {
				case "0000":
					try {
						this.handle(refunds);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				case "0001":
					try {
						this.close(refunds);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
			}
		}

	}


}