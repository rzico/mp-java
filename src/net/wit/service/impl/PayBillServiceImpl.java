package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.dao.*;
import net.wit.entity.summary.PayBillShopSummary;
import net.wit.service.SnService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.PayBillService;

/**
 * @ClassName: PayBillDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:28
 */
 
 
@Service("payBillServiceImpl")
public class PayBillServiceImpl extends BaseServiceImpl<PayBill, Long> implements PayBillService {
	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "cardBillDaoImpl")
	private CardBillDao cardBillDao;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "payBillDaoImpl")
	private PayBillDao payBillDao;

	@Resource(name = "cardDaoImpl")
	private CardDao cardDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "payBillDaoImpl")
	public void setBaseDao(PayBillDao payBillDao) {
		super.setBaseDao(payBillDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(PayBill payBill) {
		super.save(payBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public PayBill update(PayBill payBill) {
		return super.update(payBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public PayBill update(PayBill payBill, String... ignoreProperties) {
		return super.update(payBill, ignoreProperties);
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
	public void delete(PayBill payBill) {
		super.delete(payBill);
	}

	public Page<PayBill> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return payBillDao.findPage(beginDate,endDate,pageable);
	}

	public Payment submit(PayBill payBill) throws Exception {
		try {
			Card card = payBill.getCard();
			payBill.setBillDate(DateUtils.truncate(new Date(), Calendar.DATE));
			payBillDao.persist(payBill);
			CouponCode couponCode = payBill.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(true);
				couponCode.setUsedDate(new Date());
				couponCodeDao.merge(couponCode);
			}
			Payment payment = new Payment();
			payment.setPayee(payBill.getOwner());
			payment.setMember(payBill.getMember());
			payment.setStatus(Payment.Status.waiting);
			payment.setMethod(Payment.Method.online);
			payment.setType(Payment.Type.cashier);
			payment.setMemo("线下收款");
			payment.setAmount(payBill.getPayBillAmount());
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setPayBill(payBill);
			paymentDao.persist(payment);

			payBill.setPayment(payment);

			payBillDao.merge(payBill);

			return payment;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("提交失败");
		}
	}

	public Payment cardFill(PayBill payBill) throws Exception {
		try {
			Card card = payBill.getCard();
			payBill.setBillDate(DateUtils.truncate(new Date(), Calendar.DATE));
			payBillDao.persist(payBill);
			CouponCode couponCode = payBill.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(true);
				couponCode.setUsedDate(new Date());
				couponCodeDao.merge(couponCode);
			}
			Payment payment = new Payment();
			payment.setPayee(payBill.getOwner());
			payment.setMember(payBill.getMember());
			payment.setStatus(Payment.Status.waiting);
			payment.setMethod(Payment.Method.online);
			payment.setType(Payment.Type.card);
			payment.setMemo("会员卡充值");
			payment.setAmount(payBill.getPayBillAmount());
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setPayBill(payBill);
			paymentDao.persist(payment);

			payBill.setPayment(payment);

			payBillDao.merge(payBill);

			return payment;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("提交失败");
		}
	}

	public Refunds cardRefund(PayBill payBill) throws Exception {
		try {
			Card card = payBill.getCard();
			payBill.setBillDate(DateUtils.truncate(new Date(), Calendar.DATE));
			payBillDao.persist(payBill);
			Refunds refunds = new Refunds();
			refunds.setPayee(payBill.getOwner());
			refunds.setMember(payBill.getMember());
			refunds.setStatus(Refunds.Status.waiting);
			refunds.setMethod(Refunds.Method.offline);
			refunds.setType(Refunds.Type.card);
			refunds.setMemo("会员卡退款");
			refunds.setAmount(BigDecimal.ZERO.subtract(payBill.getPayBillAmount()));
			refunds.setSn(snService.generate(Sn.Type.refunds));
			refunds.setPayBill(payBill);
			refundsDao.persist(refunds);

			payBill.setRefunds(refunds);

			payBillDao.merge(payBill);

			return refunds;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("提交失败");
		}
	}

	//收款创建退款
	public PayBill createRefund(PayBill payBill,Admin admin) throws Exception {

        payBillDao.refresh(payBill,LockModeType.PESSIMISTIC_WRITE);
		if (payBill.getStatus().equals(PayBill.Status.refund_waiting) || payBill.getStatus().equals(PayBill.Status.refund_success)) {
			throw new RuntimeException("不能重复退款");
		}

        Payment payment = payBill.getPayment();
        paymentDao.refresh(payment,LockModeType.PESSIMISTIC_WRITE);
        if (payment.getStatus().equals(Payment.Status.refund_waiting) || payment.getStatus().equals(Payment.Status.refund_success)) {
			throw new RuntimeException("不能重复退款");
		}
		if (!payment.getMethod().equals(Payment.Method.offline) || payment.getMethod().equals(Payment.Method.card)) {
            Member payee = payment.getPayee();
            memberDao.refresh(payee,LockModeType.PESSIMISTIC_WRITE);
            if (payee.getBalance().compareTo(payBill.getSettleAmount())<0) {
				throw new RuntimeException("账户余额不足");
			}
		}
		PayBill bill = new PayBill();
        bill.setMember(payBill.getMember());
        bill.setFee(payBill.getFee());
        if (payBill.getType().equals(PayBill.Type.card)) {
        	Card card = payBill.getCard();
        	cardDao.refresh(card,LockModeType.PESSIMISTIC_WRITE);
        	if (card.getBalance().subtract(payBill.getCardAmount()).compareTo(BigDecimal.ZERO)<0)  {
        		throw new RuntimeException("卡余额不足");
			}
			bill.setType(PayBill.Type.cardRefund);
		} else {
			bill.setType(PayBill.Type.cashierRefund);
		}
		bill.setStatus(PayBill.Status.none);
        bill.setCardAmount(BigDecimal.ZERO.subtract(payBill.getCardAmount()));
        bill.setCard(payBill.getCard());
        bill.setCardDiscount(payBill.getCardDiscount());
        bill.setAdmin(admin);
		bill.setBillDate(DateUtils.truncate(new Date(), Calendar.DATE));
        bill.setEnterprise(payBill.getEnterprise());
        bill.setShop(payBill.getShop());
        bill.setOwner(payBill.getOwner());
        bill.setFee(BigDecimal.ZERO.subtract(payBill.getFee()));
        bill.setMethod(payBill.getMethod());
        bill.setCouponDiscount(BigDecimal.ZERO.subtract(payBill.getCouponDiscount()));
        bill.setCouponCode(payBill.getCouponCode());
        bill.setNoDiscount(BigDecimal.ZERO.subtract(payBill.getNoDiscount()));
        bill.setAmount(BigDecimal.ZERO.subtract(payBill.getAmount()));
		payBillDao.persist(bill);
		Refunds refunds = new Refunds();
		refunds.setPaymentMethod(payment.getPaymentMethod());
		refunds.setPayment(payment);
		refunds.setPaymentPluginId(payment.getPaymentPluginId());
		refunds.setStatus(Refunds.Status.waiting);
		refunds.setAmount(payBill.getPayBillAmount());
		refunds.setPayBill(bill);
		refunds.setMember(payment.getMember());
		refunds.setPayee(payment.getPayee());
		refunds.setMemo("(撤消退款)"+payment.getMemo());
		refunds.setMethod(Refunds.Method.values()[payment.getMethod().ordinal()]);
		refunds.setType(Refunds.Type.values()[payment.getType().ordinal()]);
		refunds.setSn(snService.generate(Sn.Type.refunds));
		refundsDao.persist(refunds);
		bill.setRefunds(refunds);
		payBillDao.merge(bill);
		payBill.setStatus(PayBill.Status.refund_waiting);
		payBillDao.merge(payBill);
		return bill;
	}

	public List<PayBillShopSummary> sumPage(Shop shop,Enterprise enterprise,Date beginDate,Date endDate) {
		return payBillDao.sumPage(shop,enterprise,beginDate,endDate);
	}

}