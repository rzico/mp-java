package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Page;
import net.wit.Pageable;

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
			goldBuy.setStatus(GoldBuy.Status.success);
			goldBuyDao.persist(goldBuy);
			member.setPoint(member.getPoint()+goldBuy.getGold());
			memberDao.merge(member);
			Gold deposit = new Gold();
			deposit.setBalance(member.getPoint());
			deposit.setType(Gold.Type.recharge);
			deposit.setMemo(goldBuy.getMemo());
			deposit.setMember(member);
			deposit.setCredit(goldBuy.getGold());
			deposit.setDebit(0L);
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setBoldBuy(goldBuy);
			goldDao.persist(deposit);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
		return true;
	}

}