package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.dao.DepositDao;
import net.wit.dao.GoldDao;
import net.wit.dao.MemberDao;
import net.wit.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.GoldExchangeDao;
import net.wit.service.GoldExchangeService;

/**
 * @ClassName: goldExchangeDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */
 
 
@Service("goldExchangeServiceImpl")
public class GoldExchangeServiceImpl extends BaseServiceImpl<GoldExchange, Long> implements GoldExchangeService {
	@Resource(name = "goldExchangeDaoImpl")
	private GoldExchangeDao goldExchangeDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "goldDaoImpl")
	private GoldDao goldDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "goldExchangeDaoImpl")
	public void setBaseDao(GoldExchangeDao goldExchangeDao) {
		super.setBaseDao(goldExchangeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GoldExchange goldExchange) {
		super.save(goldExchange);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldExchange update(GoldExchange goldExchange) {
		return super.update(goldExchange);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldExchange update(GoldExchange goldExchange, String... ignoreProperties) {
		return super.update(goldExchange, ignoreProperties);
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
	public void delete(GoldExchange goldExchange) {
		super.delete(goldExchange);
	}

	public Page<GoldExchange> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goldExchangeDao.findPage(beginDate,endDate,pageable);
	}


	@Transactional
	public synchronized Boolean submit(GoldExchange goldExchange) throws Exception {
		Member member = goldExchange.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {

			if (member.getPoint()<goldExchange.getGold()) {
				throw new RuntimeException("金币余额不足");
			}

			goldExchange.setStatus(GoldExchange.Status.success);
			goldExchangeDao.persist(goldExchange);
			member.setPoint(member.getPoint()-goldExchange.getGold());
			member.setBalance(member.getBalance().add(goldExchange.getAmount()));
			memberDao.merge(member);

			Gold gold = new Gold();
			gold.setBalance(member.getPoint());
			gold.setType(Gold.Type.exchange);
			gold.setMemo(goldExchange.getMemo());
			gold.setMember(member);
			gold.setCredit(0L);
			gold.setDebit(goldExchange.getGold());
			gold.setDeleted(false);
			gold.setOperator("system");
			gold.setGoldExchange(goldExchange);
			goldDao.persist(gold);

			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.product);
			deposit.setMemo(goldExchange.getMemo());
			deposit.setMember(member);
			deposit.setCredit(goldExchange.getAmount());
			deposit.setDebit(BigDecimal.ZERO);
			deposit.setDeleted(false);
			deposit.setOperator("system");
			depositDao.persist(deposit);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
		return true;
	}


}