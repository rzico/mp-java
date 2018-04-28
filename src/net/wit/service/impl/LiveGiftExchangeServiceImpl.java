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

import net.wit.dao.LiveDao;
import net.wit.dao.MemberDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.LiveGiftExchangeDao;
import net.wit.entity.*;
import net.wit.service.LiveGiftExchangeService;

/**
 * @ClassName: LiveGiftExchangeDaoImpl
 * @author 降魔战队
 * @date 2018-4-28 22:28:39
 */
 
 
@Service("liveGiftExchangeServiceImpl")
public class LiveGiftExchangeServiceImpl extends BaseServiceImpl<LiveGiftExchange, Long> implements LiveGiftExchangeService {

	@Resource(name = "liveGiftExchangeDaoImpl")
	private LiveGiftExchangeDao liveGiftExchangeDao;

	@Resource(name = "liveDaoImpl")
	private LiveDao liveDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "liveGiftExchangeDaoImpl")
	public void setBaseDao(LiveGiftExchangeDao liveGiftExchangeDao) {
		super.setBaseDao(liveGiftExchangeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveGiftExchange liveGiftExchange) {
		super.save(liveGiftExchange);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGiftExchange update(LiveGiftExchange liveGiftExchange) {
		return super.update(liveGiftExchange);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGiftExchange update(LiveGiftExchange liveGiftExchange, String... ignoreProperties) {
		return super.update(liveGiftExchange, ignoreProperties);
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
	public void delete(LiveGiftExchange liveGiftExchange) {
		super.delete(liveGiftExchange);
	}

	public Page<LiveGiftExchange> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveGiftExchangeDao.findPage(beginDate,endDate,pageable);
	}

	public void exchange(LiveGiftExchange liveGiftExchange) throws Exception {
		Live live = liveGiftExchange.getLive();
		Member member = liveGiftExchange.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		liveDao.refresh(live,LockModeType.PESSIMISTIC_WRITE);

		live.setGift(live.getGift()-liveGiftExchange.getGift());
		if (live.getGift()<0) {
			throw new RuntimeException("兑换印票数不足");
		}
		liveDao.merge(live);

		member.setBalance(member.getBalance().add(liveGiftExchange.getAmount()));
		memberDao.merge(member);

		liveGiftExchangeDao.persist(liveGiftExchange);


	}

}