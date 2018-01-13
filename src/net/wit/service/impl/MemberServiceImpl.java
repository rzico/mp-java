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
import net.wit.dao.PaymentDao;
import net.wit.dao.RefundsDao;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.MemberDao;
import net.wit.entity.*;
import net.wit.service.MemberService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName: MemberDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("memberServiceImpl")
public class MemberServiceImpl extends BaseServiceImpl<Member, Long> implements MemberService {
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "redisServiceImpl")
	private RedisService redisService;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "memberDaoImpl")
	public void setBaseDao(MemberDao memberDao) {
		super.setBaseDao(memberDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Member member) {
		super.save(member);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Member update(Member member) {
		return super.update(member);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Member update(Member member, String... ignoreProperties) {
		return super.update(member, ignoreProperties);
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
	public void delete(Member member) {
		super.delete(member);
	}

	public Page<Member> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return memberDao.findPage(beginDate,endDate,pageable);
	}
	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}
	@Transactional(readOnly = true)
	public Member findByMobile(String mobile) {
		return memberDao.findByMobile(mobile);
	}
	@Transactional(readOnly = true)
	public Member findByUUID(String uuid) {
		return memberDao.findByUUID(uuid);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		Redis redis = redisService.findKey(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (redis!=null) {
			return true;
		}

		return false;
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		Redis redis = redisService.findKey(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (redis!=null) {
			String js = redis.getValue();
			Principal principal = JsonUtils.toObject(js,Principal.class);
			if (principal != null) {
				return memberDao.find(principal.getId());
			}
		}
		return null;
	}

	//支付插件专用方法
	public void payment(Member member,Payment payment) throws Exception {
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		if (member.getBalance().compareTo(payment.getAmount()) < 0) {
			throw new Exception("余额不足");
		}
		try {
			member.setBalance(member.getBalance().subtract(payment.getAmount()));
			memberDao.merge(member);

			payment.setMember(member);
			payment.setTranSn(payment.getSn());
			payment.setMethod(Payment.Method.deposit);
			paymentDao.merge(payment);

			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setMember(member);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(payment.getAmount());
			deposit.setDeleted(false);
			deposit.setType(Deposit.Type.payment);
			deposit.setPayBill(payment.getPayBill());
			deposit.setOrder(payment.getOrder());
			deposit.setMemo(payment.getMemo());
			deposit.setPayment(payment);
			depositDao.persist(deposit);

		} catch (Exception  e) {
			throw new RuntimeException("支付失败");
		}
	}

	//支付插件专用方法
	public void refunds(Member member,Refunds refunds) throws Exception {
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {
			member.setBalance(member.getBalance().add(refunds.getAmount()));
			memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setMember(member);
			deposit.setCredit(refunds.getAmount());
			deposit.setDebit(BigDecimal.ZERO);
			deposit.setDeleted(false);
			deposit.setType(Deposit.Type.refunds);
			PayBill payBill = refunds.getPayBill();
			if (payBill!=null) {
				deposit.setPayBill(payBill);
			}
			deposit.setMemo(refunds.getMemo());
			deposit.setRefunds(refunds);
			depositDao.persist(deposit);
			refunds.setMember(member);
			refunds.setTranSn(refunds.getSn());
			refunds.setMethod(Refunds.Method.deposit);
			refundsDao.merge(refunds);
		} catch (Exception  e) {
			throw  new RuntimeException("退款失败");
		}
	}

}