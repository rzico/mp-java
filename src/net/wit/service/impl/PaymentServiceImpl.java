package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.*;
import net.wit.service.MessageService;
import net.wit.service.PluginService;
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

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}


	@Transactional
	public synchronized void handle(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && !payment.getStatus().equals(Payment.Status.success)) {
//			//余额支付时，扣余额
//			if (payment.getMethod().equals(Payment.Method.deposit)) {
//				Member member = payment.getPayee();
//				memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
//				if (member.getBalance().compareTo(payment.getAmount())>=0) {
//					member.setBalance(member.getBalance().subtract(payment.getAmount()));
//					memberDao.merge(member);
//					Deposit deposit = new Deposit();
//					deposit.setBalance(member.getBalance());
//					deposit.setType(Deposit.Type.payment);
//					deposit.setMemo(payment.getMemo());
//					deposit.setMember(member);
//					deposit.setCredit(BigDecimal.ZERO);
//					deposit.setDebit(payment.getAmount());
//					deposit.setDeleted(false);
//					deposit.setOperator("system");
//					depositDao.persist(deposit);
//					messageService.depositPushTo(deposit);
//				} else {
//					payment.setPaymentDate(new Date());
//					payment.setStatus(Payment.Status.failure);
//					paymentDao.merge(payment);
//					throw new Exception("余额不足");
//				}
//			}
			//处理支付结果
			if (payment.getType() == Payment.Type.payment) {
			} else
			if (payment.getType() == Payment.Type.cashier) {
				Member member = payment.getPayee();
				memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
				PayBill payBill = payment.getPayBill();
				BigDecimal settle = payBill.getSettleAmount();
				if (settle.compareTo(BigDecimal.ZERO)>0) {
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
				payBill.setStatus(PayBill.Status.success);
				payBillDao.merge(payBill);
			} else
			if (payment.getType() == Payment.Type.reward) {
				Member member = payment.getPayee();
				memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
				member.setBalance(member.getBalance().add(payment.getAmount()));
				memberDao.merge(member);
				Deposit deposit = new Deposit();
				deposit.setBalance(member.getBalance());
				deposit.setType(Deposit.Type.reward);
				deposit.setMemo(payment.getMemo());
				deposit.setMember(member);
				deposit.setCredit(payment.getAmount());
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setPayment(payment);
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);
				ArticleReward reward = payment.getArticleReward();
				if (reward!=null) {
					messageService.rewardPushTo(reward);
				}
			} else
			if (payment.getType() == Payment.Type.recharge) {
				Member member = payment.getPayee();
				memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
				member.setBalance(member.getBalance().add(payment.getAmount()));
				memberDao.merge(member);
				Deposit deposit = new Deposit();
				deposit.setBalance(member.getBalance());
				deposit.setType(Deposit.Type.recharge);
				deposit.setMemo(payment.getMemo());
				deposit.setMember(member);
				deposit.setCredit(payment.getAmount());
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setPayment(payment);
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);
			}
			payment.setPaymentDate(new Date());
			payment.setStatus(Payment.Status.success);
			paymentDao.merge(payment);
		} else {
			if (!payment.getStatus().equals(Payment.Status.waiting)) {
				throw new Exception("重复提交");
			} else {
				throw new Exception("无效付款单");
			}
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