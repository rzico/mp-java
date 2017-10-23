package net.wit.service.impl;

import java.math.BigDecimal;
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

import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.plat.unspay.UnsPay;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.PluginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.RefundsDao;
import net.wit.entity.*;
import net.wit.service.RefundsService;

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

	@Transactional
	public synchronized void handle(Refunds refunds) throws Exception {
		refundsDao.refresh(refunds, LockModeType.PESSIMISTIC_WRITE);
		Payment payment = refunds.getPayment();
		if (refunds != null && !refunds.getStatus().equals(Payment.Status.success)) {
			refunds.setRefundsDate(new Date());
			refunds.setStatus(Refunds.Status.success);
			refundsDao.merge(refunds);
			if (payment.getStatus().equals(Payment.Status.refund_waiting)) {
				payment.setStatus(Payment.Status.refund_success);
			} else if (payment.getStatus().equals(Payment.Status.refund_success)) {
				throw new Exception("重复退款");
			} else {
				throw new Exception("退款失败");
			}
		}
	}

	@Transactional
	public synchronized Boolean refunds(Refunds refunds, HttpServletRequest request) throws Exception {
			refundsDao.refresh(refunds, LockModeType.PESSIMISTIC_WRITE);
	    	Payment payment = refunds.getPayment();
			if (refunds != null && refunds.getStatus().equals(Refunds.Status.waiting)) {
				PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
				String result = paymentPlugin.refunds(refunds,request);
				if ("0000".equals(result)) {
					refunds.setStatus(Refunds.Status.confirmed);
					refundsDao.merge(refunds);
					return true;
				} else {
					throw new Exception(UnsPay.getErrMsg(result));
				}
			} else{
				throw new Exception("已经提交了");
			}
	}

	@Transactional
	public synchronized void close(Refunds refunds) throws Exception {
		refundsDao.refresh(refunds, LockModeType.PESSIMISTIC_WRITE);
		Payment payment = refunds.getPayment();
		//退款失败，钱退回账户
		if (refunds != null && !refunds.getStatus().equals(Refunds.Status.failure)) {
			//开始退款
			Member member = refunds.getMember();
			memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().add(refunds.getAmount()));
			memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			if (refunds.getType().equals(Refunds.Type.cashier)) {
				deposit.setType(Deposit.Type.cashier);
				deposit.setCredit(BigDecimal.ZERO);
				deposit.setDebit(BigDecimal.ZERO.subtract(refunds.getAmount()));
			} else {
				deposit.setType(Deposit.Type.refunds);
				deposit.setCredit(refunds.getAmount());
				deposit.setDebit(BigDecimal.ZERO);
			}
			deposit.setMemo("【退款】"+refunds.getMemo());
			deposit.setMember(member);
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setRefunds(refunds);
			depositDao.persist(deposit);
			refunds.setRefundsDate(new Date());
			refunds.setStatus(Refunds.Status.failure);
			refundsDao.merge(refunds);
			if (payment.getStatus().equals(Payment.Status.refund_waiting)) {
				payment.setStatus(Payment.Status.refund_success);
			} else if (payment.getStatus().equals(Payment.Status.refund_success)) {
				throw new Exception("重复退款");
			} else {
				throw new Exception("退款失败");
			}
		} else {
			throw new Exception("重复提交");
		}
	}
}