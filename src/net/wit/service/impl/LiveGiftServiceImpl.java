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

import net.wit.dao.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.LiveGiftService;

/**
 * @ClassName: LiveGiftDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveGiftServiceImpl")
public class LiveGiftServiceImpl extends BaseServiceImpl<LiveGift, Long> implements LiveGiftService {
	@Resource(name = "liveGiftDaoImpl")
	private LiveGiftDao liveGiftDao;

	@Resource(name = "liveGiftDataDaoImpl")
	private LiveGiftDataDao liveGiftDataDao;

	@Resource(name = "liveDaoImpl")
	private LiveDao liveDao;

	@Resource(name = "liveTapeDaoImpl")
	private LiveTapeDao liveTapeDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "liveGiftDaoImpl")
	public void setBaseDao(LiveGiftDao liveGiftDao) {
		super.setBaseDao(liveGiftDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveGift liveGift) {
		super.save(liveGift);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGift update(LiveGift liveGift) {
		return super.update(liveGift);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGift update(LiveGift liveGift, String... ignoreProperties) {
		return super.update(liveGift, ignoreProperties);
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
	public void delete(LiveGift liveGift) {
		super.delete(liveGift);
	}

	public Page<LiveGift> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveGiftDao.findPage(beginDate,endDate,pageable);
	}

	public void add(LiveGift gift, Member member, Live live) throws Exception {

	   memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
	   if (member.getBalance().compareTo(new BigDecimal(gift.getPrice()))<0) {
	   	  throw  new RuntimeException("余额不足");
	   }
	   member.setBalance(member.getBalance().subtract(new BigDecimal(gift.getPrice())));
	   memberDao.merge(member);
	   memberDao.flush();

	   Deposit deposit = new Deposit();
	   deposit.setBalance(member.getBalance());
	   deposit.setCredit(BigDecimal.ZERO);
	   deposit.setDebit(new BigDecimal(gift.getPrice()));
	   deposit.setMember(member);
	   deposit.setMemo("送礼物"+gift.getName());
	   deposit.setDeleted(false);
	   deposit.setOperator("system");
	   deposit.setType(Deposit.Type.payment);
	   depositDao.persist(deposit);

       LiveGiftData data = new LiveGiftData();
       data.setGiftName(gift.getName());
       data.setHeadpic(member.getLogo());
       data.setLiveGift(gift);
       data.setLiveTape(live.getLiveTape());
       data.setMember(member);
       data.setNickname(member.displayName());
       data.setPrice(gift.getPrice());
       data.setThumbnail(gift.getThumbnail());
	   liveGiftDataDao.merge(data);

	   live.setGift(live.getGift()+gift.getPrice());
	   liveDao.merge(live);

	   LiveTape liveTape = live.getLiveTape();
	   liveTape.setGift(liveTape.getGift()+gift.getPrice());
	   liveTapeDao.merge(liveTape);

	}


	public void laud(Member member, Live live) throws Exception {

		live.setLikeCount(live.getLikeCount()+1L);
		liveDao.merge(live);

		LiveTape liveTape = live.getLiveTape();
		liveTape.setLikeCount(live.getLikeCount()+1L);
		liveTapeDao.merge(liveTape);

	}



	public void barrage(Member member, Live live) throws Exception {

		memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
		if (member.getBalance().compareTo(new BigDecimal("0.1"))<0) {
			throw  new RuntimeException("余额不足");
		}
		member.setBalance(member.getBalance().subtract(new BigDecimal("0.1")));
		memberDao.merge(member);
		memberDao.flush();

		Deposit deposit = new Deposit();
		deposit.setBalance(member.getBalance());
		deposit.setCredit(BigDecimal.ZERO);
		deposit.setDebit(new BigDecimal("0.1"));
		deposit.setMember(member);
		deposit.setMemo("发送弹幕信息");
		deposit.setDeleted(false);
		deposit.setOperator("system");
		deposit.setType(Deposit.Type.payment);
		depositDao.persist(deposit);

	}

}