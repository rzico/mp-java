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

import net.wit.controller.model.CardActivityModel;
import net.wit.controller.weex.member.CardController;
import net.wit.dao.*;
import net.wit.entity.summary.CardActivity;
import net.wit.service.MessageService;
import net.wit.service.PluginService;
import net.wit.util.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.PaymentService;

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

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "payBillDaoImpl")
	private PayBillDao payBillDao;

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

	@Resource(name = "articleRewardDaoImpl")
	private ArticleRewardDao articleRewardDao;

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


	private Card.VIP calculateVip(Shop shop, BigDecimal amount) {
		Member owner = shop.getOwner();
		Topic topic = owner.getTopic();

		if (topic==null) {
			return null;
		}

		if (topic.getTopicCard()==null) {
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
			return Card.VIP.valueOf(curr.get("vip").toString());
		} else {
			return null;
		}
	}

	@Transactional
	public synchronized void handle(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && !payment.getStatus().equals(Payment.Status.success)) {
			//处理支付结果
			if (payment.getType() == Payment.Type.payment) {
			} else
			if (payment.getType() == Payment.Type.cashier) {
				Member member = payment.getPayee();
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				PayBill payBill = payment.getPayBill();
				if (payment.getMethod().equals(Payment.Method.offline) || payment.getMethod().equals(Payment.Method.card)) {
					payBill.setFee(BigDecimal.ZERO);
					payBill.setMethod(PayBill.Method.offline);
					//线下或会员卡支付，不需要给商家结算
				} else {
					BigDecimal settle = payBill.getSettleAmount();
					if (settle.compareTo(BigDecimal.ZERO) > 0) {
						member.setBalance(member.getBalance().add(settle));
						memberDao.merge(member);
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
						depositDao.persist(deposit);
						messageService.depositPushTo(deposit);
					}
					payBill.setMethod(PayBill.Method.online);
				}
				payBill.setPaymentPluginId(payment.getPaymentPluginId());
				payBill.setMember(payment.getMember());
				payBill.setStatus(PayBill.Status.success);
				payBillDao.merge(payBill);
			}else
			if (payment.getType() == Payment.Type.card) {
				Member member = payment.getPayee();
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				PayBill payBill = payment.getPayBill();
				if (payment.getMethod().equals(Payment.Method.offline) || payment.getMethod().equals(Payment.Method.card)) {
					payBill.setFee(BigDecimal.ZERO);
					payBill.setMethod(PayBill.Method.offline);
					//线下或会员卡支付，不需要给商家结算
				} else {
					BigDecimal settle = payBill.getSettleAmount();
					if (settle.compareTo(BigDecimal.ZERO) > 0) {
						member.setBalance(member.getBalance().add(settle));
						memberDao.merge(member);
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
						depositDao.persist(deposit);
						messageService.depositPushTo(deposit);
					}
					payBill.setMethod(PayBill.Method.online);
				}
				payBill.setPaymentPluginId(payment.getPaymentPluginId());
				payBill.setMember(payment.getMember());
				payBill.setStatus(PayBill.Status.success);
				payBillDao.merge(payBill);
				if (payBill.getType().equals(PayBill.Type.card)) {
					Card card = payBill.getCard();
					cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);

					Card.VIP vip = calculateVip(payBill.getShop(),payBill.getAmount());
                    if (vip!=null) {
                    	if (card.getVip().ordinal()<vip.ordinal()) {
                    		card.setVip(vip);
						}
					}
					card.setBalance(card.getBalance().add(payBill.getCardAmount()));
					cardDao.merge(card);

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
				}
			} else
			if (payment.getType() == Payment.Type.reward) {
				Member member = payment.getPayee();
				memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
				ArticleReward reward = payment.getArticleReward();
				member.setBalance(member.getBalance().add(reward.getAmount().subtract(reward.getFee())));
				memberDao.merge(member);
				Deposit deposit = new Deposit();
				deposit.setBalance(member.getBalance());
				deposit.setType(Deposit.Type.reward);
				deposit.setMemo(payment.getMemo());
				deposit.setMember(member);
				deposit.setCredit(reward.getAmount().subtract(reward.getFee()));
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setPayment(payment);
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);
				reward.setStatus(ArticleReward.Status.success);
				articleRewardDao.merge(reward);
				messageService.rewardPushTo(reward);
			} else
//			if (payment.getType() == Payment.Type.recharge) {
//				Member member = payment.getPayee();
//				memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
//				member.setBalance(member.getBalance().add(payment.getAmount()));
//				memberDao.merge(member);
//				Deposit deposit = new Deposit();
//				deposit.setBalance(member.getBalance());
//				deposit.setType(Deposit.Type.recharge);
//				deposit.setMemo(payment.getMemo());
//				deposit.setMember(member);
//				deposit.setCredit(payment.getAmount());
//				deposit.setDebit(BigDecimal.ZERO);
//				deposit.setDeleted(false);
//				deposit.setOperator("system");
//				deposit.setPayment(payment);
//				depositDao.persist(deposit);
//				messageService.depositPushTo(deposit);
//			} else
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
			}
			payment.setPaymentDate(new Date());
			payment.setStatus(Payment.Status.success);
			paymentDao.merge(payment);
		}
	}



	@Transactional
	public synchronized void close(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus() == Payment.Status.waiting) {
			payment.setStatus(Payment.Status.failure);
			paymentDao.merge(payment);
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
			if (payment.getType().equals(Payment.Type.card)) {
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
}