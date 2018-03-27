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

import net.wit.dao.GoldBuyDao;
import net.wit.service.GoldBuyService;

/**
 * @ClassName: GoldBuyDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */
 
 
@Service("goldBuyServiceImpl")
public class GoldBuyServiceImpl extends BaseServiceImpl<GoldBuy, Long> implements GoldBuyService {
	@Resource(name = "goldBuyDaoImpl")
	private GoldBuyDao goldBuyDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "goldDaoImpl")
	private GoldDao goldDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "goldBuyDaoImpl")
	public void setBaseDao(GoldBuyDao goldBuyDao) {
		super.setBaseDao(goldBuyDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GoldBuy goldBuy) {
		super.save(goldBuy);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldBuy update(GoldBuy goldBuy) {
		return super.update(goldBuy);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldBuy update(GoldBuy goldBuy, String... ignoreProperties) {
		return super.update(goldBuy, ignoreProperties);
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
	public void delete(GoldBuy goldBuy) {
		super.delete(goldBuy);
	}

	public Page<GoldBuy> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goldBuyDao.findPage(beginDate,endDate,pageable);
	}


	@Transactional
	public synchronized Boolean submit(GoldBuy goldBuy) throws Exception {
		Member member = goldBuy.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {
			member.setBalance(member.getBalance().subtract(goldBuy.getAmount()));
			if (member.getBalance().compareTo(BigDecimal.ZERO)<0) {
				throw new RuntimeException("余额不足");
			}

			member.setPoint(member.getPoint()+goldBuy.getGold());
			member.setFreezePoint(member.getFreezePoint()+goldBuy.getGold());
			memberDao.merge(member);

			goldBuy.setStatus(GoldBuy.Status.success);
			goldBuyDao.persist(goldBuy);

			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.payment);
			deposit.setMemo(goldBuy.getMemo());
			deposit.setMember(member);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(goldBuy.getAmount());
			deposit.setDeleted(false);
			deposit.setOperator("system");
			depositDao.persist(deposit);

			Gold gold = new Gold();
			gold.setBalance(member.getPoint());
			gold.setType(Gold.Type.recharge);
			gold.setMemo(goldBuy.getMemo());
			gold.setMember(member);
			gold.setCredit(goldBuy.getGold());
			gold.setDebit(0L);
			gold.setDeleted(false);
			gold.setOperator("system");
			gold.setBoldBuy(goldBuy);
			goldDao.persist(gold);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
		return true;
	}

}