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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.GameDao;
import net.wit.entity.*;
import net.wit.service.GameService;

/**
 * @ClassName: GameDaoImpl
 * @author 降魔战队
 * @date 2018-2-3 21:28:27
 */
 
 
@Service("gameServiceImpl")
public class GameServiceImpl extends BaseServiceImpl<Game, Long> implements GameService {

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "gameDaoImpl")
	private GameDao gameDao;

	@Resource(name = "gameDaoImpl")
	public void setBaseDao(GameDao gameDao) {
		super.setBaseDao(gameDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Game game) {
		super.save(game);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Game update(Game game) {
		return super.update(game);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Game update(Game game, String... ignoreProperties) {
		return super.update(game, ignoreProperties);
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
	public void delete(Game game) {
		super.delete(game);
	}

	public Page<Game> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gameDao.findPage(beginDate,endDate,pageable);
	}

	public synchronized void sumbit(Game game) throws Exception {
		Member member = game.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {
			member.setBalance(member.getBalance().subtract(game.getDebit()));
			if (member.getBalance().compareTo(BigDecimal.ZERO)<0) {
				throw new Exception("余额不足");
			}
			memberDao.merge(member);
			memberDao.flush();
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.payment);
			deposit.setMemo(game.getMemo());
			deposit.setMember(member);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(game.getDebit());
			deposit.setDeleted(false);
			deposit.setOperator("system");
			depositDao.persist(deposit);
			gameDao.persist(game);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
	}


	public synchronized void history(Game game) throws Exception {
		Member member = game.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		try {
			member.setBalance(member.getBalance().add(game.getCredit()));
			memberDao.merge(member);
			memberDao.flush();
			if (game.getCredit().compareTo(BigDecimal.ZERO)>0) {
				Deposit deposit = new Deposit();
				deposit.setBalance(member.getBalance());
				deposit.setType(Deposit.Type.reward);
				deposit.setMemo(game.getMemo());
				deposit.setMember(member);
				deposit.setCredit(game.getCredit());
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				depositDao.persist(deposit);
			}
			game.setStatus(Game.Status.history);
			gameDao.persist(game);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
	}

	public Game find(String game,String tableNo,String roundNo) {
		return gameDao.find(game,tableNo,roundNo);
	}

}