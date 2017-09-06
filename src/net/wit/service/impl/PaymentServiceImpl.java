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

import net.wit.dao.SnDao;
import net.wit.service.PluginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.PaymentDao;
import net.wit.entity.*;
import net.wit.service.PaymentService;

/**
 * @ClassName: PaymentDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:54:59
 */
 
 
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}


	@Transactional
	public synchronized void handle(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && !payment.getStatus().equals(Payment.Status.success)) {
			if (payment.getType() == Payment.Type.payment) {
			} else if (payment.getType() == Payment.Type.recharge) {
			}
			payment.setPaymentDate(new Date());
			paymentDao.merge(payment);
		}
	}



	@Transactional
	public void close(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus() == Payment.Status.waiting) {
			payment.setMemo("超时关闭");
			payment.setStatus(Payment.Status.failure);
			paymentDao.merge(payment);
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