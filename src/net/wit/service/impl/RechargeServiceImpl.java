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

import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.plat.unspay.UnsPay;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.RechargeDao;
import net.wit.entity.*;
import net.wit.service.RechargeService;
import org.tuckey.web.filters.urlrewrite.Run;

/**
 * @ClassName: RechargeDaoImpl
 * @author 降魔战队
 * @date 2018-2-1 14:1:27
 */


@Service("rechargeServiceImpl")
public class RechargeServiceImpl extends BaseServiceImpl<Recharge, Long> implements RechargeService {

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "rechargeDaoImpl")
	private RechargeDao rechargeDao;

	@Resource(name = "rechargeDaoImpl")
	public void setBaseDao(RechargeDao rechargeDao) {
		super.setBaseDao(rechargeDao);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Recharge recharge) {
		super.save(recharge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Recharge update(Recharge recharge) {
		return super.update(recharge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Recharge update(Recharge recharge, String... ignoreProperties) {
		return super.update(recharge, ignoreProperties);
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
	public void delete(Recharge recharge) {
		super.delete(recharge);
	}

	public Page<Recharge> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return rechargeDao.findPage(beginDate,endDate,pageable);
	}

	public Recharge findBySn(String sn) {
		return rechargeDao.findBySn(sn);
	}

	@Transactional
	public synchronized Boolean submit(Recharge recharge) throws Exception {
		Member member = recharge.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {
			recharge.setStatus(Recharge.Status.success);
			recharge.setTransferDate(new Date());
			rechargeDao.persist(recharge);
			member.setBalance(member.getBalance().add(recharge.effectiveAmount()));
			memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.recharge);
			deposit.setMemo(recharge.getMemo());
			deposit.setMember(member);
			deposit.setCredit(recharge.effectiveAmount());
			deposit.setDebit(BigDecimal.ZERO);
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setRecharge(recharge);
			depositDao.persist(deposit);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
		return true;
	}

	@Transactional
	public synchronized void handle(Recharge recharge) throws Exception {
		Member member = recharge.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {
			if (recharge != null && recharge.getStatus().equals(Recharge.Status.confirmed)) {
				recharge.setStatus(Recharge.Status.success);
				recharge.setTransferDate(new Date());
				rechargeDao.merge(recharge);
				member.setBalance(member.getBalance().add(recharge.effectiveAmount()));
				memberDao.merge(member);
				memberDao.flush();
				Deposit deposit = new Deposit();
				deposit.setBalance(member.getBalance());
				deposit.setType(Deposit.Type.recharge);
				deposit.setMemo(recharge.getMemo());
				deposit.setMember(member);
				deposit.setCredit(recharge.effectiveAmount());
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setRecharge(recharge);
				depositDao.persist(deposit);
			} else {
				throw  new RuntimeException("重复提交");
			}

		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("加款出错了");
		}
	}

	@Transactional
	public synchronized void close(Recharge recharge) throws Exception {
		try {
			if (recharge != null && !recharge.getStatus().equals(Recharge.Status.confirmed)) {
				recharge.setStatus(Recharge.Status.failure);
				recharge.setTransferDate(new Date());
				rechargeDao.persist(recharge);
			} else {
				throw  new RuntimeException("重复提交");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("退款出错了");
		}
	}


}