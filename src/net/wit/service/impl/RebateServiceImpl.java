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
import net.wit.entity.summary.RebateSummary;
import net.wit.service.MessageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.RebateDao;
import net.wit.entity.*;
import net.wit.service.RebateService;

/**
 * @ClassName: RebateDaoImpl
 * @author 降魔战队
 * @date 2018-4-24 20:57:53
 */
 
 
@Service("rebateServiceImpl")
public class RebateServiceImpl extends BaseServiceImpl<Rebate, Long> implements RebateService {
	@Resource(name = "rebateDaoImpl")
	private RebateDao rebateDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "rebateDaoImpl")
	public void setBaseDao(RebateDao rebateDao) {
		super.setBaseDao(rebateDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Rebate rebate) {
		super.save(rebate);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Rebate update(Rebate rebate) {
		return super.update(rebate);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Rebate update(Rebate rebate, String... ignoreProperties) {
		return super.update(rebate, ignoreProperties);
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
	public void delete(Rebate rebate) {
		super.delete(rebate);
	}

	public Page<Rebate> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return rebateDao.findPage(beginDate,endDate,pageable);
	}

	public Page<RebateSummary> sumPage(Date beginDate, Date endDate, Enterprise enterprise,Pageable pageable) {
		return rebateDao.sumPage(beginDate,endDate,enterprise,pageable);
	}

	public void rebate(BigDecimal amount,Enterprise personal,Enterprise agent,Enterprise operate,Order order) throws Exception {
       if (personal!=null) {
       	 Member member = personal.getMember();
       	 memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
       	 BigDecimal rebate = amount.multiply(personal.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
       	 member.setBalance(member.getBalance().add(rebate));
       	 memberDao.merge(member);
       	 memberDao.flush();
		   Deposit d1 = new Deposit();
		   d1.setBalance(member.getBalance());
		   d1.setType(Deposit.Type.rebate);
		   d1.setMemo("代理奖励金");
		   d1.setMember(member);
		   d1.setCredit(amount);
		   d1.setDebit(BigDecimal.ZERO);
		   d1.setDeleted(false);
		   d1.setOperator("system");
		   d1.setOrder(order);
		   d1.setSeller(order.getSeller());
		   depositDao.persist(d1);
		   messageService.depositPushTo(d1);
	   }
		if (agent!=null) {
			Member member = agent.getMember();
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
			BigDecimal rebate = amount.multiply(agent.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			member.setBalance(member.getBalance().add(rebate));
			memberDao.merge(member);
			memberDao.flush();
			Deposit d1 = new Deposit();
			d1.setBalance(member.getBalance());
			d1.setType(Deposit.Type.rebate);
			d1.setMemo("代理奖励金");
			d1.setMember(member);
			d1.setCredit(amount);
			d1.setDebit(BigDecimal.ZERO);
			d1.setDeleted(false);
			d1.setOperator("system");
			d1.setOrder(order);
			d1.setSeller(order.getSeller());
			depositDao.persist(d1);
			messageService.depositPushTo(d1);
		}
		if (operate!=null) {
			Member member = operate.getMember();
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
			BigDecimal rebate = amount.multiply(operate.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			member.setBalance(member.getBalance().add(rebate));
			memberDao.merge(member);
			memberDao.flush();
			Deposit d1 = new Deposit();
			d1.setBalance(member.getBalance());
			d1.setType(Deposit.Type.rebate);
			d1.setMemo("代理奖励金");
			d1.setMember(member);
			d1.setCredit(amount);
			d1.setDebit(BigDecimal.ZERO);
			d1.setDeleted(false);
			d1.setOperator("system");
			d1.setOrder(order);
			d1.setSeller(order.getSeller());
			depositDao.persist(d1);
			messageService.depositPushTo(d1);
		}
	}

}