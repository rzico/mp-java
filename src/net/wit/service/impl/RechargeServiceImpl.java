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
import net.wit.service.MemberService;
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

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

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
	public synchronized Boolean agentSubmit(Recharge recharge,Member agent) throws Exception {
		recharge.setStatus(Recharge.Status.success);
		recharge.setTransferDate(new Date());
		rechargeDao.persist(recharge);
		Member member = recharge.getMember();
		try {
			//扣代理款
			memberDao.refresh(agent, LockModeType.PESSIMISTIC_WRITE);
			agent.setBalance(agent.getBalance().subtract(recharge.getAmount()));
			if (agent.getBalance().compareTo(BigDecimal.ZERO)<0) {
				throw new RuntimeException("代理商余额不足");
			}
			if (agent.getFreezeBalance().compareTo(recharge.getAmount())>0) {
				agent.setFreezeBalance(agent.getFreezeBalance().subtract(recharge.getAmount()));
			} else {
				agent.setFreezeBalance(BigDecimal.ZERO);
			}
			memberDao.merge(agent);
			Deposit deposit = new Deposit();
			deposit.setBalance(agent.getBalance());
			deposit.setType(Deposit.Type.payment);
			deposit.setMemo("代"+member.getUsername()+"充值");
			deposit.setMember(agent);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(recharge.getAmount());
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setRecharge(recharge);
			depositDao.persist(deposit);

			//充用户款

			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().add(recharge.effectiveAmount()));
			member.setFreezeBalance(member.getFreezeBalance().add(recharge.effectiveAmount()));
			memberDao.merge(member);
			Deposit memberDeposit = new Deposit();
			memberDeposit.setBalance(member.getBalance());
			memberDeposit.setType(Deposit.Type.recharge);
			memberDeposit.setMemo(recharge.getMemo());
			memberDeposit.setMember(member);
			memberDeposit.setCredit(recharge.getAmount());
			memberDeposit.setDebit(BigDecimal.ZERO);
			memberDeposit.setDeleted(false);
			memberDeposit.setOperator("system");
			memberDeposit.setRecharge(recharge);
			depositDao.persist(memberDeposit);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}

		if (member.getPromoter()==null) {
			member.setPromoter(agent);
			memberDao.merge(member);
			memberService.create(member, agent);
		}

		return true;
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
			member.setFreezeBalance(member.getFreezeBalance().add(recharge.effectiveAmount()));
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