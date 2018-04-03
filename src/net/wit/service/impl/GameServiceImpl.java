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
import net.wit.dao.GoldDao;
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

	@Resource(name = "goldDaoImpl")
	private GoldDao goldDao;

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
			if (member.getPoint()<game.getDebit()) {
				throw new Exception("余额不足");
			}
			member.setPoint(member.getPoint()-(game.getDebit()));

			Long r = game.getDebit();
			if (member.getFreezePoint()<r) {
				member.setFreezePoint(0L);
			} else {
				member.setFreezePoint(member.getFreezePoint()-r);
			}

			memberDao.merge(member);
			memberDao.flush();
			Gold deposit = new Gold();
			deposit.setBalance(member.getPoint());
			deposit.setType(Gold.Type.transaction);
			deposit.setMemo(game.getMemo());
			deposit.setMember(member);
			deposit.setCredit(0L);
			deposit.setDebit(game.getDebit());
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setGame(game);
			goldDao.persist(deposit);
			gameDao.persist(game);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
	}


	public synchronized void history(Game game) throws Exception {
		Member member = game.getMember();
		try {
			if (game.getCredit()>0L) {
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				member.setPoint(member.getPoint()+game.getCredit());

				Long r = Math.round(game.getDebit()*0.6666);
				member.setFreezePoint(member.getFreezePoint()+r);

				memberDao.merge(member);
				memberDao.flush();
				Gold deposit = new Gold();
				deposit.setBalance(member.getPoint());
				deposit.setType(Gold.Type.history);
				deposit.setMemo(game.getMemo());
				deposit.setMember(member);
				deposit.setCredit(game.getCredit());
				deposit.setDebit(0L);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setGame(game);
				goldDao.persist(deposit);
			}
			game.setStatus(Game.Status.history);
			gameDao.persist(game);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
	}

	public Game find(Member member, String game,String tableNo,String roundNo) {
		return gameDao.find(member,game,tableNo,roundNo);
	}

}